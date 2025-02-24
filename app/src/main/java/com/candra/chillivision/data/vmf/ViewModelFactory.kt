package com.candra.chillivision.data.vmf

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.candra.chillivision.data.di.Injection
import com.candra.chillivision.data.repository.ChilliVisionRepository
import com.candra.chillivision.ui.pages.home.HomeScreenViewModel
import com.candra.chillivision.ui.pages.langganan.LanggananScreenViewModel
import com.candra.chillivision.ui.pages.login.LoginScreenViewModel
import com.candra.chillivision.ui.pages.profile.ProfileScreenViewModel
import com.candra.chillivision.ui.pages.profile.ubah.UbahKataSandiViewModel
import com.candra.chillivision.ui.pages.profile.ubah.UbahProfileViewModel
import com.candra.chillivision.ui.pages.register.RegisterScreenViewModel

class ViewModelFactory(private val repository: ChilliVisionRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeScreenViewModel::class.java) -> HomeScreenViewModel(
                repository
            ) as T

            modelClass.isAssignableFrom(LoginScreenViewModel::class.java) -> LoginScreenViewModel(
                repository
            ) as T

            modelClass.isAssignableFrom(RegisterScreenViewModel::class.java) -> RegisterScreenViewModel(
                repository
            ) as T

            modelClass.isAssignableFrom(ProfileScreenViewModel::class.java) -> ProfileScreenViewModel(
                repository
            ) as T

//            modelClass.isAssignableFrom(GalleryScreenViewModel::class.java) -> GalleryScreenViewModel(
//                repository
//            ) as T

            modelClass.isAssignableFrom(UbahProfileViewModel::class.java) -> UbahProfileViewModel(
                repository
            ) as T

            modelClass.isAssignableFrom(UbahKataSandiViewModel::class.java) -> UbahKataSandiViewModel(
                repository
            ) as T

            // langganan
            modelClass.isAssignableFrom(LanggananScreenViewModel::class.java) -> LanggananScreenViewModel(
                repository
            ) as T

            else -> throw IllegalArgumentException("ViewModel Not Found" + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

}