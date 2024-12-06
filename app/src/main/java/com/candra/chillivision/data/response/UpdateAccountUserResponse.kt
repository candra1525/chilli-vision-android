package com.candra.chillivision.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class UpdateAccountUserResponse(

	@field:SerializedName("data")
	val data: DataUpdateAccountUser? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class DataUpdateAccountUser(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("no_handphone")
	val noHandphone: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("fullname")
	val fullname: String? = null,

	@field:SerializedName("email")
	val email: String? = null
) : Parcelable
