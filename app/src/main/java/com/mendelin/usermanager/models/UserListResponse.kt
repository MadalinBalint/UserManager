package com.mendelin.usermanager.models

import androidx.annotation.Keep

@Keep
data class UserListResponse(
    val code: Int,
    val meta: MetadataObject,
    val data: List<UserObject>,
    val message: String?
)