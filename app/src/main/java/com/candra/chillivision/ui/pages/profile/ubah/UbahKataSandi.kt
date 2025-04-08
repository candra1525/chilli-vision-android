package com.candra.chillivision.ui.pages.profile.ubah

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.candra.chillivision.R
import com.candra.chillivision.component.SweetAlertComponent
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.data.common.Result
import com.candra.chillivision.data.vmf.ViewModelFactory
import com.candra.chillivision.ui.theme.BlackMode
import com.candra.chillivision.ui.theme.PrimaryGreen
import com.candra.chillivision.ui.theme.WhiteSoft

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UbahKataSandi(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: UbahKataSandiViewModel = viewModel(
        factory = ViewModelFactory.getInstance(
            LocalContext.current
        )
    )
) {
    val isDarkTheme = isSystemInDarkTheme()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TextBold(
                        "Kata Sandi",
                        colors = PrimaryGreen,
                        sized = 18
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = PrimaryGreen
                        )
                    }
                },

                modifier = Modifier.shadow(1.dp), // Shadow manual
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isSystemInDarkTheme()) BlackMode else WhiteSoft,
                    scrolledContainerColor = if (isSystemInDarkTheme()) BlackMode else WhiteSoft
                )
            )
        },
        containerColor = if (isSystemInDarkTheme()) BlackMode else WhiteSoft
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            ContentUbahKataSandi(modifier = modifier, viewModel = viewModel, context = context)
        }
    }

}


@Composable
private fun ContentUbahKataSandi(
    modifier: Modifier = Modifier,
    viewModel: UbahKataSandiViewModel,
    context: Context
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.password_icon),
            contentDescription = "back header ${R.string.app_name}",
            modifier = Modifier
                .size(60.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextBold(
            text = "Pastikan semua data terisi dengan baik dan benar.",
            colors = if (isSystemInDarkTheme()) WhiteSoft else BlackMode,
            modifier = Modifier.padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))
        FormUbahKataSandi(modifier = modifier, viewModel = viewModel, context = context)
    }
}


@Composable
private fun FormUbahKataSandi(
    modifier: Modifier = Modifier,
    viewModel: UbahKataSandiViewModel,
    context: Context
) {
    val lifeCycleOwner = LocalLifecycleOwner.current

    var idUser by remember {
        mutableStateOf("")
    }
    var textOldPassword by remember {
        mutableStateOf("")
    }
    var textPassword by remember {
        mutableStateOf("")
    }

    var textConfirmPassword by remember {
        mutableStateOf("")
    }

    viewModel.getPreferences().asLiveData().observe(lifeCycleOwner) {
        if (idUser.isEmpty()) {
            idUser = it?.id.toString()
        }
    }

    var isLoading by remember {
        mutableStateOf(false)
    }


    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {

        Text(
            text = "Kata Sandi Lama *",
            modifier = Modifier,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 12.sp,
                color = PrimaryGreen,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.quicksand_bold))
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = textOldPassword,
            onValueChange = {
                textOldPassword = it
            },
            placeholder = {
                Text(
                    text = "Masukkan Kata Sandi Anda",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Normal,
                        fontSize = 11.sp,
                        fontFamily = FontFamily(Font(R.font.quicksand_medium))
                    )
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.quicksand_medium))
            ),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Kata Sandi Baru *",
            modifier = Modifier,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 12.sp,
                color = PrimaryGreen,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.quicksand_bold))
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = textPassword,
            onValueChange = {
                textPassword = it
            },
            placeholder = {
                Text(
                    text = "Masukkan Kata Sandi Baru Anda",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Normal,
                        fontSize = 11.sp,
                        fontFamily = FontFamily(Font(R.font.quicksand_medium))
                    )
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.quicksand_medium))
            ),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Konfirmasi Kata Sandi Baru *",
            modifier = Modifier,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 12.sp,
                color = PrimaryGreen,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.quicksand_bold))
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = textConfirmPassword,
            onValueChange = {
                textConfirmPassword = it
            },
            placeholder = {
                Text(
                    text = "Masukkan Konfirmasi Kata Sandi Baru Anda",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Normal,
                        fontSize = 11.sp,
                        fontFamily = FontFamily(Font(R.font.quicksand_medium))
                    )
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.quicksand_medium))
            ),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                validationChangePassword(
                    viewModel = viewModel,
                    idUser = idUser,
                    oldPassword = textOldPassword,
                    password = textPassword,
                    confirmPassword = textConfirmPassword,
                    context = context,
                    onLoadingStateChange = { loading ->
                        isLoading = loading
                    },
                    onSuccess = {
                        // Reset semua input setelah berhasil
                        textOldPassword = ""
                        textPassword = ""
                        textConfirmPassword = ""
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
            enabled = !isLoading
        ) {
            Text(
                text = if (isLoading) "Memuat..." else "Simpan",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private fun validationChangePassword(
    viewModel: UbahKataSandiViewModel,
    idUser: String,
    oldPassword: String,
    password: String,
    confirmPassword: String,
    context: Context,
    onLoadingStateChange: (Boolean) -> Unit,
    onSuccess: () -> Unit
) {
    if (oldPassword.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
        SweetAlertComponent(
            context = context,
            title = "Gagal",
            contentText = "Mohon maaf, data tidak boleh kosong",
            type = "error"
        )
    } else if (password.length < 8 || confirmPassword.length < 8) {
        SweetAlertComponent(
            context = context,
            title = "Gagal",
            contentText = "Mohon maaf, kata sandi minimal 8 karakter ya",
            type = "error"
        )
    } else if (password != confirmPassword) {
        SweetAlertComponent(
            context = context,
            title = "Gagal",
            contentText = "Mohon maaf, kata sandi dan konfirmasi kata sandi tidak sama",
            type = "error"
        )
    } else {
        UbahKataSandi(
            viewModel = viewModel,
            idUser = idUser,
            oldPassword = oldPassword,
            password = password,
            lifecycleOwner = context as LifecycleOwner,
            onLoadingStateChange = onLoadingStateChange,
            onSuccess = onSuccess // Panggil reset field setelah sukses
        )
    }

}


private fun UbahKataSandi(
    viewModel: UbahKataSandiViewModel,
    idUser: String,
    oldPassword: String,
    password: String,
    lifecycleOwner: LifecycleOwner,
    onLoadingStateChange: (Boolean) -> Unit,
    onSuccess: () -> Unit // Tambahkan callback sukses
) {
    Log.d("Ubah Password", "idUser: $idUser, oldPassword: $oldPassword, password: $password")
    viewModel.updatePasswordUser(oldPassword, password, idUser).observe(lifecycleOwner) { result ->
        if (result != null) {
            when (result) {
                is Result.Loading -> {
                    onLoadingStateChange(true)
                }

                is Result.Success -> {
                    onLoadingStateChange(false)
                    SweetAlertComponent(
                        context = lifecycleOwner as Context,
                        title = "Berhasil",
                        contentText = "Kata sandi anda berhasil diubah, silahkan gunakan kata sandi terbaru saat masuk aplikasi lagi ya 😉",
                        type = "success"
                    )
                    onSuccess()
                }

                is Result.Error -> {
                    onLoadingStateChange(false)
                    Log.e("Ubah Profile", "login: ${result}")
                    SweetAlertComponent(
                        context = lifecycleOwner as Context,
                        title = "Gagal",
                        contentText = "Mohon maaf anda gagal masuk, silahkan pastikan no handphone dan password benar",
                        type = "error"
                    )
                }

                else -> {
                    onLoadingStateChange(false)
                    Log.e("Ubah Password", "error: ${result}")
                    SweetAlertComponent(
                        lifecycleOwner as Context,
                        "Kesalahan",
                        "Mohon maaf sepertinya ada kesalahan sistem. Mohon ulangi beberapa saat lagi...",
                        "error"
                    )

                }
            }
        }
    }
}