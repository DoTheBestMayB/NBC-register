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
                this.value = checkEmailFilled()
            }
        }
    }
    val isEmailFilled: LiveData<Boolean>
        get() = _isEmailFilled

    private fun checkEmailFilled() = _inputEmailFront.value?.isNotBlank() == true || _inputEmailTail.value?.isNotBlank() == true

    private val isPwValid = MutableLiveData(false)

    private val _isAllValid = MediatorLiveData<Boolean>().apply {
        listOf(_inputName, _inputEmailFront, _inputEmailTail, _inputPw, _inputPwCheck).forEach {
            addSource(it) {
                this.value = checkAllValid()
            }
        }
    }
    val isAllValid: LiveData<Boolean>
        get() = _isAllValid

    private fun checkAllValid() = checkEmailFilled() && _inputName.value?.isNotBlank() == true && isPwValid.value == true && checkPwSame()

    private fun checkPwSame() = _inputPw.value == _inputPwCheck.value

    private val _registeredUserInfo = MutableLiveData<UserInfo>()
    val registeredUserInfo: LiveData<UserInfo>
        get() = _registeredUserInfo

    private val _errorType = MutableLiveData<SignUpErrorType>()
    val errorType: LiveData<SignUpErrorType>
        get() = _errorType

    fun updateInputPw(input: String) {
        checkPwValidity(input)
        _inputPw.value = input
    }

    fun updateInputPwCheck(input: String) {
        _inputPwCheck.value = input
    }

    private fun checkPwValidity(pw: String) {
        // 10자리 이상, 대문자 반드시 포함, 특수문자 !, @, #를 1개 이상 포함, 허용되지 않는 특수문자를 포함하지 않음
        isPwValid.value = validator.checkLengthValid(pw) && validator.checkContainCapital(pw) &&
                validator.checkContainAtLeastOneSpecialCharacter(pw) && validator.checkNotContainNotAllowedSpecialCharacter(pw)
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
        val userInfo = UserInfo(name, email, pw)
        if (UserRepository.registerUserInfo(userInfo)) {
            _registeredUserInfo.value = userInfo
        } else {
            _errorType.value = SignUpErrorType.ALREADY_REGISTERED_ID
        }
    }
}
