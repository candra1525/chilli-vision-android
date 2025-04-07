package com.candra.chillivision.data.response.historyAnalysis

data class CreateHistoryRequest(
    val image: String,
    val detection_time: String,
    val user_id: String,
    val unique_name_disease: String,
    val history_details: List<HistoryDetail>
)

data class HistoryDetail(
    val name_disease: String,
    val another_name_disease: String,
    val symptom: String,
    val reason: String,
    val preventive_meansure: String,
    val source: String,
    val confidence_score: String
)
