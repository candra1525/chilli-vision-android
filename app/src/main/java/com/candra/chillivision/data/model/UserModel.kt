package com.candra.chillivision.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val token: String = "",
    val id : String = "",
    val fullname : String = "",
    val noHandphone : String = "",
    val image : String = "",
    val subscriptionName : String = "",
    val startDateSubscription : String = "",
    val endDateSubscription : String = "",
) : Parcelable