package com.candra.chillivision.ui.pages.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.candra.chillivision.component.InitialAvatar
import com.candra.chillivision.component.SweetAlertComponent
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.component.directToWhatsapp
import com.candra.chillivision.data.vmf.ViewModelFactory
import com.candra.chillivision.ui.theme.BlackMode
import com.candra.chillivision.ui.theme.PrimaryGreen
import com.candra.chillivision.ui.theme.WhiteSoft
import kotlinx.coroutines.runBlocking

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
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    var fullname by remember {
        mutableStateOf("")
    }

    viewModel.getPreferences().asLiveData().observe(context as LifecycleOwner) {
        if (it != null) {
            fullname = it.fullname
        } else {
            fullname = "Pengguna Chilli Vision"
        }
    }

    Column(
        modifier = modifier
            .padding(start = 32.dp, end = 32.dp, top = 32.dp, bottom = 90.dp)
            .verticalScroll(scrollState),
    ) {
        TitleProfile()
        Spacer(modifier = Modifier.height(32.dp))
        SectionImageProfile(viewModel = viewModel, fullname = fullname)
        TextBold(
            text = "Pengaturan Akun",
            sized = 12,
            textAlign = TextAlign.Start,
            colors = if (isSystemInDarkTheme()) WhiteSoft else BlackMode
        )
        Spacer(modifier = Modifier.height(16.dp))
        MenuProfile(
            name = "Ubah Profile",
            icon = R.drawable.profile_icon,
            onClick = { navController.navigate("changeProfile") })
        Spacer(modifier = Modifier.height(16.dp))
        MenuProfile(
            name = "Ubah Kata Sandi",
            icon = R.drawable.password_icon,
            onClick = { navController.navigate("changePassword") })

        Spacer(modifier = Modifier.height(32.dp))
        TextBold(
            text = "Bantuan",
            colors = if (isSystemInDarkTheme()) WhiteSoft else BlackMode,
            sized = 12,
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.height(16.dp))
        MenuProfile(
            name = "Hubungi Pengembang Aplikasi",
            icon = R.drawable.call_dev_icon,
            onClick = {
                directToWhatsapp(
                    context,
                    "62895603231365",
                    "Halo, saya ${fullname} ingin bertanya tentang aplikasi Chilli Vision"
                )
            })

        Spacer(modifier = Modifier.height(32.dp))
        TextBold(
            text = "Lainnya",
            colors = if (isSystemInDarkTheme()) WhiteSoft else BlackMode,
            sized = 12,
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.height(16.dp))
        MenuProfile(
            name = "Tentang Aplikasi",
            icon = R.drawable.about_apps_icon,
            onClick = { navController.navigate("aboutApps") })
        Spacer(modifier = Modifier.height(16.dp))
        MenuProfile(name = "Keluar Aplikasi", icon = R.drawable.logout_icon, onClick = {
            SweetAlertComponent(
                context = context,
                title = "Keluar Aplikasi",
                contentText = "Apakah anda yakin ingin keluar aplikasi?",
                type = "logout",
                isCancel = true,
                confirmYes = {
                    SweetAlertComponent(
                        context = context,
                        title = "Berhasil",
                        contentText = "Anda berhasil keluar aplikasi",
                        type = "success"
                    )

                    viewModel.setLogout().observe(context as LifecycleOwner) {
                        navController.navigate("welcome") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    }
                    runBlocking {
                        viewModel.clearToken()
                    }
                }
            )
        })
    }
}

@Composable
private fun TitleProfile() {
    TextBold(text = "Pengaturan", colors = PrimaryGreen, sized = 18)
}

@Composable
private fun SectionImageProfile(viewModel: ProfileScreenViewModel, fullname: String) {
    val context = LocalContext.current
    var image by remember {
        mutableStateOf("")
    }

    runBlocking {
        viewModel.getPreferences().asLiveData().observe(context as LifecycleOwner) {
            if (it != null) {
                image = it.image
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        InitialAvatar(fullname = fullname, imageUrl = image, size = 100.dp)
        Spacer(modifier = Modifier.height(16.dp))
        TextBold(text = fullname, colors = PrimaryGreen, sized = 20)
        Spacer(modifier = Modifier.height(4.dp))
        TextBold(
            text = "Paket Gratis",
            colors = if (isSystemInDarkTheme()) WhiteSoft else BlackMode,
            sized = 12
        )
        Spacer(modifier = Modifier.height(32.dp))


    }
}


@Composable
private fun MenuProfile(name: String, icon: Int, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
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
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,

        ) {
        TextBold(
            text = name,
            colors = if (isSystemInDarkTheme()) WhiteSoft else BlackMode,
            sized = 12,
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.height(4.dp))
        Image(
            painter = painterResource(id = icon),
            contentDescription = "Logo ${R.string.app_name}",
            modifier = Modifier.size(24.dp)
        )
    }
}