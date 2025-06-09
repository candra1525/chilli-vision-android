package com.candra.chillivision.ui.pages.profile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.candra.chillivision.R
import com.candra.chillivision.component.AnimatedLoading
import com.candra.chillivision.component.InitialAvatar
import com.candra.chillivision.component.SweetAlertComponent
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.component.directToWhatsapp
import com.candra.chillivision.data.vmf.ViewModelFactory
import com.candra.chillivision.ui.theme.BlackMode
import com.candra.chillivision.ui.theme.PrimaryGreen
import com.candra.chillivision.ui.theme.WhiteSoft
import kotlinx.coroutines.runBlocking
import com.candra.chillivision.data.common.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ProfileScreenViewModel = viewModel(
        factory = ViewModelFactory.getInstance(
            LocalContext.current
        )
    )
) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    var isLoading by remember {
        mutableStateOf(false)
    }

    var fullname by remember {
        mutableStateOf("")
    }

    var subscriptionName by remember {
        mutableStateOf("")
    }

    val userPreferences by viewModel.getPreferences()
        .collectAsState(initial = null)

    when {
        userPreferences == null -> {
            // Jika data belum dimuat, jangan tampilkan apapun (menghindari flicker)
            return
        }

        userPreferences?.fullname.isNullOrEmpty() -> {
            return
        }

        userPreferences?.subscriptionName.isNullOrEmpty() -> {
            return
        }

        else -> {
            Log.d("Profile", "UserPreferences: $userPreferences")
            fullname = userPreferences?.fullname.toString()
            subscriptionName = userPreferences?.subscriptionName.toString()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                TextBold(
                    "Pengaturan", colors = PrimaryGreen, sized = 18
                )
            }, navigationIcon = {}, actions = {}, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = if (isSystemInDarkTheme()) BlackMode else WhiteSoft,
                scrolledContainerColor = if (isSystemInDarkTheme()) BlackMode else WhiteSoft
            )
            )
        }, containerColor = if (isSystemInDarkTheme()) BlackMode else WhiteSoft
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AnimatedLoading(modifier = Modifier.size(120.dp))
                        TextBold(
                            text = "Mohon menunggu...", sized = 14, textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 90.dp)
                        .padding(innerPadding) // ⬅️ penting
                        .verticalScroll(scrollState)
                ) {
                    SectionImageProfile(
                        viewModel = viewModel,
                        fullname = fullname,
                        subscriptionName = subscriptionName
                    )
                    TextBold(
                        text = "Pengaturan Akun",
                        sized = 14,
                        textAlign = TextAlign.Start,
                        colors = if (isSystemInDarkTheme()) WhiteSoft else BlackMode
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    MenuProfile(name = "Profil",
                        icon = R.drawable.profile_icon,
                        onClick = { navController.navigate("changeProfile") })
                    Spacer(modifier = Modifier.height(16.dp))
                    MenuProfile(name = "Kata Sandi",
                        icon = R.drawable.password_icon,
                        onClick = { navController.navigate("changePassword") })
                    Spacer(modifier = Modifier.height(16.dp))
                    MenuProfile(name = "Bantuan", icon = R.drawable.call_dev_icon, onClick = {
                        directToWhatsapp(
                            context,
                            "62895603231365",
                            "Halo, saya ${fullname} ingin bertanya tentang aplikasi Chilli Vision"
                        )
                    })
                    Spacer(modifier = Modifier.height(16.dp))
                    TextBold(
                        text = "Lainnya",
                        colors = if (isSystemInDarkTheme()) WhiteSoft else BlackMode,
                        sized = 14,
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    MenuProfile(name = "Tentang",
                        icon = R.drawable.about_apps_icon,
                        onClick = { navController.navigate("aboutApps") })
                    Spacer(modifier = Modifier.height(16.dp))
                    MenuProfile(name = "Ketentuan ",
                        icon = R.drawable.term,
                        onClick = { navController.navigate("terms") })
                    Spacer(modifier = Modifier.height(16.dp))
                    MenuProfile(name = "Privasi",
                        icon = R.drawable.privacy,
                        onClick = { navController.navigate("privacy") })
                    Spacer(modifier = Modifier.height(16.dp))
                    MenuProfile(
                        name = "Keluar",
                        icon = R.drawable.logout_icon,
                        onClick = {
                            SweetAlertComponent(
                                context = context,
                                title = "Keluar",
                                contentText = "Apakah anda yakin ingin keluar aplikasi?",
                                type = "logout",
                                isCancel = true,
                                confirmYes = {
                                    scope.launch(Dispatchers.IO) {
                                        withContext(Dispatchers.Main) {
                                            Log.d("Logout", "Preferences cleared")
                                            viewModel.setLogout()
                                                .observe(context as LifecycleOwner) { result ->
                                                    when (result) {
                                                        is Result.Loading -> {
                                                            Log.d("Logout", "Loading...")
                                                            isLoading = true
                                                        }

                                                        is Result.Success -> {
                                                            Log.d("Logout", "Logout success: ${result.data}")
                                                            scope.launch {
                                                                viewModel.clearPreferences()
                                                            }
                                                            isLoading = false

                                                            SweetAlertComponent(
                                                                context = context,
                                                                title = "Berhasil",
                                                                contentText = "Anda berhasil keluar aplikasi",
                                                                type = "success"
                                                            )

                                                            navController.navigate("welcome") {
                                                                popUpTo(navController.graph.startDestinationId) {
                                                                    inclusive = true
                                                                }
                                                            }
                                                        }

                                                        is Result.Error -> {
                                                            isLoading = false
                                                            SweetAlertComponent(
                                                                context = context,
                                                                title = "Gagal",
                                                                contentText = "Gagal keluar aplikasi",
                                                                type = "error"
                                                            )
                                                            Log.d("Logout", "Error: ${result.errorMessage}")
                                                        }
                                                    }
                                                }
                                        }
                                    }
                                }
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TextBold(
                            text = "Versi 2.0.2",
                            colors = if (isSystemInDarkTheme()) WhiteSoft else BlackMode,
                            sized = 12,
                            textAlign = TextAlign.Center
                        )
                        TextBold(
                            text = "Candra - Informatika - Universitas Multi Data Palembang",
                            colors = if (isSystemInDarkTheme()) WhiteSoft else BlackMode,
                            sized = 12,
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun SectionImageProfile(
    viewModel: ProfileScreenViewModel,
    fullname: String,
    subscriptionName: String
) {
    val context = LocalContext.current
    var image by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        if (image.isEmpty()) {
            viewModel.getPreferences().asLiveData().observe(context as LifecycleOwner) {
                if (it != null) {
                    image = it.image
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 8.dp, 0.dp, 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        InitialAvatar(fullname = fullname, imageUrl = image, size = 100.dp)
        Spacer(modifier = Modifier.height(16.dp))
        TextBold(text = fullname, colors = PrimaryGreen, sized = 20)
        Spacer(modifier = Modifier.height(4.dp))
        TextBold(
            text = subscriptionName,
            colors = if (isSystemInDarkTheme()) WhiteSoft else BlackMode,
            sized = 12
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}


@Composable
private fun MenuProfile(name: String, icon: Int, onClick: () -> Unit = {}) {

    Row(modifier = Modifier
        .fillMaxWidth()
        .background(color = if (isSystemInDarkTheme()) BlackMode else WhiteSoft)
        .clip(RoundedCornerShape(8.dp))
        .border(
            width = 1.dp, color = PrimaryGreen, shape = RoundedCornerShape(8.dp)
        )
        .clickable {
            onClick()
        }
        .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = "Logo ${R.string.app_name}",
            modifier = Modifier.size(35.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))
        TextBold(
            text = name,
            colors = if (isSystemInDarkTheme()) WhiteSoft else BlackMode,
            sized = 12,
            textAlign = TextAlign.Center
        )
    }
}