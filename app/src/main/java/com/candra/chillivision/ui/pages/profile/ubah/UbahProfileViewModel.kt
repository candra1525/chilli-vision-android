package com.candra.chillivision.ui.pages.profile.ubah

import androidx.lifecycle.ViewModel
import com.candra.chillivision.data.repository.ChilliVisionRepository
import okhttp3.MultipartBody

class UbahProfileViewModel(private val repository: ChilliVisionRepository) : ViewModel() {
    fun updateAccountUser(fullname: String, no_handphone: String, id: String) =
        repository.setUpdateAccountUser(fullname = fullname, no_handphone = no_handphone, id = id)

    fun updatePhotoAccountUser(image: MultipartBody.Part, id: String) =
        repository.setUpdatePhotoAccountUser(image = image, id = id)

    fun deletePhotoProfile(id: String) = repository.setDeletePhotoProfile(id)

    fun getPreferences() = repository.getPreferences()

    suspend fun savePreferences(
        token: String,
        id: String,
        fullname: String,
        no_handphone: String,
        image: String,
        subscriptionName : String
    ) = repository.savePreferences(token, id, fullname, no_handphone, image, subscriptionName)
}