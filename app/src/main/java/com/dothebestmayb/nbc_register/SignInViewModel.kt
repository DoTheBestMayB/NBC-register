package com.dothebestmayb.nbc_register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dothebestmayb.nbc_register.model.SignInErrorType
import com.dothebestmayb.nbc_register.model.UserInfo

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

    private val registeredInfo = hashMapOf<String, UserInfo>()

    fun updateInputId(inputId: String) {
        _inputId.value = inputId
    }

    fun updateInputPw(inputPw: String) {
        _inputPw.value = inputPw
    }

    fun login() {
        val userInfo = registeredInfo[_inputId.value] ?: run {
            _errorMessage.value = SignInErrorType.NO_USER_EXIST
            return
        }
        if (userInfo.pw == _inputPw.value) {
            _loggedUserInfo.value = userInfo
        }
    }

    fun registerUserInfo(userInfo: UserInfo) {
        registeredInfo[userInfo.id] = userInfo
    }

}