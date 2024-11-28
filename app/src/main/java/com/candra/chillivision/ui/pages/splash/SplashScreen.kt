package com.candra.chillivision.ui.pages.splash

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.candra.chillivision.data.vmf.ViewModelFactory
import com.candra.chillivision.ui.navigation.NavigationViewModel

@SuppressLint("CustomSplashScreen")
class SplashScreen : ComponentActivity()
{
    private val viewModel by viewModels<NavigationViewModel>{
        ViewModelFactory.getInstance(this)
    }

}