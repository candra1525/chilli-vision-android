package com.candra.chillivision.ui.pages.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.candra.chillivision.MainActivity
import com.candra.chillivision.R
import com.candra.chillivision.data.common.Result
import com.candra.chillivision.data.vmf.ViewModelFactory
import com.candra.chillivision.ui.theme.BlackMode
import com.candra.chillivision.ui.theme.PrimaryGreen
import com.candra.chillivision.ui.theme.WhiteSoft
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@SuppressLint("CustomSplashScreen")
class SplashScreen : ComponentActivity() {
    private val splashScreenViewModel by viewModels<SplashScreenViewModel> {
        ViewModelFactory.getInstance(this@SplashScreen)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = resources.getColor(R.color.black, theme)
        }

        setContent {
            SplashScreenContent(viewModel = splashScreenViewModel)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    fun SplashScreenContent(modifier: Modifier = Modifier, viewModel: SplashScreenViewModel) {
        val userPreferences by viewModel.getPreferences()
            .collectAsState(initial = null)

        when {
            userPreferences == null -> {
                // Jika data belum dimuat, jangan menampilkan apapun
                return
            }

            userPreferences?.id.isNullOrEmpty() -> {
                // Jika id kosong
                return
            }

            else -> {
                Log.d("Splash", "UserPreferences: $userPreferences")

                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .background(if (isSystemInDarkTheme()) BlackMode else WhiteSoft)
                ) {
                    // Image
                    Image(
                        painter = painterResource(id = R.drawable.chilli_vision_logo),
                        contentDescription = "Logo ${R.string.app_name}",
                        modifier = modifier
                            .size(150.dp)
                            .align(Alignment.Center)
                    )

                    Column(
                        modifier = modifier
                            .height(200.dp)
                            .align(Alignment.BottomCenter)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Chilli Vision",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.quicksand_semibold)),
                                color = if (isSystemInDarkTheme()) WhiteSoft else BlackMode,
                                textAlign = TextAlign.Center
                            ),
                            modifier = modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Candra - Informatics",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                                fontWeight = FontWeight.Bold,
                                color = PrimaryGreen,
                                textAlign = TextAlign.Center
                            ),
                            modifier = modifier.fillMaxWidth()
                        )
                    }
                }

                viewModel.checkSubscriptionActive(userPreferences?.id.orEmpty())
                    .observe(this) { result ->
                        when (result) {
                            is Result.Loading -> {
                                // Tampilkan loading jika perlu
                            }

                            is Result.Success -> {
                                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
                                val today = LocalDate.now()

                                val data = result.data.data

                                if (data != null) {
                                    val endDate = data.endDate?.let { LocalDate.parse(it, formatter) }

                                    if (endDate != null) {
                                        if (endDate >= today) {
                                            // Belum expired
                                            Log.d("Splash", "Belum Exp")
                                            lifecycleScope.launch {
                                                viewModel.setSubscriptionName(data.subscriptions?.title ?: "Gratis")
                                                nextScreen(this@SplashScreen, viewModel = viewModel)
                                            }
                                        } else {
                                            // Sudah expired
                                            Log.d("Splash", "Exp")
                                            viewModel.updateStatusSubscriptionUser(data.id.orEmpty(), "expired")
                                                .observeForever { updateResult ->
                                                    when (updateResult) {
                                                        is Result.Success, is Result.Error -> {
                                                            Log.d("Splash", "Update Status Expired Done/Error")
                                                            lifecycleScope.launch {
                                                                viewModel.setSubscriptionName("Gratis")
                                                                nextScreen(this@SplashScreen, viewModel = viewModel)
                                                            }
                                                        }
                                                        else -> {}
                                                    }
                                                }
                                        }
                                    }
                                } else {
                                    // Tidak ada data langganan
                                    Log.d("Splash", "No Langganan")
                                    lifecycleScope.launch {
                                        viewModel.setSubscriptionName("Gratis")
                                        nextScreen(this@SplashScreen, viewModel = viewModel)
                                    }
                                }
                            }

                            is Result.Error -> {
                                // Optional: tampilkan error atau lanjutkan sebagai "Gratis"
                                Log.d("Splash", "Error saat cek subscription")
                                lifecycleScope.launch {
                                    viewModel.setSubscriptionName("Gratis")
                                    nextScreen(this@SplashScreen, viewModel = viewModel)
                                }
                            }
                        }
                    }
            }
        }
    }

    private fun nextScreen(originActivity: ComponentActivity, viewModel: SplashScreenViewModel) {
        lifecycleScope.launch {
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val datePref = viewModel.getUsageDate().firstOrNull()

            if (datePref == null || datePref == "") {
                viewModel.setUsageDate(date)
            }

            if (datePref != date) {
                lifecycleScope.launch {
                    viewModel.setCountUsageAI("0")
                    viewModel.setCountUsageDetect("0")
                    viewModel.setUsageDate(date)
                }
            }
        }
        val intent = Intent(originActivity, MainActivity::class.java)
        startActivity(intent)
        originActivity.finish()
    }
}