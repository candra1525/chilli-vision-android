package com.candra.chillivision.ui.pages.scan.confirm_scan

import androidx.lifecycle.ViewModel
import com.candra.chillivision.data.repository.ChilliVisionRepository
import okhttp3.MultipartBody

class ConfirmScanScreenViewModel (private val repository: ChilliVisionRepository) : ViewModel()
{
    fun sendPrediction(
        file: MultipartBody.Part
    ) = repository.sendPrediction(
        file = file
    )
}

