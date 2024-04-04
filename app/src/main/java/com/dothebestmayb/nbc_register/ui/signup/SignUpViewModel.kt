package com.dothebestmayb.nbc_register.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dothebestmayb.nbc_register.model.SignUpErrorType
import com.dothebestmayb.nbc_register.model.UserInfo
import com.dothebestmayb.nbc_register.ui.UserRepository

class SignUpViewModel : ViewModel() {

    private val validator = Validator()

    private val _inputName = MutableLiveData<String>()
    val inputName: LiveData<String>
        get() = _inputName

    private val _inputEmailFront = MutableLiveData<String>()
    private val _inputEmailTail = MutableLiveData<String>()

    private val _inputPw = MutableLiveData<String>()
    val inputPw: LiveData<String>
        get() = _inputPw

    private val _inputPwCheck = MutableLiveData<String>()

    private val _isEmailFilled = MediatorLiveData<Boolean>().apply {
        listOf(_inputEmailFront, _inputEmailTail).forEach {
            addSource(it) {
                this.value = checkEmailNotFilled()
            }
        }
    }
    val isEmailFilled: LiveData<Boolean>
        get() = _isEmailFilled

    private fun checkEmailNotFilled() =
        _inputEmailFront.value.isNullOrBlank() || _inputEmailTail.value.isNullOrBlank()


    private val _isAllInputFilled = MediatorLiveData<Boolean>().apply {
        listOf(_inputName, _inputEmailFront, _inputEmailTail, _inputPw, _inputPwCheck).forEach {
            addSource(it) {
                this.value = checkAllInputFilled()
            }
        }
    }

    private fun checkAllInputFilled() =
        !(checkEmailNotFilled() || _inputName.value.isNullOrBlank() || _inputPw.value.isNullOrBlank() || _inputPwCheck.value.isNullOrBlank())

    val isAllInputFilled: LiveData<Boolean>
        get() = _isAllInputFilled

    private val _registeredUserInfo = MutableLiveData<UserInfo>()
    val registeredUserInfo: LiveData<UserInfo>
        get() = _registeredUserInfo

    private val _errorType = MutableLiveData<SignUpErrorType>()
    val errorType: LiveData<SignUpErrorType>
        get() = _errorType

    fun updateInputPw(input: String) {
        _inputPw.value = input
    }

    fun updateInputPwCheck(input: String) {
        _inputPwCheck.value = input
    }

    fun updateInputName(input: String) {
        _inputName.value = input
    }

    fun updateInputEmailFront(input: String) {
        _inputEmailFront.value = input
    }

    fun updateInputEmailTail(input: String) {
        _inputEmailTail.value = input
    }

    fun signUp() {
        val name = _inputName.value ?: run {
            _errorType.value = SignUpErrorType.NO_INPUT
            return
        }
        val pw = _inputPw.value ?: run {
            _errorType.value = SignUpErrorType.NO_INPUT
            return
        }
        val emailFront = _inputEmailFront.value ?: run {
            _errorType.value = SignUpErrorType.NO_INPUT
            return
        }
        val emailTail = _inputEmailTail.value ?: run {
            _errorType.value = SignUpErrorType.NO_INPUT
            return
        }
        val email = "$emailFront@$emailTail"

        if (UserRepository.checkRegisterEmailPossible(email).not()) {
            _errorType.value = SignUpErrorType.ALREADY_REGISTERED_ID
            return
        }
        if (checkPwValidity(pw).not()) {
            return
        }
        _registeredUserInfo.value = UserInfo(name, email, pw).apply {
            UserRepository.registerUserInfo(this)
        }
    }

    private fun checkPwValidity(pw: String): Boolean {
        // 8 ~ 16자리 확인
        if (validator.checkLength(pw).not()) {
            _errorType.value = SignUpErrorType.PW_LENGTH_IS_NOT_CORRECT
            return false
        }
        // 대문자 반드시 포함
        if (validator.checkContainCapital(pw).not()) {
            _errorType.value = SignUpErrorType.CAPITAL_IS_NOT_CONTAINED
            return false
        }
        // 특수문자 !, @, #를 1개 이상 포함하는지 확인
        if (validator.checkContainAtLeastOneSpecialCharacter(pw).not()) {
            _errorType.value = SignUpErrorType.SPECIAL_CHARACTER_IS_NOT_CONTAINED
            return false
        }
        // 허용되지 않는 특수문자를 포함하는지 확인
        if (validator.checkContainNotAllowedSpecialCharacter(pw)) {
            _errorType.value = SignUpErrorType.NOT_ALLOWED_CHARACTER_IS_CONTAINED
            return false
        }
        return true
    }
}
