package com.mendelin.usermanager.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class UserObject(
    val id: Int,
    val name: String,
    val email: String,
    val gender: String,
    val status: String,
    val created_at: String,
    val updated_at: String
) : Parcelable