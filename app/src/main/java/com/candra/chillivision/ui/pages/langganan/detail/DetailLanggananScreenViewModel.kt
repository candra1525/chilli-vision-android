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
import okhttp3.MultipartBody

class DetailLanggananScreenViewModel(private val repository: ChilliVisionRepository) : ViewModel() {

    private val _detailSubscriptions =
        MutableStateFlow<Result<SubscriptionsGetDetailResponse>>(Result.Loading)
    val detailSubscriptions: StateFlow<Result<SubscriptionsGetDetailResponse>> =
        _detailSubscriptions.asStateFlow()

    fun fetchDetailSubscriptions(id: String) {
        viewModelScope.launch {
            repository.getDetailSubscription(id = id)
                .collect { result ->
                    _detailSubscriptions.value = result
                }
        }
    }

    fun getPreferences() = repository.getPreferences()

    fun setCreateHistorySubscription(
        subscriptionId: String,
        userId: String,
        startDate: String,
        endDate: String,
        imageTransaction: MultipartBody.Part
    ) = repository.setCreateHistorySubscription(
        subscription_id = subscriptionId,
        user_id = userId,
        start_date = startDate,
        end_date = endDate,
        image_transaction = imageTransaction
    )


}