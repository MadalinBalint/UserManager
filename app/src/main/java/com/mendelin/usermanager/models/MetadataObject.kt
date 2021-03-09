package com.mendelin.usermanager.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class MetadataObject(
    val pagination: PaginationObject
) : Parcelable


