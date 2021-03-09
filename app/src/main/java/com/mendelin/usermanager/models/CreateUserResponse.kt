package com.mendelin.usermanager.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class CreateUserResponse(
    val code: Int,
    val meta: MetadataObject,
    val data: UserObject
) : Parcelable