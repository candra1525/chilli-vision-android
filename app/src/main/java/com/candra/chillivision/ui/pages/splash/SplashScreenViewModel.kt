package com.candra.chillivision.ui.pages.splash

import androidx.lifecycle.ViewModel
import com.candra.chillivision.data.repository.ChilliVisionRepository

class SplashScreenViewModel(private val repository: ChilliVisionRepository) : ViewModel() {
    fun getUsageDate() = repository.getUsageDate()
    fun getPreferences() = repository.getPreferences()

    suspend fun setUsageDate(date: String) {
        repository.setUsageDate(date)
    }

    suspend fun setCountUsageAI(count: String) {
        repository.setCountUsageAI(count)
    }

    suspend fun setCountUsageDetect(count: String) {
        repository.setCountUsageDetect(count)
    }

    suspend fun setSubscriptionName(subscriptionName: String) {
        repository.setSubscriptionName(subscriptionName)
    }

    suspend fun setStartEndSubscriptionDate(startDate: String, endDate: String) {
        repository.setStartEndSubscriptionDate(startDate, endDate)
    }

    fun checkSubscriptionActive(idUser : String) = repository.checkSubscriptionActive(idUser = idUser)

    fun updateStatusSubscriptionUser(idSubscription: String, status : String) = repository.updateStatusSubscriptionUser(idSubscription = idSubscription, status = status)

}