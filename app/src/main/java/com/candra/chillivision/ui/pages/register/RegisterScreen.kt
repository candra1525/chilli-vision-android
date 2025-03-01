package com.candra.chillivision.ui.pages.register

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.graphics.Color
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
import com.candra.chillivision.ui.theme.PrimaryGreen

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: RegisterScreenViewModel = viewModel(
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
            TitleRegister(modifier)
            // Form
            FormRegister(modifier, viewModel, context, navController)

        }
    }
}


@Composable
private fun TitleRegister(modifier: Modifier = Modifier) {
    Spacer(modifier = Modifier.height(16.dp))
    Column(modifier = Modifier.padding(horizontal = 32.dp)) {
        Text(
            text = "Daftar", modifier = Modifier, style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 25.sp,
                fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                textAlign = TextAlign.Center
            )
        )

        Text(
            text = "Silahkan lengkapi form pendaftaran berikut!",
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
private fun FormRegister(
    modifier: Modifier = Modifier,
    viewModel: RegisterScreenViewModel,
    context: Context,
    navController: NavController
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    var textFullname by remember {
        mutableStateOf("")
    }

    var textNoHandphone by remember {
        mutableStateOf("")
    }

    var textPassword by remember {
        mutableStateOf("")
    }

    var textKonfirmasiPassword by remember {
        mutableStateOf("")
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    var isChecked by remember {
        mutableStateOf(false)
    }

    Column(modifier = modifier.padding(horizontal = 32.dp)) {
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
            isError = textFullname.isBlank(),
            visualTransformation = VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .focusRequester(focusRequester),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.quicksand_medium))
            ),
            enabled = !isLoading
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
            isError = textNoHandphone.isBlank(),
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
            ),
            enabled = !isLoading
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
            isError = textPassword.isBlank(),
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
            text = "Konfirmasi Kata Sandi *",
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
            value = textKonfirmasiPassword,
            onValueChange = {
                textKonfirmasiPassword = it
            },
            placeholder = {
                Text(
                    text = "Masukkan Konfirmasi Kata Sandi Anda",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Normal,
                        fontSize = 11.sp,
                        fontFamily = FontFamily(Font(R.font.quicksand_medium))
                    )
                )
            },
            isError = textKonfirmasiPassword.isBlank(),
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

        SyaratKetentuan(
            isChecked = isChecked,
            onCheckedChange = { isChecked = it },
            navController = navController,
            isLoading = isLoading
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
                    validationRegister(
                        viewModel = viewModel,
                        fullname = textFullname.trim(),
                        no_handphone = textNoHandphone.trim(),
                        password = textPassword.trim(),
                        confirmPassword = textKonfirmasiPassword.trim(),
                        isChecked = isChecked,
                        context = context,
                        navController = navController,
                    ) { loading ->
                        isLoading = loading
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                shape = RoundedCornerShape(8.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
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
private fun SyaratKetentuan(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    navController: NavController,
    isLoading: Boolean = false
) {
    Column(modifier = Modifier) {
        Text(
            text = "Catatan :", style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                color = PrimaryGreen
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        CheckBox(
            isChecked = isChecked,
            onCheckedChange = onCheckedChange,
            navController = navController,
            isLoading = isLoading
        )
    }
}

@Composable
fun CheckBox(isChecked: Boolean, onCheckedChange: (Boolean) -> Unit, navController: NavController, isLoading : Boolean = false) {
    val annotatedString = buildAnnotatedString {
        append("Dengan melanjutkan proses pendaftaran akun pada Chilli Vision, saya menyetujui semua ")

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

        append(" dan ")

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
        append(".")
    }

    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = { onCheckedChange(it) },
            colors = CheckboxDefaults.colors(
                checkedColor = PrimaryGreen,
                uncheckedColor = Color.Gray,
                checkmarkColor = Color.White
            ),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.width(8.dp))

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


private fun validationRegister(
    viewModel: RegisterScreenViewModel,
    fullname: String,
    no_handphone: String,
    password: String,
    confirmPassword: String,
    isChecked: Boolean,
    navController: NavController,
    context: Context,
    onLoadingStateChanged: (Boolean) -> Unit
) {
    if (fullname.isEmpty() || no_handphone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
        SweetAlertComponent(
            context,
            "Peringatan",
            "Nama Lengkap, No Handphone, Kata Sandi, dan Konfirmasi Kata Sandi tidak boleh kosong",
            "warning"
        )
        return
    }
    if (password.length < 8) {
        SweetAlertComponent(
            context, "Peringatan", "Kata Sandi minimal 8 karakter", "warning"
        )
        return
    }

    if (password != confirmPassword) {
        SweetAlertComponent(
            context,
            "Peringatan",
            "Konfirmasi Kata Sandi tidak sama dengan Kata Sandi",
            "warning"
        )
        return
    }

    if (!isChecked) {
        SweetAlertComponent(
            context,
            "Peringatan",
            "Anda harus menyetujui Ketentuan Layanan dan Kebijakan Privasi untuk melanjutkan 😉",
            "warning"
        )
        return
    }

    register(
        viewModel,
        fullname,
        no_handphone,
        password,
        context as LifecycleOwner,
        navController,
        onLoadingStateChanged
    )


}


private fun register(
    viewModel: RegisterScreenViewModel,
    fullname: String,
    no_handphone: String,
    password: String,
    lifecycleOwner: LifecycleOwner,
    navController: NavController,
    onLoadingStateChanged: (Boolean) -> Unit
) {
    viewModel.setRegister(fullname, no_handphone, password)
        .observe(lifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        onLoadingStateChanged(true)
                    }

                    is Result.Success -> {
                        onLoadingStateChanged(false)
                        val fullname = result.data.data?.fullname

                        if (fullname != null) {
                            SweetAlertComponent(
                                lifecycleOwner as Context,
                                "Berhasil",
                                "Akun ${fullname} berhasil didaftarkan. Silahkan masuk terlebih dahulu 😉",
                                "success"
                            )
                            navController.navigate("login") {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            }
                        }
                    }

                    is Result.Error -> {
                        onLoadingStateChanged(false)
                        // Show Error Message
                        SweetAlertComponent(
                            lifecycleOwner as Context,
                            "Gagal",
                            result.errorMessage,
                            "error"
                        )
                    }
                }
            }
        }

}