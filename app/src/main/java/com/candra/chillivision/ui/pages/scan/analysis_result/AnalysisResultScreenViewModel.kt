package com.candra.chillivision.ui.pages.scan.analysis_result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.candra.chillivision.data.common.Result
import com.candra.chillivision.data.repository.ChilliVisionRepository
import com.candra.chillivision.data.response.historyAnalysis.CreateHistoryRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class AnalysisResultScreenViewModel(private val repository: ChilliVisionRepository) : ViewModel()
{

    private val _countHistoryUser = MutableStateFlow(0)
    val countHistoryUser: StateFlow<Int> get() = _countHistoryUser


    fun createHistory(
        request : CreateHistoryRequest
    ) = repository.createHistory(request)

    fun getPreferences() = repository.getPreferences()

    fun getCountHistoryUser(idUser: String) {
        viewModelScope.launch {
            repository.getCountHistoryUser(idUser).collect { result ->
                when (result){
                    is Result.Success -> {
                        val data = result.data
                        _countHistoryUser.emit(data.data ?: 0)
                    }
                    else -> {
                        // kasih -1
                        _countHistoryUser.emit(-1)
                    }
                }

            }
        }
    }

}