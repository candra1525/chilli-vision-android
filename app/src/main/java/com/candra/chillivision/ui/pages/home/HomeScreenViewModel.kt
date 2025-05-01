package com.candra.chillivision.ui.pages.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.candra.chillivision.data.repository.ChilliVisionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val repository: ChilliVisionRepository) : ViewModel() {

    private val _startDateSubscription = MutableStateFlow("")
    val startDateSubscription: StateFlow<String> get() = _startDateSubscription

    private val _endDateSubscription = MutableStateFlow("")
    val endDateSubscription: StateFlow<String> get() = _endDateSubscription

    init {
        // Ambil nilai awal countUsageAI dari preferences (Flow<String>)
        viewModelScope.launch {
            repository.getStartDateSubscription().collect { date ->
                _startDateSubscription.emit(date)
            }
            repository.getEndDateSubscription().collect { date ->
                _endDateSubscription.emit(date)
            }
        }
    }

    fun getPreferences() = repository.getPreferences()

    suspend fun setSubscriptionName(subscriptionName: String) {
        repository.setSubscriptionName(subscriptionName)
    }

    suspend fun setStartEndSubscriptionDate(startDate: String, endDate: String) {
        repository.setStartEndSubscriptionDate(startDate, endDate)
    }

    fun checkSubscriptionActive(idUser : String) = repository.checkSubscriptionActive(idUser = idUser)

    fun updateStatusSubscriptionUser(idSubscription: String, status : String) = repository.updateStatusSubscriptionUser(idSubscription = idSubscription, status = status)
}