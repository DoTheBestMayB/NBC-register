package com.dothebestmayb.nbc_register.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserInfo(
    val name: String,
    val id: String,
    val pw: String,
) : Parcelable
