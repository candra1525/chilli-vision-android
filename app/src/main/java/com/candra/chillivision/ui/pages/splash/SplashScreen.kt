package com.candra.chillivision.ui.pages.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import com.candra.chillivision.data.vmf.ViewModelFactory
import com.candra.chillivision.ui.navigation.NavigationViewModel
import com.candra.chillivision.ui.theme.BlackMode
import com.candra.chillivision.ui.theme.PrimaryGreen
import com.candra.chillivision.ui.theme.White
import com.candra.chillivision.ui.theme.WhiteSoft
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashScreen : ComponentActivity() {
    private val viewModel by viewModels<NavigationViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = resources.getColor(R.color.black, theme)
        }

        setContent {
            SplashScreenContent()
        }

        lifecycleScope.launch {
            delay(SPLASH_SCREEN_DURATION)
            nextScreen(this@SplashScreen)
        }
    }

    @Composable
    fun SplashScreenContent(modifier: Modifier = Modifier) {
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
            )
            {
                Text(
                    text = "Chilli Vision", style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.quicksand_semibold)),
                        color = if(isSystemInDarkTheme()) WhiteSoft else BlackMode, textAlign = TextAlign.Center
                    ), modifier = modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Candra - Informatics", style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                        fontWeight = FontWeight.Bold,
                        color =  PrimaryGreen,
                        textAlign = TextAlign.Center
                    ), modifier = modifier.fillMaxWidth()
                )
            }
        }
    }

    private fun nextScreen(originActivity: ComponentActivity) {
//        runBlocking {
//        viewModel.getSessionToken().observe(originActivity){}
//            Toast.makeText(originActivity, "Token : ${viewModel.getSessionToken().value?.token}", Toast.LENGTH_SHORT).show()
//        }

        val intent = Intent(originActivity, MainActivity::class.java)
        startActivity(intent)
        originActivity.finish()
    }

    companion object {
        private const val SPLASH_SCREEN_DURATION = 3000L
    }

}