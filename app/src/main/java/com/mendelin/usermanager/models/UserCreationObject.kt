package com.mendelin.usermanager.models

import androidx.annotation.Keep

@Keep
data class UserCreationObject(
    val name: String,
    val gender: String,
    val email: String,
    val status: String,
)