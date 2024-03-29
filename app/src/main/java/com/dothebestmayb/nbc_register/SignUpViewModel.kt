package com.dothebestmayb.nbc_register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dothebestmayb.nbc_register.model.SignUpErrorType
import com.dothebestmayb.nbc_register.model.UserInfo

class SignUpViewModel : ViewModel() {

    private val validator = Validator()

    private val _inputId = MutableLiveData<String>()
    private val _inputPw = MutableLiveData<String>()
    private val _inputName = MutableLiveData<String>()

    private val _isAllInputFilled = MediatorLiveData<Boolean>().apply {
        var forId = false
        var forPw = false
        var forName = false
        addSource(_inputId) {
            forId = it.isNotBlank()
            this.value = forId && forPw && forName
        }
        addSource(_inputPw) {
            forPw = it.isNotBlank()
            this.value = forId && forPw && forName
        }
        addSource(_inputName) {
            forName = it.isNotBlank()
            this.value = forId && forPw && forName
        }
    }
    val isAllInputFilled: LiveData<Boolean>
        get() = _isAllInputFilled

    private val _registeredUserInfo = MutableLiveData<UserInfo>()
    val registeredUserInfo: LiveData<UserInfo>
        get() = _registeredUserInfo

    private val _errorMessage = MutableLiveData<SignUpErrorType>()
    val errorMessage: LiveData<SignUpErrorType>
        get() = _errorMessage

    fun updateInputId(inputId: String) {
        _inputId.value = inputId
    }

    fun updateInputPw(inputPw: String) {
        _inputPw.value = inputPw
    }

    fun updateInputName(inputName: String) {
        _inputName.value = inputName
    }

    fun signUp() {
        val name = _inputName.value ?: run {
            _errorMessage.value = SignUpErrorType.NO_INPUT
            return
        }
        val id = _inputId.value ?: run {
            _errorMessage.value = SignUpErrorType.NO_INPUT
            return
        }
        val pw = _inputPw.value ?: run {
            _errorMessage.value = SignUpErrorType.NO_INPUT
            return
        }
        if (checkPwValidity(pw).not()) {
            return
        }
        _registeredUserInfo.value = UserInfo(name, id, pw)
    }

    private fun checkPwValidity(pw: String): Boolean {
        // 8 ~ 16자리 확인
        if (validator.checkLength(pw).not()) {
            _errorMessage.value = SignUpErrorType.PW_LENGTH_IS_NOT_CORRECT
            return false
        }
        // 대문자 반드시 포함
        if (validator.checkContainCapital(pw).not()) {
            _errorMessage.value = SignUpErrorType.CAPITAL_IS_NOT_CONTAINED
            return false
        }
        // 특수문자 !, @, #를 1개 이상 포함하는지 확인
        if (validator.checkContainAtLeastOneSpecialCharacter(pw).not()) {
            _errorMessage.value = SignUpErrorType.SPECIAL_CHARACTER_IS_NOT_CONTAINED
            return false
        }
        // 허용되지 않는 특수문자를 포함하는지 확인
        if (validator.checkContainNotAllowedSpecialCharacter(pw)) {
            _errorMessage.value = SignUpErrorType.NOT_ALLOWED_CHARACTER_IS_CONTAINED
            return false
        }
        return true
    }
}
