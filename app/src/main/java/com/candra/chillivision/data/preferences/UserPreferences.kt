package com.candra.chillivision.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.candra.chillivision.data.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    val tokenFlow: Flow<String> = dataStore.data.map { pref ->
        pref[TOKEN_KEY] ?: ""
    }.distinctUntilChanged()

    suspend fun savePreferences(
        token: String,
        id: String,
        fullname: String,
        noHandphone: String,
        image: String,
        subscriptionName: String
    ) {
        dataStore.edit { pref ->
            pref[TOKEN_KEY] = token
            pref[ID_KEY] = id
            pref[FULLNAME_KEY] = fullname
            pref[NOHANDPHONE_KEY] = noHandphone
            pref[IMAGE_KEY] = image
            pref[SUBSCRIPTION_NAME_KEY] = subscriptionName
        }
    }

    suspend fun setStartEndSubscriptionDate(startDate: String, endDate: String) {
        dataStore.edit { pref ->
            pref[START_SUBSCRIPTION_DATE] = startDate
            pref[END_SUBSCRIPTION_DATE] = endDate
        }
    }

    suspend fun setSubscriptionName(subscriptionName: String) {
        dataStore.edit { pref ->
            pref[SUBSCRIPTION_NAME_KEY] = subscriptionName
        }
    }


    suspend fun setUsageDate(date: String) {
        dataStore.edit { pref ->
            pref[USAGE_DATE] = date
        }
    }

    suspend fun setCountUsageAI(count: String) {
        dataStore.edit { pref ->
            pref[COUNT_USAGE_AI] = count
        }
    }

    suspend fun setCountUsageDetect(count: String) {
        dataStore.edit { pref ->
            pref[COUNT_USAGE_DETECT] = count
        }
    }

    fun getPreferences(): Flow<UserModel> {
        return dataStore.data.map { pref ->
            UserModel(
                pref[TOKEN_KEY] ?: "",
                pref[ID_KEY] ?: "",
                pref[FULLNAME_KEY] ?: "",
                pref[NOHANDPHONE_KEY] ?: "",
                pref[IMAGE_KEY] ?: "",
                pref[SUBSCRIPTION_NAME_KEY] ?: "",
                pref[START_SUBSCRIPTION_DATE] ?: "",
                pref[END_SUBSCRIPTION_DATE] ?: "",
                pref[COUNT_USAGE_AI] ?: "0",
                pref[COUNT_USAGE_DETECT] ?: "0",

            )
        }
    }

    fun getCountUsageAI(): Flow<String> {
        return dataStore.data.map { pref ->
            pref[COUNT_USAGE_AI] ?: "0"
        }
    }

    fun getCountUsageDetect(): Flow<String> {
        return dataStore.data.map { pref ->
            pref[COUNT_USAGE_DETECT] ?: "0"
        }
    }

    fun getUsageDate(): Flow<String> {
        return dataStore.data.map { pref ->
            pref[USAGE_DATE] ?: ""
        }
    }

    fun getStartDateSubscription(): Flow<String> {
        return dataStore.data.map { pref ->
            pref[START_SUBSCRIPTION_DATE] ?: ""
        }
    }

    fun getEndDateSubscription(): Flow<String> {
        return dataStore.data.map { pref ->
            pref[END_SUBSCRIPTION_DATE] ?: ""
        }
    }

    suspend fun clearPreferences() {
        dataStore.edit { pref ->
            pref[TOKEN_KEY] = ""
            pref[ID_KEY] = ""
            pref[FULLNAME_KEY] = ""
            pref[NOHANDPHONE_KEY] = ""
            pref[IMAGE_KEY] = ""
            pref[SUBSCRIPTION_NAME_KEY] = ""

            pref[START_SUBSCRIPTION_DATE] = ""
            pref[END_SUBSCRIPTION_DATE] = ""
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val ID_KEY = stringPreferencesKey("id")
        private val FULLNAME_KEY = stringPreferencesKey("fullname")
        private val NOHANDPHONE_KEY = stringPreferencesKey("noHandphone")
        private val IMAGE_KEY = stringPreferencesKey("image")
        private val SUBSCRIPTION_NAME_KEY = stringPreferencesKey("subscription_name")

        private val USAGE_DATE = stringPreferencesKey("usage_date")
        private val COUNT_USAGE_AI = stringPreferencesKey("count_usage_ai")
        private val COUNT_USAGE_DETECT = stringPreferencesKey("count_usage_detect")

        private val START_SUBSCRIPTION_DATE = stringPreferencesKey("start_subscription_date")
        private val END_SUBSCRIPTION_DATE = stringPreferencesKey("end_subscription_date")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}