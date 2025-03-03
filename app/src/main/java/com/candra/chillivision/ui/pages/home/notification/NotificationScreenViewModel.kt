package com.candra.chillivision.ui.pages.home.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.candra.chillivision.data.common.Result
import com.candra.chillivision.data.repository.ChilliVisionRepository
import com.candra.chillivision.data.response.notification.NotificationAllResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotificationScreenViewModel(private val repository: ChilliVisionRepository) : ViewModel() {

    private val _notification = MutableStateFlow<Result<NotificationAllResponse>>(
        Result.Loading
    )
    val notification: StateFlow<Result<NotificationAllResponse>> = _notification.asStateFlow()

    init {
        getAllNotification()
    }


    fun getAllNotification() {
        viewModelScope.launch {
            repository.getAllNotification().collect { result ->
                _notification.value = result
            }
        }
    }
}