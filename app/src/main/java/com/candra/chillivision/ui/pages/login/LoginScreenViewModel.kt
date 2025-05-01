package com.candra.chillivision.ui.pages.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.candra.chillivision.data.repository.ChilliVisionRepository
import kotlinx.coroutines.launch

class LoginScreenViewModel(private val repository: ChilliVisionRepository) : ViewModel() {
    fun setLogin(noHandphone: String, password: String) = repository.setLogin(noHandphone, password)

    fun savePreferences(
        token: String,
        id: String,
        fullname: String,
        noHandphone: String,
        image: String,
        subscriptionName : String,
        onSaved : () -> Unit = {}
    ) {
        viewModelScope.launch {
            repository.savePreferences(token, id, fullname, noHandphone, image, subscriptionName)
            onSaved() // callback setelah save selesai
        }
    }

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

    fun checkSubscriptionActive(idUser : String) = repository.checkSubscriptionActive(idUser = idUser)

    fun updateStatusSubscriptionUser(idSubscription: String, status : String) = repository.updateStatusSubscriptionUser(idSubscription = idSubscription, status = status)
}
