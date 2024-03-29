package com.dothebestmayb.nbc_register.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dothebestmayb.nbc_register.model.SignInErrorType
import com.dothebestmayb.nbc_register.model.UserInfo
import com.dothebestmayb.nbc_register.ui.UserRepository

class SignInViewModel: ViewModel() {

    private val _inputId = MutableLiveData<String>()
    private val _inputPw = MutableLiveData<String>()

    private val _isAllInputFilled = MediatorLiveData<Boolean>().apply {
        var forId = false
        var forPw = false
        addSource(_inputId) {
            forId = it.isNotBlank()
            this.value = forId && forPw
        }
        addSource(_inputPw) {
            forPw = it.isNotBlank()
            this.value = forId && forPw
        }
    }
    val isAllInputFilled: LiveData<Boolean>
        get() = _isAllInputFilled

    private val _loggedUserInfo = MutableLiveData<UserInfo>()
    val loggedUserInfo: LiveData<UserInfo>
        get() = _loggedUserInfo

    private val _errorMessage = MutableLiveData<SignInErrorType>()
    val errorMessage: LiveData<SignInErrorType>
        get() = _errorMessage

    fun updateInputId(inputId: String) {
        _inputId.value = inputId
    }

    fun updateInputPw(inputPw: String) {
        _inputPw.value = inputPw
    }

    fun login() {
        val id = _inputId.value ?: run {
            _errorMessage.value = SignInErrorType.NO_ID_INPUT
            return
        }
        val pw = _inputPw.value ?: run {
            _errorMessage.value = SignInErrorType.NO_PW_INPUT
            return
        }
        val userInfo = UserRepository.getUserInfo(id, pw) ?: run {
            _errorMessage.value = SignInErrorType.NOT_VALID
            return
        }
        if (userInfo.pw != _inputPw.value) {
            _errorMessage.value = SignInErrorType.NOT_VALID
            return
        }
        _loggedUserInfo.value = userInfo
    }
}