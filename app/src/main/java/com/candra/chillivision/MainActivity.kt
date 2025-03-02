package com.candra.chillivision

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.candra.chillivision.service.ClearCacheService
import com.candra.chillivision.ui.navigation.Navigation
import com.candra.chillivision.ui.theme.ChilliVisionTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mulai service untuk membersihkan cache saat aplikasi dihapus dari recent apps
        val serviceIntent = Intent(this, ClearCacheService::class.java)
        startService(serviceIntent)

        enableEdgeToEdge()
        setContent {
            ChilliVisionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navigation(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

}
