package com.candra.chillivision.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreateHistoryUserResponse(

	@field:SerializedName("data")
	val data: CreateHistoryUser? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class CreateHistoryUser(

	@field:SerializedName("end_date")
	val endDate: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("image_transaction")
	val imageTransaction: String? = null,

	@field:SerializedName("start_date")
	val startDate: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
) : Parcelable
