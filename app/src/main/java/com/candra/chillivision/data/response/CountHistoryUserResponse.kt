package com.candra.chillivision.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CountHistoryUserResponse(

	@field:SerializedName("data")
	val data: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable
