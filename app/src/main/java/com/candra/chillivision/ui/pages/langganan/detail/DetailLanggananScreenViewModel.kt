package com.candra.chillivision.ui.pages.langganan.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.candra.chillivision.data.common.Result
import com.candra.chillivision.data.repository.ChilliVisionRepository
import com.candra.chillivision.data.response.subscriptions.SubscriptionsGetDetailResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailLanggananScreenViewModel (private val repository : ChilliVisionRepository) : ViewModel(){

    private val _detailSubscriptions = MutableStateFlow<Result<SubscriptionsGetDetailResponse>>(Result.Loading)
    val detailSubscriptions : StateFlow<Result<SubscriptionsGetDetailResponse>> = _detailSubscriptions.asStateFlow()

    fun fetchDetailSubscriptions(id : String) {
        viewModelScope.launch {
            repository.getDetailSubscription(id = id)
                .collect { result ->
                    _detailSubscriptions.value = result
                }
        }
    }
}