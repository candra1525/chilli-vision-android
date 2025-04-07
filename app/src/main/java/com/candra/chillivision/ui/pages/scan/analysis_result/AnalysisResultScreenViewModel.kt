package com.candra.chillivision.ui.pages.scan.analysis_result

import androidx.lifecycle.ViewModel
import com.candra.chillivision.data.repository.ChilliVisionRepository
import com.candra.chillivision.data.response.historyAnalysis.CreateHistoryRequest
import okhttp3.MultipartBody

class AnalysisResultScreenViewModel(private val repository: ChilliVisionRepository) : ViewModel()
{
    fun createHistory(
        request : CreateHistoryRequest
    ) = repository.createHistory(request)

    fun getPreferences() = repository.getPreferences()
}