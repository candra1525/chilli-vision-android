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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import androidx.lifecycle.compose.LocalLifecycleOwner
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
import kotlinx.coroutines.runBlocking

@Composable
fun UbahProfile(
    modifier: Modifier = Modifier, navController: NavController,
    viewModel: UbahProfileViewModel = viewModel(
        factory = ViewModelFactory.getInstance(
            LocalContext.current
        )
    )
) {
    val context = LocalContext.current
    val isDarkTheme = isSystemInDarkTheme()
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(start = 32.dp, end = 32.dp, bottom = 90.dp),
    ) {
        HeaderUbahProfile(modifier, navController)
        
        ContentUbahProfile(
            modifier = modifier,
            viewModel = viewModel,
            context = context
        )

    }
}

@Composable
private fun HeaderUbahProfile(modifier: Modifier = Modifier, navController: NavController) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.back_arrow),
            contentDescription = "back header ${R.string.app_name}",
            modifier = Modifier
                .size(24.dp)
                .clickable { navController.popBackStack() },
        )

        Text(
            text = "Ubah Profil", style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                color = PrimaryGreen,
                textAlign = TextAlign.Center
            ), modifier = Modifier
                .fillMaxWidth()
        )
    }
}


@Composable
private fun ContentUbahProfile(
    modifier: Modifier = Modifier,
    viewModel: UbahProfileViewModel,
    context: Context
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.profile2),
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
        FormUbahProfile(
            modifier = modifier,
            viewModel = viewModel,
            context = context
        )
    }
}


@Composable
private fun FormUbahProfile(
    modifier: Modifier = Modifier,
    viewModel: UbahProfileViewModel,
    context: Context
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    var idUser by remember{
        mutableStateOf("")
    }
    var token by remember {
        mutableStateOf("")
    }
    var textFullname by remember {
        mutableStateOf("")
    }
    var textEmail by remember {
        mutableStateOf("")
    }
    var textNoHandphone by remember {
        mutableStateOf("")
    }

    viewModel.getPreferences().asLiveData().observe(lifecycleOwner) {
        if (idUser.isEmpty()) { // Hanya atur nilai awal saat kosong
            idUser = it?.id.toString()
            token = it?.token.toString()
            textFullname = it?.fullname.toString()
            textNoHandphone = it?.noHandphone.toString()
            textEmail = it?.email.toString()
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
            text = "Nama Lengkap *",
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
            value = textFullname,
            onValueChange = {
                textFullname = it
            },
            placeholder = {
                Text(
                    text = "Masukkan Nama Lengkap Anda",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Normal,
                        fontSize = 11.sp,
                        fontFamily = FontFamily(Font(R.font.quicksand_medium))
                    )
                )
            },
            visualTransformation = VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.quicksand_medium))
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "No Handphone *",
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
            value = textNoHandphone,
            onValueChange = {
                textNoHandphone = it
            },
            placeholder = {
                Text(
                    text = "Masukkan No Handphone Anda",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Normal,
                        fontSize = 11.sp,
                        fontFamily = FontFamily(Font(R.font.quicksand_medium))
                    )
                )
            },
            visualTransformation = VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.quicksand_medium))
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Alamat Email",
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
            value = textEmail,
            onValueChange = {
                textEmail = it
            },
            placeholder = {
                Text(
                    text = "Masukkan Alamat Email Anda (Opsional)",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Normal,
                        fontSize = 11.sp,
                        fontFamily = FontFamily(Font(R.font.quicksand_medium))
                    )
                )
            },
            visualTransformation = VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email, imeAction = ImeAction.Done
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.quicksand_medium))
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                validationUbahProfile(
                    viewModel = viewModel,
                    fullname = textFullname,
                    email = textEmail,
                    no_handphone = textNoHandphone,
                    idUser = idUser,
                    token = token,
                    context = context,
                ) { loading ->
                    isLoading = loading
                }
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

private fun validationUbahProfile(
    viewModel: UbahProfileViewModel,
    fullname: String,
    email: String,
    no_handphone: String,
    idUser: String,
    token: String,
    context: Context,
    onLoadingStateChanged: (Boolean) -> Unit
) {
    if (fullname.isEmpty() || no_handphone.isEmpty()) {
        // Show error message
        return
    }

    UbahProfile(
        viewModel = viewModel,
        idUser = idUser,
        token = token,
        fullname = fullname,
        email = email,
        no_handphone = no_handphone,
        lifecycleOwner = context as LifecycleOwner,
        onLoadingStateChanged = onLoadingStateChanged
    )


}

private fun UbahProfile(
    viewModel: UbahProfileViewModel,
    idUser: String,
    token: String,
    fullname: String,
    email: String,
    no_handphone: String,
    lifecycleOwner: LifecycleOwner,
    onLoadingStateChanged: (Boolean) -> Unit
) {

    Log.d("Data", "idUser: $idUser, token: $token, fullname: $fullname, email: $email, no_handphone: $no_handphone")

    viewModel.updateAccountUser(idUser, fullname, email, no_handphone)
        .observe(lifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    onLoadingStateChanged(true)
                }

                is Result.Success -> {
                    onLoadingStateChanged(false)
                    runBlocking {
                        viewModel.savePreferences(
                            token = token,
                            id = idUser,
                            fullname = fullname,
                            no_handphone = no_handphone,
                            email = email
                        )
                    }
                    SweetAlertComponent(
                        title = "Berhasil",
                        contentText = "Data profil berhasil diubah",
                        context = lifecycleOwner as Context,
                        type = "success"
                    )
                    Log.d("UbahProfile", "Ubah Profile Success")
                }

                is Result.Error -> {
                    onLoadingStateChanged(false)
                    Log.e("Ubah Profile", "login: ${result}")
                    SweetAlertComponent(
                        context = lifecycleOwner as Context,
                        title = "Gagal",
                        contentText = "Mohon maaf anda gagal masuk, silahkan pastikan no handphone dan password benar",
                        type = "error"
                    )
                }
                else -> {
                    Log.e("Ubah Profile", "error: ${result}")
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

