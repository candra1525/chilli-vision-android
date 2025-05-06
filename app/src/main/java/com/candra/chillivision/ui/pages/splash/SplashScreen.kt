package com.candra.chillivision.ui.pages.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@SuppressLint("CustomSplashScreen")
class SplashScreen : ComponentActivity() {

    private val splashScreenViewModel by viewModels<SplashScreenViewModel> {
        ViewModelFactory.getInstance(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Status bar styling
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = resources.getColor(R.color.black, theme)
        }

        setContent {
            SplashScreenContent(viewModel = splashScreenViewModel)
        }

        lifecycleScope.launch {
            initializeApp()
            checkSubscription()
        }
    }

    private suspend fun initializeApp() {
        val dateNow = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val datePref = splashScreenViewModel.getUsageDate().firstOrNull()

        if (datePref.isNullOrEmpty() || datePref != dateNow) {
            splashScreenViewModel.setCountUsageAI("0")
            splashScreenViewModel.setCountUsageDetect("0")
            splashScreenViewModel.setUsageDate(dateNow)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkSubscription() {
        lifecycleScope.launch {
            val preferences = splashScreenViewModel.getPreferences().firstOrNull()
            val userId = preferences?.id.orEmpty()
            if (userId.isEmpty()) {
                goToMain()
                return@launch
            }

            splashScreenViewModel.checkSubscriptionActive(userId)
                .observe(this@SplashScreen) { result ->
                    when (result) {
                        is Result.Success -> {
                            val data = result.data.data
                            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            val today = formatter.parse(formatter.format(Date()))

                            val endDate = data?.endDate?.let {
                                try {
                                    formatter.parse(it)
                                } catch (e: Exception) {
                                    null
                                }
                            }

                            val startDate = data?.startDate?.let {
                                try {
                                    formatter.parse(it)
                                } catch (e: Exception) {
                                    null
                                }
                            }

                            if (endDate != null && endDate >= today) {
                                lifecycleScope.launch {
                                    splashScreenViewModel.setSubscriptionName(data.subscriptions?.title ?: "Gratis")
                                    splashScreenViewModel.setStartEndSubscriptionDate(
                                        startDate = startDate.toString(),
                                        endDate = endDate.toString()
                                    )
                                    goToMain()
                                }
                            } else {
                                lifecycleScope.launch {
                                    data?.id?.let {
                                        splashScreenViewModel.updateStatusSubscriptionUser(it, "expired").observe(this@SplashScreen) {}
                                    }
                                    splashScreenViewModel.setSubscriptionName("Gratis")
                                    splashScreenViewModel.setStartEndSubscriptionDate("", "")
                                    goToMain()
                                }
                            }
                        }

                        is Result.Error -> {
                            lifecycleScope.launch {
                                splashScreenViewModel.setSubscriptionName("Gratis")
                                splashScreenViewModel.setStartEndSubscriptionDate("", "")
                                goToMain()
                            }
                        }

                        else -> Unit
                    }
                }
        }
    }

    private fun goToMain() {
        startActivity(Intent(this@SplashScreen, MainActivity::class.java))
    }

    @Composable
    fun SplashScreenContent(modifier: Modifier = Modifier, viewModel: SplashScreenViewModel) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(if (isSystemInDarkTheme()) BlackMode else WhiteSoft)
        ) {
            Image(
                painter = painterResource(id = R.drawable.chilli_vision_logo),
                contentDescription = stringResource(id = R.string.app_name),
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
                    text = "Candra - Informatika",
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
    }
}