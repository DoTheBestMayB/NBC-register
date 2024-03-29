package com.dothebestmayb.nbc_register.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dothebestmayb.nbc_register.model.PictureValue
import kotlin.random.Random

class HomeActivityViewModel : ViewModel() {

    private val _uerId = MutableLiveData<String>()
    val userId: LiveData<String>
        get() = _uerId

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String>
        get() = _userName

    private val _userAge = MutableLiveData<Int>()
    val userAge: LiveData<Int>
        get() = _userAge

    private val _userMbti = MutableLiveData<String>()
    val userMbti: LiveData<String>
        get() = _userMbti

    fun setUserInfo(id: String, name: String, age: Int? = null, mbti: String? = null) {
        _uerId.value = id
        _userName.value = name
        _userAge.value = age ?: DEFAULT_AGE
        _userMbti.value = mbti ?: DEFAULT_MBTI
    }

    private val random = Random(System.currentTimeMillis())

    private val _pictureValue = MutableLiveData<PictureValue>()
    val pictureValue: LiveData<PictureValue>
        get() = _pictureValue

    init {
        _pictureValue.value = PictureValue.entries.random(random)
    }

    companion object {
        private const val DEFAULT_AGE = 30
        private const val DEFAULT_MBTI = "ESTJ"
    }
}
