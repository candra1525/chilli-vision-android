package com.candra.chillivision.ui.pages.login

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
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
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    TextBold(
                        "Masuk Akun",
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
                actions = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isSystemInDarkTheme()) BlackMode else WhiteSoft,
                    scrolledContainerColor = if (isSystemInDarkTheme()) BlackMode else WhiteSoft
                ),
                modifier = Modifier.shadow(1.dp)
            )
        },
        containerColor = if (isSystemInDarkTheme()) BlackMode else WhiteSoft
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            TitleLogin(modifier)
            FormLogin(modifier, viewModel, navController, context, scope)
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
            text = "Masuk", modifier = Modifier, style = MaterialTheme.typography.bodyMedium.copy(
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


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FormLogin(
    modifier: Modifier = Modifier,
    viewModel: LoginScreenViewModel,
    navController: NavController,
    context: Context,
    scope: CoroutineScope
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
        OutlinedTextField(value = textPassword,
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
                    scope.launch {
                        validationLogin(
                            no_handphone = textNoHandphone.trim(),
                            password = textPassword.trim(),
                            context = context,
                            viewModel = viewModel,
                            navController = navController,
                            onLoadingStateChanged = { loading ->
                                isLoading = loading
                            },
                            scope = scope
                        )
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


        ClickableText(text = annotatedString, style = MaterialTheme.typography.bodyMedium.copy(
            fontSize = 10.sp,
            fontFamily = FontFamily(Font(R.font.quicksand_medium)),
            textAlign = TextAlign.Justify
        ), onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "terms", start = offset, end = offset)
                .firstOrNull()?.let {
                    navController.navigate("terms") // Ganti dengan route yang sesuai
                }

            annotatedString.getStringAnnotations(tag = "privacy", start = offset, end = offset)
                .firstOrNull()?.let {
                    navController.navigate("privacy") // Ganti dengan route yang sesuai
                }
        })
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun validationLogin(
    no_handphone: String,
    password: String,
    context: Context,
    viewModel: LoginScreenViewModel,
    navController: NavController,
    onLoadingStateChanged: (Boolean) -> Unit,
    scope : CoroutineScope
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
            onLoadingStateChanged = onLoadingStateChanged,
            scope = scope
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
private fun login(
    viewModel: LoginScreenViewModel,
    noHandphone: String,
    lifecycleOwner: LifecycleOwner,
    password: String,
    navController: NavController,
    onLoadingStateChanged: (Boolean) -> Unit,
    scope : CoroutineScope,
) {
    viewModel.setLogin(noHandphone, password).observe(lifecycleOwner) { result ->
        if (result != null) {
            when (result) {
                is Result.Loading -> {
                    onLoadingStateChanged(true)
                }

                is Result.Success -> {
                    viewModel.savePreferences(
                        token = result.data.data?.token.toString(),
                        id = result.data.data?.id.toString(),
                        fullname = result.data.data?.fullname.toString(),
                        noHandphone = result.data.data?.noHandphone.toString(),
                        image = if (result.data.data?.imageUrl.toString() == null || result.data.data?.imageUrl.toString() == "null") "" else result.data.data?.imageUrl.toString(),
                        subscriptionName = result.data.data?.historySubscriptions?.subscriptions?.title ?: "Gratis"
                    ) {
                        Log.d("Token Berhasil Disimpan", "login: ${result.data.data}")

                        val fullname = result.data.data?.fullname.orEmpty()
                        // pengecekan
                        scope.launch {
                            initializeApp(viewModel)
                            checkSubscriptionInBackground(scope = scope, viewModel = viewModel, lifecycleOwner = lifecycleOwner, navController = navController, onDone = {
                                onLoadingStateChanged(false)
                                SweetAlertComponent(
                                    lifecycleOwner as Context,
                                    "Berhasil",
                                    "Hai, ${fullname}, Anda berhasil Masuk",
                                    "success"
                                )
                                navController.navigate("home") {
                                    popUpTo("login") {
                                        inclusive = true // hapus login dari back stack
                                    }
                                    launchSingleTop = true
                                }
                            })
                        }
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


private suspend fun initializeApp(viewModel : LoginScreenViewModel) {
    val dateNow = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val datePref = viewModel.getUsageDate().firstOrNull()

    if (datePref.isNullOrEmpty() || datePref != dateNow) {
        viewModel.setCountUsageAI("0")
        viewModel.setCountUsageDetect("0")
        viewModel.setUsageDate(dateNow)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun checkSubscriptionInBackground(scope : CoroutineScope, viewModel: LoginScreenViewModel, lifecycleOwner: LifecycleOwner, navController: NavController, onDone : () -> Unit = {}) {
    scope.launch {
        val preferences = viewModel.getPreferences().firstOrNull()
        val userId = preferences?.id.orEmpty()
        if (userId.isEmpty()) return@launch

        viewModel.checkSubscriptionActive(userId)
            .observe(lifecycleOwner) { result ->
                when (result) {
                    is Result.Success -> {
                        val data = result.data.data
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
                        val today = LocalDate.now()
                        val endDate = data?.endDate?.let { LocalDate.parse(it, formatter) }
                        val startDate = data?.startDate?.let { LocalDate.parse(it, formatter) }

                        if (endDate != null && endDate >= today) {
                            // Langganan aktif
                            scope.launch {
                                viewModel.setSubscriptionName(data.subscriptions?.title ?: "Gratis")
                                viewModel.setStartEndSubscriptionDate(
                                    startDate = startDate.toString(),
                                    endDate = endDate.toString()
                                )
                                onDone()
                            }
                        } else {
                            // Expired atau tidak ada langganan
                            viewModel.updateStatusSubscriptionUser(data?.id.orEmpty(), "expired")
                                .observeForever {
                                    scope.launch {
                                        viewModel.setSubscriptionName("Gratis")
                                        viewModel.setStartEndSubscriptionDate("", "")
                                        onDone()
                                    }
                                }
                        }
                    }

                    is Result.Error -> {
                        scope.launch {
                            viewModel.setSubscriptionName("Gratis")
                            viewModel.setStartEndSubscriptionDate("", "")
                            onDone()
                        }
                    }

                    else -> {}
                }
            }
    }
}