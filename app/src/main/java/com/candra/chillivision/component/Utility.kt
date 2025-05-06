package com.candra.chillivision.component

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.candra.chillivision.R
import com.candra.chillivision.ui.theme.BlackMode
import com.candra.chillivision.ui.theme.PrimaryGreen
import com.candra.chillivision.ui.theme.White
import com.candra.chillivision.ui.theme.WhiteSoft
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.abs

fun Modifier.dashedBorder(width: Dp, radius: Dp, color: Color) =
    drawBehind {
        drawIntoCanvas {
            val paint = Paint()
                .apply {
                    strokeWidth = width.toPx()
                    this.color = color
                    style = PaintingStyle.Stroke
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                }
            it.drawRoundRect(
                width.toPx(),
                width.toPx(),
                size.width - width.toPx(),
                size.height - width.toPx(),
                radius.toPx(),
                radius.toPx(),
                paint
            )
        }
    }

// Check Internet
fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

    return when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
}

@SuppressLint("QueryPermissionsNeeded")
fun directToGmail(context: Context) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data =
            Uri.parse("https://mail.google.com/mail/?view=cm&fs=1&to=candra_2007@mhs.mdp.ac.id&su=Feedback+untuk+ChilliVision&body=Halo+Developer%2C+saya+ingin+memberikan+feedback...")
    }

    intent.setPackage("com.google.android.gm") // Hanya membuka aplikasi Gmail

    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        Toast.makeText(context, "Gmail tidak ditemukan di perangkat ini.", Toast.LENGTH_SHORT)
            .show()
    }
}


//fun handleCameraPermission(
//    context: Context,
//    permissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
//    cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>,
//    capturedImageUri: Uri?
//) {
//    if (ContextCompat.checkSelfPermission(
//            context,
//            Manifest.permission.CAMERA
//        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
//    ) {
//        capturedImageUri?.let {
//            cameraLauncher.launch(it)
//        }
//    } else {
//        permissionLauncher.launch(Manifest.permission.CAMERA)
//    }
//}

fun handleCameraPermission(
    context: Context,
    permissionLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
    cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>,
    capturedImageUri: Uri?
) {
    val permissionsNeeded = mutableListOf(Manifest.permission.CAMERA)

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        permissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    val allGranted = permissionsNeeded.all { permission ->
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    if (allGranted) {
        capturedImageUri?.let { cameraLauncher.launch(it) }
    } else {
        permissionLauncher.launch(permissionsNeeded.toTypedArray())
    }
}


// Fungsi untuk membuat URI gambar di penyimpanan sementara
//fun createImageUri(context: Context): Uri {
//    val contentValues = android.content.ContentValues().apply {
//        put(MediaStore.Images.Media.DISPLAY_NAME, "captured_image.jpg")
//        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
//        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
//    }
//    return context.contentResolver.insert(
//        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//        contentValues
//    )
//        ?: throw IllegalStateException("Gagal membuat URI gambar")
//}

fun createImageUri(context: Context): Uri {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        // Android 10 ke atas
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(
                MediaStore.Images.Media.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES + "/ScanImages"
            )
        }

        context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ) ?: throw IllegalStateException("Gagal membuat URI untuk Android 10+")
    } else {
        // Android 8 dan 9
        val imageDir =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ScanImages")
        if (!imageDir.exists()) imageDir.mkdirs()

        val imageFile = File(imageDir, "IMG_${System.currentTimeMillis()}.jpg")
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            imageFile
        )
    }
}


fun directToWhatsapp(context: Context, phoneNumber: String, message: String = "") {
    try {
        val url = "https://wa.me/$phoneNumber?text=${Uri.encode(message)}"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@Composable
fun InitialAvatar(
    fullname: String,
    imageUrl: String = "",
    size: Dp = 80.dp,
    fs: Int = 60,
    isLoading: Boolean = false,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    onClick: () -> Unit = {} // Tambahkan callback klik
) {
    val firstLetter = fullname.firstOrNull()?.uppercase() ?: "?"

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(Color.Gray)
            .clickable { onClick() }, // Tambahkan klik di sini
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.fillMaxSize(0.3f),
                    color = PrimaryGreen
                )
            }

            imageUrl == "null" -> {
                Text(
                    text = firstLetter,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = fs.sp,  // Sesuaikan font dengan ukuran avatar
                        fontFamily = FontFamily(Font(R.font.quicksand_bold))
                    )
                )
            }

            imageUrl.isNotEmpty() -> {
                Image(
                    painter = rememberAsyncImagePainter(model = imageUrl),
                    contentDescription = "Profile Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(size),
                )
            }

            else -> {
                Text(
                    text = firstLetter,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = fs.sp,  // Sesuaikan font dengan ukuran avatar
                        fontFamily = FontFamily(Font(R.font.quicksand_bold))
                    )
                )
            }
        }
    }
}


@Composable
fun ButtonGreen(
    onClick: () -> Unit,
    text: String,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
        enabled = !isLoading
    ) {
        Text(
            text = if (isLoading) "Memuat..." else text,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                textAlign = TextAlign.Center,
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ButtonCustomColorWithIcon(
    onClick: () -> Unit,
    text: String,
    isLoading: Boolean = false,
    color: Color,
    icon: ImageVector? = null,
    width: Dp? = null, // Opsional: menentukan lebar tombol
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .then(if (width != null) Modifier.width(width) else Modifier.fillMaxWidth())
            .height(40.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        enabled = !isLoading
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = WhiteSoft,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Text(
            text = if (isLoading) "Memuat..." else text,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                textAlign = TextAlign.Center,
                color = WhiteSoft
            )
        )
    }
}

@Composable
fun ButtonBorderCustom(
    onClick: () -> Unit,
    text: String,
    textSize: Int = 12,
    color: Color,
    textColor: Color,
    width: Dp? = null, // Parameter width opsional
    icon: ImageVector? = null, // Parameter icon opsional
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .then(if (width != null) Modifier.width(width) else Modifier.fillMaxWidth())
            .border(
                width = 1.dp,
                color = color,
                shape = RoundedCornerShape(8.dp)
            )
            .height(40.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSystemInDarkTheme()) BlackMode else WhiteSoft,
            contentColor = textColor
        )
    ) {
        // Menampilkan ikon jika ada
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp)) // Memberi jarak antara ikon dan teks
        }

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = textSize.sp,
                fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                textAlign = TextAlign.Center,
            )
        )
    }
}


@Composable
fun ButtonBorderGreen(
    onClick: () -> Unit,
    text: String,
    textSize: Int = 12,
    width: Dp? = null, // Parameter width opsional
    icon: ImageVector? = null, // Parameter icon opsional
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .then(if (width != null) Modifier.width(width) else Modifier.fillMaxWidth())
            .border(
                width = 1.dp,
                color = PrimaryGreen,
                shape = RoundedCornerShape(8.dp)
            )
            .height(40.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSystemInDarkTheme()) BlackMode else WhiteSoft,
            contentColor = PrimaryGreen
        )
    ) {
        // Menampilkan ikon jika ada
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryGreen,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp)) // Memberi jarak antara ikon dan teks
        }

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = textSize.sp,
                fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                textAlign = TextAlign.Center,
            )
        )
    }
}


fun uriToFile(imageUri: Uri, context: Context): File {
    val contentResolver = context.contentResolver
    val mimeType = contentResolver.getType(imageUri)

    val extension = when (mimeType) {
        "image/png" -> "png"
        "image/jpeg", "image/jpg" -> "jpg"
        else -> "jpg"
    }

    val file = File(context.cacheDir, "temp_image.$extension")
    contentResolver.openInputStream(imageUri)?.use { inputStream ->
        file.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }
    }
    return file
}


fun formatRupiah(number: Int): String {
    val reverse = number.toString().reversed()
    val reverseWithDot = reverse.chunked(3).joinToString(".").reversed()
    return "Rp.$reverseWithDot,00"
}

fun DateToday(): String {
    val calendar = Calendar.getInstance()
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
    return simpleDateFormat.format(calendar.time)
}


fun DatePlusMonths(date: String, periode: Int): String? {
    return try {
        val inputFormatter = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
        val calendar = Calendar.getInstance()
        val parsedDate = inputFormatter.parse(date)

        if (parsedDate != null) {
            calendar.time = parsedDate
            calendar.add(Calendar.MONTH, periode)

            val outputFormatter = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
            outputFormatter.format(calendar.time)
        } else {
            null
        }
    } catch (e: ParseException) {
        Log.e("DATE_PLUS_MONTHS", "Format tanggal input tidak valid. Gunakan format yyyy-MM-dd.", e)
        null
    }
}

fun konversiFormatTanggal(tanggalInput: String): String? {
    return try {
        // Format input sesuai dengan format tanggal yang diberikan
        val inputFormatter = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
        val tanggal = inputFormatter.parse(tanggalInput)

        // Format output menjadi "dd MMMM yyyy" dalam Bahasa Indonesia
        val outputFormatter = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
        tanggal?.let {
            outputFormatter.format(it)
        }
    } catch (e: ParseException) {
        Log.e("KONVERSI FORMAT TANGGAL", "Format tanggal tidak valid. $e")
        null
    }
}

fun konversiFormatTanggal2(tanggalInput: String): String? {
    return try {
        // Format input: "yyyy-MM-dd"
        val inputFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val tanggal = inputFormatter.parse(tanggalInput)

        // Format output: "dd MMMM yyyy" dalam Bahasa Indonesia
        val outputFormatter = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
        tanggal?.let {
            outputFormatter.format(it)
        }
    } catch (e: ParseException) {
        Log.e("KONVERSI FORMAT TANGGAL", "Format tanggal tidak valid. $e")
        null
    }
}

fun compressImage(imageFile: File, maxSizeKB: Int): File {
    val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)

    val format = when {
        imageFile.extension.equals("png", ignoreCase = true) -> Bitmap.CompressFormat.PNG
        else -> Bitmap.CompressFormat.JPEG
    }

    var quality = if (format == Bitmap.CompressFormat.PNG) 100 else 90
    val outputStream = ByteArrayOutputStream()

    do {
        outputStream.reset()
        bitmap.compress(format, quality, outputStream)
        quality -= 5
    } while (outputStream.toByteArray().size / 1024 > maxSizeKB && quality > 5)

    // Simpan hasil compress ke file baru
    val compressedFile = File(imageFile.parent, "compressed_${imageFile.name}")
    FileOutputStream(compressedFile).use { it.write(outputStream.toByteArray()) }

    return compressedFile
}

fun convertIsoToDateTime(isoTimestamp: String): String? {
    return try {
        // Format input ISO 8601, contoh: "2025-04-20T14:30:00.000000Z"
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.US)
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")

        val date = inputFormat.parse(isoTimestamp)

        // Format output: "20 April 2025, 21:30" (waktu Asia/Jakarta)
        val outputFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("id", "ID"))
        outputFormat.timeZone = TimeZone.getTimeZone("Asia/Jakarta")

        date?.let {
            outputFormat.format(it)
        }
    } catch (e: Exception) {
        Log.e("ISO_TO_DATETIME", "Gagal mengonversi ISO timestamp: $e")
        null
    }
}


fun daysRemaining(endDate: String): Long {
    return try {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = Calendar.getInstance()
        val end = Calendar.getInstance()

        val endDateParsed = formatter.parse(endDate)
        if (endDateParsed != null) {
            end.time = endDateParsed

            val diffInMillis = end.timeInMillis - today.timeInMillis
            diffInMillis / (1000 * 60 * 60 * 24)
        } else {
            0L
        }
    } catch (e: Exception) {
        Log.e("DAYS_REMAINING", "Gagal menghitung sisa hari: $e")
        0L
    }
}


@Composable
fun MenuScan(
    isDarkTheme: Boolean,
    icon: Int,
    title: String,
    sizeTitle : Int = 12,
    desc: String = "",
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSystemInDarkTheme()) BlackMode else WhiteSoft)
            .border(
                width = 1.dp, color = PrimaryGreen, shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = "chilli_vision",
            modifier = Modifier
                .padding(16.dp)
                .size(35.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            TextBold(
                text = title,
                sized = sizeTitle,
                textAlign = TextAlign.Start,
                colors = if (isSystemInDarkTheme()) White else BlackMode
            )
            if (desc.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 10.sp,
                        fontFamily = FontFamily(Font(R.font.quicksand_regular)),
                        fontWeight = FontWeight.Normal,
                        color = if (isDarkTheme) PrimaryGreen else Color.Black
                    ),
                    textAlign = TextAlign.Justify
                )
            }
        }
    }
}

fun hitungSelisihHari(tanggalAwal: String, tanggalAkhir: String): Int {
    val format = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
    return try {
        val dateAwal = format.parse(tanggalAwal)
        val dateAkhir = format.parse(tanggalAkhir)

        if (dateAwal != null && dateAkhir != null) {
            val diffInMillis = abs(dateAkhir.time - dateAwal.time)
            (diffInMillis / (1000 * 60 * 60 * 24)).toInt()
        } else 0
    } catch (e: Exception) {
        Log.e("HITUNG_SELISIH", "Gagal parsing tanggal: $e")
        0
    }
}

fun hitungHariMenujuTanggal(tanggalAkhir: String): Int {
    val format = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
    return try {
        val endDate = format.parse(tanggalAkhir)
        val today = Date()

        if (endDate != null) {
            val diffInMillis = endDate.time - today.time
            (diffInMillis / (1000 * 60 * 60 * 24)).toInt()
        } else 0
    } catch (e: Exception) {
        Log.e("HITUNG_MENUJU", "Gagal parsing tanggal: $e")
        0
    }
}
