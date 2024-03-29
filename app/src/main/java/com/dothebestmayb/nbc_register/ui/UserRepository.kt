package com.dothebestmayb.nbc_register.ui

import com.dothebestmayb.nbc_register.model.UserInfo

object UserRepository {
    private val registeredInfo = hashMapOf<String, UserInfo>()

    fun getUserInfo(userId: String, userPw: String): UserInfo? {
        val info = registeredInfo[userId] ?: return null
        if (info.pw != userPw) {
            return null
        }
        return info
    }

    fun registerUserInfo(userInfo: UserInfo): Boolean {
        if (userInfo.id in registeredInfo) {
            return false
        }
        registeredInfo[userInfo.id] = userInfo
        return true
    }

    fun checkRegisterIdPossible(id: String): Boolean {
        return id !in registeredInfo
    }
}