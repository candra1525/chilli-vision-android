package com.candra.chillivision.data.vmf

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.candra.chillivision.data.di.Injection
import com.candra.chillivision.data.repository.ChilliVisionRepository
import com.candra.chillivision.ui.pages.history.HistoryScreenViewModel
import com.candra.chillivision.ui.pages.history.detail_history.DetailHistoryScreenViewModel
import com.candra.chillivision.ui.pages.home.HomeScreenViewModel
import com.candra.chillivision.ui.pages.home.notification.NotificationScreenViewModel
import com.candra.chillivision.ui.pages.home.tanyaAI.ChilliAIScreenViewModel
import com.candra.chillivision.ui.pages.langganan.LanggananScreenViewModel
import com.candra.chillivision.ui.pages.langganan.detail.DetailLanggananScreenViewModel
import com.candra.chillivision.ui.pages.langganan.detail_active.DetailActiveLanggananScreenViewModel
import com.candra.chillivision.ui.pages.langganan.detail_history.DetailHistoryLanggananScreenViewModel
import com.candra.chillivision.ui.pages.login.LoginScreenViewModel
import com.candra.chillivision.ui.pages.profile.ProfileScreenViewModel
import com.candra.chillivision.ui.pages.profile.ubah.UbahKataSandiViewModel
import com.candra.chillivision.ui.pages.profile.ubah.UbahProfileViewModel
import com.candra.chillivision.ui.pages.register.RegisterScreenViewModel
import com.candra.chillivision.ui.pages.scan.analysis_result.AnalysisResultScreenViewModel
import com.candra.chillivision.ui.pages.scan.ScanScreenViewModel
import com.candra.chillivision.ui.pages.scan.confirm_scan.ConfirmScanScreenViewModel
import java.util.concurrent.ConcurrentHashMap

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

            // detail langganan
            modelClass.isAssignableFrom(DetailLanggananScreenViewModel::class.java) -> DetailLanggananScreenViewModel(
                repository
            ) as T

            // langganan
            modelClass.isAssignableFrom(HistoryScreenViewModel::class.java) -> HistoryScreenViewModel(
                repository
            ) as T

            // Detail History
            modelClass.isAssignableFrom(DetailHistoryScreenViewModel::class.java) -> DetailHistoryScreenViewModel(
                repository
            ) as T

            // Detail Active Langganan
            modelClass.isAssignableFrom(DetailActiveLanggananScreenViewModel::class.java) -> DetailActiveLanggananScreenViewModel(
                repository
            ) as T

            // Detail History Langganan
            modelClass.isAssignableFrom(DetailHistoryLanggananScreenViewModel::class.java) -> DetailHistoryLanggananScreenViewModel(
                repository
            ) as T

            // Notification
            modelClass.isAssignableFrom(NotificationScreenViewModel::class.java) -> NotificationScreenViewModel(
                repository
            ) as T

            // Analysis
            modelClass.isAssignableFrom(AnalysisResultScreenViewModel::class.java) -> AnalysisResultScreenViewModel(
                repository
            ) as T

            modelClass.isAssignableFrom(ChilliAIScreenViewModel::class.java) -> ChilliAIScreenViewModel(
                repository
            ) as T

            modelClass.isAssignableFrom(ScanScreenViewModel::class.java) -> ScanScreenViewModel(
                repository
            ) as T

            modelClass.isAssignableFrom(ConfirmScanScreenViewModel::class.java) -> ConfirmScanScreenViewModel(
                repository
            ) as T

            else -> throw IllegalArgumentException("ViewModel Not Found" + modelClass.name)
        }
    }

    companion object {
//        @Volatile
//        private var INSTANCE: ViewModelFactory? = null
//
//        @JvmStatic
//        fun getInstance(context: Context, apiType: String = "default"): ViewModelFactory {
//            if (INSTANCE == null) {
//                synchronized(ViewModelFactory::class.java) {
//                    INSTANCE = ViewModelFactory(Injection.provideRepository(context, apiType))
//                }
//            }
//            return INSTANCE as ViewModelFactory
//        }

        private val instances = ConcurrentHashMap<String, ViewModelFactory>()

        @JvmStatic
        fun getInstance(context: Context, apiType: String = "default"): ViewModelFactory {
            return instances.getOrPut(apiType) {
                ViewModelFactory(Injection.provideRepository(context, apiType))
            }
        }
    }

}