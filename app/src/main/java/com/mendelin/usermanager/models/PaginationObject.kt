package com.mendelin.usermanager.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class PaginationObject(
    val total: Int,
    val pages: Int,
    val page: Int,
    val limit: Int
) : Parcelable


