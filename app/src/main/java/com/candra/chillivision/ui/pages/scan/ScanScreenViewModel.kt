package com.candra.chillivision.ui.pages.scan

import androidx.lifecycle.ViewModel
import com.candra.chillivision.data.repository.ChilliVisionRepository
import okhttp3.MultipartBody

class ScanScreenViewModel(private val repository: ChilliVisionRepository) : ViewModel() {
    fun sendPrediction(
        file: MultipartBody.Part
    ) = repository.sendPrediction(
        file = file
    )
}