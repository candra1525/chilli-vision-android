package com.candra.chillivision.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val token: String,
    val id : String,
    val fullname : String,
    val noHandphone : String,
    val email : String,
) : Parcelable