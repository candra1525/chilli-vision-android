package com.candra.chillivision.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailHistoryUserResponse(

	@field:SerializedName("data")
	val data: DataDetailHistoryUser? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class DataDetailHistoryUser(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("url_image")
	val urlImage: String? = null
) : Parcelable
