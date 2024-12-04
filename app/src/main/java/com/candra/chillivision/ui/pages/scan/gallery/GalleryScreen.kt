package com.candra.chillivision.ui.pages.scan.gallery

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.candra.chillivision.R
import com.candra.chillivision.component.dashedBorder
import com.candra.chillivision.data.vmf.ViewModelFactory
import com.candra.chillivision.ui.theme.PrimaryGreen

@Composable
fun GalleryScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: GalleryScreenViewModel = viewModel(
        factory = ViewModelFactory.getInstance(
            LocalContext.current
        )
    )
) {
    val isDarkTheme = isSystemInDarkTheme()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    var isImageUpload by remember {
        mutableStateOf(false)
    }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        isImageUpload = uri != null
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(start = 32.dp, end = 32.dp, bottom = 32.dp),
    ) {
        Box(
            modifier = modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.back_arrow),
                contentDescription = "back header ${R.string.app_name}",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { navController.popBackStack() },
            )

            Text(
                text = "Unggah Gambar", style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                    color = PrimaryGreen,
                    textAlign = TextAlign.Center
                ), modifier = Modifier.fillMaxWidth()
            )

            if (isImageUpload) {
                Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                    Image(
                        painter = painterResource(id = R.drawable.unggah_ulang),
                        contentDescription = "back header ${R.string.app_name}",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { isImageUpload = false },

                        )

                    Spacer(modifier = Modifier.width(16.dp))

                    Image(
                        painter = painterResource(id = R.drawable.oke),
                        contentDescription = "back header ${R.string.app_name}",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { isImageUpload = false },

                        )

                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
        ) {
            if (isImageUpload) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .dashedBorder(2.dp, 16.dp, PrimaryGreen),
                    contentAlignment = Alignment.Center
                ) {
                    selectedImageUri?.let {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(model = it),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(16.dp))
                            )
                        }
                    } ?: run {
                        UploadImage(
                            launcher = launcher, isImage = false
                        )
                    }
                }
            } else {
                UploadImage(
                    launcher = launcher
                )
            }
        }
    }
}


@Composable
private fun UploadImage(
    modifier: Modifier = Modifier, launcher: ActivityResultLauncher<String>, isImage: Boolean = true
) {
    Box(modifier = modifier
        .fillMaxSize()
        .dashedBorder(2.dp, 16.dp, PrimaryGreen)
        .padding(16.dp)
        .clickable {
            launcher.launch("image/*")
        }) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!isImage) {
                Text("Tidak ada gambar yang dipilih.")
            }
            Image(
                painter = painterResource(id = R.drawable.upload_cloud),
                contentDescription = "camera_scan",
                modifier = Modifier.size(100.dp),
                colorFilter = ColorFilter.tint(Color.Gray)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Silahkan unggah salah satu gambar penyakit tanaman cabai rawit pada daun yang ingin di identifikasi.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Gray,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.quicksand_regular)),
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}
