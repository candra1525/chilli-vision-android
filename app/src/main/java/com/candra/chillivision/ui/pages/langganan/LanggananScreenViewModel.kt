package com.candra.chillivision.ui.pages.langganan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.candra.chillivision.data.repository.ChilliVisionRepository
import com.candra.chillivision.data.response.subscriptions.SubscriptionsGetAllResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.candra.chillivision.data.common.Result
import com.candra.chillivision.data.response.ListHistorySubscriptionActiveResponse
import com.candra.chillivision.data.response.ListHistorySubscriptionResponse

class LanggananScreenViewModel(private val repository: ChilliVisionRepository) : ViewModel() {

    private val _subscriptions = MutableStateFlow<Result<SubscriptionsGetAllResponse>>(Result.Loading)
    val subscriptions: StateFlow<Result<SubscriptionsGetAllResponse>> = _subscriptions.asStateFlow()

    private val _subscriptionsActive = MutableStateFlow<Result<ListHistorySubscriptionActiveResponse>>(Result.Loading)
    val subscriptionsActive: StateFlow<Result<ListHistorySubscriptionActiveResponse>> = _subscriptionsActive.asStateFlow()

    private val _subscriptionsHistory = MutableStateFlow<Result<ListHistorySubscriptionResponse>>(Result.Loading)
    val subscriptionsHistory: StateFlow<Result<ListHistorySubscriptionResponse>> = _subscriptionsHistory.asStateFlow()

    init { fetchSubscriptions() }

    fun fetchSubscriptions() {
        viewModelScope.launch {
            repository.getAllSubscriptions()
                .collect { result ->
                    _subscriptions.value = result
                }
        }
    }

    fun fetchSubscriptionsActive(idUser : String){
        viewModelScope.launch {
            repository.getHistorySubscriptionUserActive(idUser = idUser)
                .collect { result ->
                    _subscriptionsActive.value = result
                }
        }
    }

    fun fetchSubscriptionsHistory(idUser : String){
        viewModelScope.launch {
            repository.getAllHistorySubscription(idUser = idUser)
                .collect { result ->
                    _subscriptionsHistory.value = result
                }
        }
    }

    fun getPreferences() = repository.getPreferences()

}
