package com.dothebestmayb.nbc_register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dothebestmayb.nbc_register.model.ErrorType
import com.dothebestmayb.nbc_register.model.UserInfo

class SignUpViewModel: ViewModel() {

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

    private val _errorMessage = MutableLiveData<ErrorType>()
    val errorMessage: LiveData<ErrorType>
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
            _errorMessage.value = ErrorType.NO_INPUT
            return
        }
        val id = _inputId.value ?: run {
            _errorMessage.value = ErrorType.NO_INPUT
            return
        }
        val pw = _inputPw.value ?: run {
            _errorMessage.value = ErrorType.NO_INPUT
            return
        }
        _registeredUserInfo.value = UserInfo(name, id, pw)
    }

}
