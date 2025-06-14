package com.candra.chillivision.ui.pages.profile.ubah

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.twotone.Edit
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import com.candra.chillivision.component.AnimatedLoading
import com.candra.chillivision.component.ButtonBorderCustom
import com.candra.chillivision.component.ButtonBorderGreen
import com.candra.chillivision.component.InitialAvatar
import com.candra.chillivision.component.SweetAlertComponent
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.component.compressImage
import com.candra.chillivision.component.uriToFile
import com.candra.chillivision.data.common.Result
import com.candra.chillivision.data.vmf.ViewModelFactory
import com.candra.chillivision.ui.theme.BlackMode
import com.candra.chillivision.ui.theme.PrimaryGreen
import com.candra.chillivision.ui.theme.Red
import com.candra.chillivision.ui.theme.WhiteSoft
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UbahProfile(
    modifier: Modifier = Modifier, navController: NavController,
    viewModel: UbahProfileViewModel = viewModel(
        factory = ViewModelFactory.getInstance(
            LocalContext.current
        )
    )
) {
    val isDarkTheme = isSystemInDarkTheme()
    val context = LocalContext.current

    var isChanged by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TextBold(
                        "Profil",
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
            ContentUbahProfile(
                modifier = modifier,
                viewModel = viewModel,
                context = context,
                navController = navController,
                onChanged = { isChanged = it }
            )
        }
    }
}

@Composable
private fun ContentUbahProfile(
    modifier: Modifier = Modifier,
    viewModel: UbahProfileViewModel,
    context: Context,
    navController: NavController,
    onChanged: (Boolean) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        FormUbahProfile(
            modifier = modifier,
            viewModel = viewModel,
            context = context,
            navController = navController,
            onChanged = onChanged
        )
    }
}


@Composable
private fun FormUbahProfile(
    modifier: Modifier = Modifier,
    viewModel: UbahProfileViewModel,
    context: Context,
    navController: NavController,
    onChanged: (Boolean) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    // Simpan nilai awal dari user
    var idUser by remember { mutableStateOf("") }
    var token by remember { mutableStateOf("") }
    var originalFullname by remember { mutableStateOf("") }
    var originalNoHandphone by remember { mutableStateOf("") }
    var originalImage by remember { mutableStateOf("") } // Link Image
    var originalSubscriptionName by remember { mutableStateOf("")}
    var textFullname by remember { mutableStateOf("") }
    var textNoHandphone by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) } // URI

    // Ambil data awal dari ViewModel
    LaunchedEffect(Unit) {
        if (idUser.isEmpty() && token.isEmpty()) {
            viewModel.getPreferences().asLiveData().observe(lifecycleOwner) {
                if (idUser.isEmpty()) {
                    idUser = it?.id.toString()
                    token = it?.token.toString()
                    originalFullname = it?.fullname.toString()
                    originalNoHandphone = it?.noHandphone.toString()
                    originalImage = it?.image.toString()
                    originalSubscriptionName = it?.subscriptionName.toString()
                    textFullname = originalFullname
                    textNoHandphone = originalNoHandphone
                }
                Log.d("Ubah Profile ", it.toString())
            }
        }
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    var isLoadingImage by remember {
        mutableStateOf(false)
    }


    fun checkChanges() {
        val hasChanged = textFullname != originalFullname || textNoHandphone != originalNoHandphone
        onChanged(hasChanged)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
        val imageFile = imageUri?.let { uriToFile(it, context) }

        if (imageFile != null) {
            val compressedFile = compressImage(imageFile, 2048)

            val mimeType = when (compressedFile.extension.lowercase()) {
                "png" -> "image/png"
                "jpg", "jpeg" -> "image/jpeg"
                else -> "image/jpeg" // Default
            }

            val requestImageToFile = compressedFile.asRequestBody(mimeType.toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "image", compressedFile.name, requestImageToFile
            )

            viewModel.updatePhotoAccountUser(
                image = multipartBody,
                id = idUser
            ).observe(lifecycleOwner) { result ->
                when (result) {
                    is Result.Loading -> {
                        isLoadingImage = true
                    }

                    is Result.Success -> {
                        isLoadingImage = false
                        originalImage = result.data.data?.urlImage ?: ""
                        runBlocking {
                            viewModel.savePreferences(
                                token = token,
                                id = idUser,
                                fullname = textFullname,
                                no_handphone = textNoHandphone,
                                image = originalImage,
                                subscriptionName = originalSubscriptionName
                            )
                        }
                        SweetAlertComponent(
                            context = context,
                            title = "Berhasil",
                            contentText = "Foto profil berhasil diubah",
                            type = "success"
                        )
                    }

                    is Result.Error -> {
                        isLoadingImage = false
                        Log.e("Ubah Profile", "ubahprofile: ${result}")
                        SweetAlertComponent(
                            context = context,
                            title = "Gagal",
                            contentText = "Mohon maaf anda gagal mengubah foto profil, silahkan pastikan masukkan anda benar",
                            type = "error"
                        )
                    }

                    else -> {
                        isLoadingImage = false
                        Log.e("Ubah Profile", "error: ${result}")
                        SweetAlertComponent(
                            context = context,
                            title = "Kesalahan",
                            contentText = "Mohon maaf sepertinya ada kesalahan sistem. Mohon ulangi beberapa saat lagi...",
                            type = "error"
                        )
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (isLoadingImage) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimatedLoading(modifier = Modifier.width(80.dp))
                    TextBold(
                        text = "Sedang memperbarui foto...",
                        modifier = Modifier.padding(top = 8.dp),
                        sized = 10
                    )
                }
            } else {
                InitialAvatar(
                    fullname = textFullname,
                    imageUrl = originalImage,
                    size = 120.dp,
                )
            }


            Spacer(modifier = Modifier.padding(16.dp))
            if(!isLoadingImage) {
                Row() {
                    if (originalImage.isEmpty()) {
                        ButtonBorderGreen(
                            onClick = { launcher.launch("image/*") },
                            text = "Ubah Foto",
                            width = 150.dp,
                            icon = Icons.TwoTone.Edit
                        )
                    }

                    if (originalImage.isNotEmpty()) {
                        Spacer(modifier = Modifier.padding(8.dp))
                        ButtonBorderCustom(
                            onClick = {
                                SweetAlertComponent(
                                    context = context,
                                    title = "Peringatan",
                                    contentText = "Apakah anda yakin ingin menghapus foto profil?",
                                    type = "perhatian",
                                    isCancel = true,
                                    confirmYes = {
                                        viewModel.deletePhotoProfile(idUser)
                                            .observe(lifecycleOwner) { result ->
                                                when (result) {
                                                    is Result.Loading -> {
                                                        isLoadingImage = true
                                                    }

                                                    is Result.Success -> {
                                                        isLoadingImage = false
                                                        originalImage = ""
                                                        runBlocking {
                                                            viewModel.savePreferences(
                                                                token = token,
                                                                id = idUser,
                                                                fullname = textFullname,
                                                                no_handphone = textNoHandphone,
                                                                image = "", // Empty Image
                                                                subscriptionName = originalSubscriptionName
                                                            )
                                                        }
                                                        SweetAlertComponent(
                                                            context = context,
                                                            title = "Berhasil",
                                                            contentText = "Foto profil berhasil dihapus",
                                                            type = "success"
                                                        )
                                                    }

                                                    is Result.Error -> {
                                                        isLoadingImage = false
                                                        Log.e("Ubah Profile", "delete: ${result}")
                                                        SweetAlertComponent(
                                                            context = context,
                                                            title = "Gagal",
                                                            contentText = "Mohon maaf anda gagal menghapus foto profil, silahkan pastikan masukkan anda benar",
                                                            type = "error"
                                                        )
                                                    }

                                                    else -> {
                                                        isLoadingImage = false
                                                        Log.e("Ubah Profile", "error: ${result}")
                                                        SweetAlertComponent(
                                                            context = context,
                                                            title = "Kesalahan",
                                                            contentText = "Mohon maaf sepertinya ada kesalahan sistem. Mohon ulangi beberapa saat lagi...",
                                                            type = "error"
                                                        )

                                                    }
                                                }
                                            }
                                    }
                                )
                            },
                            text = "Hapus Foto",
                            color = Red,
                            textColor = Red,
                            width = 150.dp,
                            icon = Icons.Default.Delete,
                        )
                    }


                }
            }
            Spacer(modifier = Modifier.padding(8.dp))
            TextBold(
                text = "Pastikan semua data terisi dengan baik dan benar.",
                colors = if (isSystemInDarkTheme()) WhiteSoft else BlackMode,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

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
                checkChanges()
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
                checkChanges()
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
                    validationUbahProfile(
                        viewModel = viewModel,
                        fullname = textFullname,
                        no_handphone = textNoHandphone,
                        subscription_name = originalSubscriptionName,
                        idUser = idUser,
                        token = token,
                        context = context,
                        onSuccess = {
                            originalFullname = textFullname
                            originalNoHandphone = textNoHandphone
                            onChanged(false)
                        },
                        onLoadingStateChanged = { loading ->
                            isLoading = loading
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
        Spacer(modifier = Modifier.height(32.dp))
        TextBold(text = "Catatan: * Wajib untuk diisi", colors = Red)
    }
}

private fun validationUbahProfile(
    viewModel: UbahProfileViewModel,
    fullname: String,
    no_handphone: String,
    subscription_name : String,
    idUser: String,
    token: String,
    context: Context,
    onLoadingStateChanged: (Boolean) -> Unit,
    onSuccess: () -> Unit = {}
) {
    if (fullname.isEmpty() || no_handphone.isEmpty()) {
        SweetAlertComponent(
            context = context,
            title = "Peringatan",
            contentText = "Nama lengkap dan no handphone tidak boleh kosong",
            type = "warning"
        )
        return
    }

    UpdateProfile(
        viewModel = viewModel,
        idUser = idUser,
        token = token,
        fullname = fullname,
        no_handphone = no_handphone,
        subscription_name = subscription_name,
        lifecycleOwner = context as LifecycleOwner,
        onLoadingStateChanged = onLoadingStateChanged,
        onSuccess = onSuccess
    )
}

private fun UpdateProfile(
    viewModel: UbahProfileViewModel,
    idUser: String,
    token: String,
    fullname: String,
    no_handphone: String,
    subscription_name : String,
    lifecycleOwner: LifecycleOwner,
    onLoadingStateChanged: (Boolean) -> Unit,
    onSuccess: () -> Unit
) {
    viewModel.updateAccountUser(id = idUser, fullname = fullname, no_handphone = no_handphone)
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
                            image = result.data.data?.imageUrl ?: "",
                            subscriptionName = subscription_name
                        )
                    }

                    SweetAlertComponent(
                        title = "Berhasil",
                        contentText = "Data profil berhasil diubah",
                        context = lifecycleOwner as Context,
                        type = "success"
                    )
                    onSuccess()
                }

                is Result.Error -> {
                    onLoadingStateChanged(false)
                    Log.e("Ubah Profile", "login: ${result}")
                    SweetAlertComponent(
                        context = lifecycleOwner as Context,
                        title = "Gagal",
                        contentText = "Mohon maaf anda gagal mengubah profil, silahkan pastikan masukkan anda benar",
                        type = "error"
                    )
                }

                else -> {
                    onLoadingStateChanged(false)
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