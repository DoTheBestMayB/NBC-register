package com.dothebestmayb.nbc_register.ui

import com.dothebestmayb.nbc_register.model.UserInfo

object UserRepository {
    private val registeredInfo = hashMapOf<String, UserInfo>()

    fun getUserInfo(userEmail: String, userPw: String): UserInfo? {
        val info = registeredInfo[userEmail] ?: return null
        if (info.pw != userPw) {
            return null
        }
        return info
    }

    fun registerUserInfo(userInfo: UserInfo): Boolean {
        if (userInfo.email in registeredInfo) {
            return false
        }
        registeredInfo[userInfo.email] = userInfo
        return true
    }

    fun checkRegisterEmailPossible(email: String): Boolean {
        return email !in registeredInfo
    }
}