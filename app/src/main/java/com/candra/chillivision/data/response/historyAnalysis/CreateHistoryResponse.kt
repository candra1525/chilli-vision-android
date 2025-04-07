package com.candra.chillivision.data.response.historyAnalysis

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreateHistoryResponse(

	@field:SerializedName("data")
	val data: CreateHistory? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class HistoryDetailsItem(

	@field:SerializedName("reason")
	val reason: String? = null,

	@field:SerializedName("symptom")
	val symptom: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("preventive_measure")
	val preventiveMeasure: String? = null,

	@field:SerializedName("confidence_score")
	val confidenceScore: String? = null,

	@field:SerializedName("name_disease")
	val nameDisease: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("source")
	val source: String? = null,

	@field:SerializedName("history_id")
	val historyId: String? = null,

	@field:SerializedName("another_name_disease")
	val anotherNameDisease: String? = null,

	@field:SerializedName("deleted_at")
	val deletedAt: String? = null
) : Parcelable

@Parcelize
data class CreateHistory(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("history_details")
	val historyDetails: List<HistoryDetailsItem?>? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("detection_time")
	val detectionTime: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("url_image")
	val urlImage: String? = null
) : Parcelable
