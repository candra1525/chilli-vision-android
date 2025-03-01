package com.candra.chillivision.ui.pages.login

import android.content.Context
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.candra.chillivision.R
import com.candra.chillivision.component.AnimatedLoading
import com.candra.chillivision.component.HeaderComponentLoginRegister
import com.candra.chillivision.component.SweetAlertComponent
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.data.common.Result
import com.candra.chillivision.data.vmf.ViewModelFactory
import com.candra.chillivision.ui.theme.BlackMode
import com.candra.chillivision.ui.theme.PrimaryGreen
import com.candra.chillivision.ui.theme.WhiteSoft
import kotlinx.coroutines.runBlocking

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: LoginScreenViewModel = viewModel(
        factory = ViewModelFactory.getInstance(
            LocalContext.current
        )
    )
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .imePadding()
    ) {
        Column(modifier = modifier) {
            // Header
            HeaderComponentLoginRegister(context, modifier, navController)
            // Title
            TitleLogin(modifier)
            // Form
            FormLogin(modifier, viewModel, navController, context)
            // Catatan
            Catatan(modifier = modifier, navController = navController)
        }
    }
}

@Composable
fun TitleLogin(modifier: Modifier = Modifier) {
    Spacer(modifier = Modifier.height(16.dp))
    Column(modifier = Modifier.padding(horizontal = 32.dp)) {
        Text(
            text = "Masuk",
            modifier = Modifier,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 25.sp,
                fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                textAlign = TextAlign.Center
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Silahkan masuk dengan nomor handphone dan kata sandi yang terdaftar",
            modifier = Modifier,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.quicksand_medium)),
                textAlign = TextAlign.Justify
            )
        )
    }
}


@Composable
fun FormLogin(
    modifier: Modifier = Modifier,
    viewModel: LoginScreenViewModel,
    navController: NavController,
    context: Context
) {

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    var textNoHandphone by remember {
        mutableStateOf("")
    }

    var textPassword by remember {
        mutableStateOf("")
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    Column(modifier = modifier.padding(horizontal = 32.dp)) {
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
            isError = textNoHandphone.isEmpty(),
            visualTransformation = VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .focusRequester(focusRequester), // Tambahkan ini
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.quicksand_medium))
            ),
            enabled = !isLoading,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Kata Sandi *",
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
                    text = "Masukkan Kata Sandi Anda",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Normal,
                        fontSize = 11.sp,
                        fontFamily = FontFamily(Font(R.font.quicksand_medium))
                    )
                )
            },
            isError = textPassword.isEmpty(),
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

        if (isLoading) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedLoading(modifier = Modifier.width(80.dp))
                TextBold(text = "Memuat...", modifier = Modifier.padding(top = 8.dp), sized = 10)
            }
        } else {
            Button(
                onClick = {
                    validationLogin(
                        textNoHandphone.trim(),
                        textPassword.trim(),
                        context,
                        viewModel,
                        navController
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
                    text = if (isLoading) "Memuat..." else "Lanjut",
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
}


@Composable
private fun Catatan(modifier: Modifier = Modifier, navController: NavController) {
    Column(modifier = Modifier.padding(horizontal = 32.dp)) {
        Text(
            text = "Catatan :", style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                color = PrimaryGreen
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        val annotatedString = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = if (isSystemInDarkTheme()) WhiteSoft else BlackMode,
                )
            ) {
                append("Dengan melanjutkan proses pendaftaran akun pada Chilli Vision, saya menyetujui semua ")
            }
            pushStringAnnotation(tag = "terms", annotation = "terms")
            withStyle(
                style = SpanStyle(
                    color = PrimaryGreen,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append("Ketentuan Layanan")
            }
            pop()

            withStyle(
                style = SpanStyle(
                    color = if (isSystemInDarkTheme()) WhiteSoft else BlackMode,
                )
            ) {
                append(" dan ")
            }
            pushStringAnnotation(tag = "privacy", annotation = "privacy")
            withStyle(
                style = SpanStyle(
                    color = PrimaryGreen,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append("Kebijakan Privasi")
            }
            pop()
            withStyle(
                style = SpanStyle(
                    color = if (isSystemInDarkTheme()) WhiteSoft else BlackMode,
                )
            ) {
                append(".")
            }
        }


        ClickableText(
            text = annotatedString,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 10.sp,
                fontFamily = FontFamily(Font(R.font.quicksand_medium)),
                textAlign = TextAlign.Justify
            ),
            onClick = { offset ->
                annotatedString.getStringAnnotations(tag = "terms", start = offset, end = offset)
                    .firstOrNull()?.let {
                        navController.navigate("terms") // Ganti dengan route yang sesuai
                    }

                annotatedString.getStringAnnotations(tag = "privacy", start = offset, end = offset)
                    .firstOrNull()?.let {
                        navController.navigate("privacy") // Ganti dengan route yang sesuai
                    }
            }
        )
    }
}

private fun validationLogin(
    no_handphone: String,
    password: String,
    context: Context,
    viewModel: LoginScreenViewModel,
    navController: NavController,
    onLoadingStateChanged: (Boolean) -> Unit
) {
    if (no_handphone.isEmpty()) {
        SweetAlertComponent(context, "Peringatan", "No Handphone tidak boleh kosong", "warning")
    } else if (password.isEmpty()) {
        SweetAlertComponent(context, "Peringatan", "Password tidak boleh kosong", "warning")
    } else {
        login(
            viewModel = viewModel,
            noHandphone = no_handphone,
            lifecycleOwner = context as LifecycleOwner,
            password = password,
            navController = navController,
            onLoadingStateChanged = onLoadingStateChanged
        )
    }
}


private fun login(
    viewModel: LoginScreenViewModel,
    noHandphone: String,
    lifecycleOwner: LifecycleOwner,
    password: String,
    navController: NavController,
    onLoadingStateChanged: (Boolean) -> Unit
) {
    viewModel.setLogin(noHandphone, password).observe(lifecycleOwner) { result ->
        if (result != null) {
            when (result) {
                is Result.Loading -> {
                    onLoadingStateChanged(true)
                }

                is Result.Success -> {
                    onLoadingStateChanged(false)

                    runBlocking {
                        viewModel.savePreferences(
                            token = result.data.data?.token.toString(),
                            id = result.data.data?.id.toString(),
                            fullname = result.data.data?.fullname.toString(),
                            noHandphone = result.data.data?.noHandphone.toString(),
                            image = if (result.data.data?.imageUrl.toString() == null || result.data.data?.imageUrl.toString() == "null") "" else result.data.data?.imageUrl.toString()
                        )
                    }

                    Log.d("Token Berhasil Disimpan", "login: ${result.data.data}")

                    val fullname = result.data.data?.fullname.orEmpty()
                    SweetAlertComponent(
                        lifecycleOwner as Context,
                        "Berhasil",
                        "Hai, ${fullname}, Anda berhasil Masuk",
                        "success"
                    )
                    navController.navigate("home") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }

                is Result.Error -> {
                    onLoadingStateChanged(false)
                    Log.e("LoginScreen", "login: ${result}")
                    SweetAlertComponent(
                        lifecycleOwner as Context,
                        "Gagal",
                        "Mohon pastikan nomor handphone dan password benar",
                        "error"
                    )
                }

                else -> {
                    Log.e("LoginScreen", "error: ${result}")
                    SweetAlertComponent(
                        lifecycleOwner as Context,
                        "Kesalahan",
                        "Terjadi kesalahan sistem. Coba lagi nanti.",
                        "error"
                    )
                }
            }
        }
    }
}
