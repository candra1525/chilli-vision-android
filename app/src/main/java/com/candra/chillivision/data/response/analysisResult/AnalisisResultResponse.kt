package com.candra.chillivision.data.response.analysisResult

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AnalisisResultResponse(

	@field:SerializedName("detections_summary")
	val detectionsSummary: List<DetectionsSummaryItem?>? = null,

	@field:SerializedName("unique_name_disease")
	val uniqueNameDisease: String? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("detection_time")
	val detectionTime: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("detections")
	val detections: List<DetectionsItem?>? = null
) : Parcelable

@Parcelize
data class DetectionsItem(

	@field:SerializedName("disease_info")
	val diseaseInfo: DiseaseInfo? = null,

	@field:SerializedName("bbox")
	val bbox: List<Int?>? = null,

	@field:SerializedName("confidence")
	val confidence: String? = null,

	@field:SerializedName("label")
	val label: String? = null
) : Parcelable

@Parcelize
data class DetectionsSummaryItem(

	@field:SerializedName("disease_info")
	val diseaseInfo: DiseaseInfo? = null,

	@field:SerializedName("label")
	val label: String? = null
) : Parcelable

@Parcelize
data class DiseaseInfo(

	@field:SerializedName("tindakan_pencegahan")
	val tindakanPencegahan: String? = null,

	@field:SerializedName("sumber")
	val sumber: String? = null,

	@field:SerializedName("nama_lain")
	val namaLain: String? = null,

	@field:SerializedName("nama_penyakit")
	val namaPenyakit: String? = null,

	@field:SerializedName("penyebab")
	val penyebab: String? = null,

	@field:SerializedName("gejala")
	val gejala: String? = null,

	@field:SerializedName("prediksi")
	val prediksi: String? = null
) : Parcelable
