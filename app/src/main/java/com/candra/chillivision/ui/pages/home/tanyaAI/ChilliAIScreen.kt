package com.candra.chillivision.ui.pages.home.tanyaAI

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.candra.chillivision.R
import com.candra.chillivision.component.AnimatedLoadingChat
import com.candra.chillivision.component.InitialAvatar
import com.candra.chillivision.component.SweetAlertComponent
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.component.TextRegular
import com.candra.chillivision.data.model.ChatModel
import com.candra.chillivision.data.vmf.ViewModelFactory
import com.candra.chillivision.service.SUBSCRIPTION_MAX_USAGE_AI_PREMIUM
import com.candra.chillivision.service.SUBSCRIPTION_MAX_USAGE_AI_REGULAR
import com.candra.chillivision.ui.theme.BlackMode
import com.candra.chillivision.ui.theme.GraySoft
import com.candra.chillivision.ui.theme.PrimaryGreen
import com.candra.chillivision.ui.theme.WhiteSoft
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChilliAIScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ChilliAIScreenViewModel = viewModel(
        factory = ViewModelFactory.getInstance(
            context = LocalContext.current,
            apiType = "chatAi"
        )
    )
) {
    val isLoading = viewModel.isLoading.collectAsState()
    val userPreferences by viewModel.getPreferences()
        .collectAsState(initial = null)

    val context = LocalContext.current

    val imageUser = userPreferences?.image
    val fullname = userPreferences?.fullname

    var message by remember { mutableStateOf(TextFieldValue()) }
    val messages by viewModel.conversation.collectAsState()

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val countUsageAI by viewModel.countUsageAI.collectAsState()

    when {
        userPreferences == null -> {
            // Jika data belum dimuat, jangan menampilkan apapun
            return
        }

        userPreferences?.subscriptionName.isNullOrEmpty() -> {
            return
        }

        else -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            TextBold(
                                "Chilli AI",
                                colors = if (isSystemInDarkTheme()) WhiteSoft else BlackMode,
                                sized = 20
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = if (isSystemInDarkTheme()) WhiteSoft else BlackMode
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                viewModel.clearChat()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Reset",
                                    tint = if (isSystemInDarkTheme()) WhiteSoft else BlackMode
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
                containerColor = if (isSystemInDarkTheme()) BlackMode else WhiteSoft,
                bottomBar = {
                    Row(
                        modifier = Modifier
                            .navigationBarsPadding()
                            .imePadding()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = message,
                            onValueChange = { message = it },
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 56.dp),
                            placeholder = {
                                TextRegular(
                                    text = "💡 Tanyakan sesuatu pada Chilli AI...",
                                    colors = BlackMode
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                focusedContainerColor = GraySoft,
                                unfocusedContainerColor = GraySoft,
                            ),
                            shape = RoundedCornerShape(16.dp),
                            textStyle = TextStyle(
                                fontFamily = FontFamily(Font(R.font.quicksand_medium)),
                                color = BlackMode
                            ),

                            )

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = {
                                when (userPreferences?.subscriptionName) {
                                    "Gratis" -> {
                                        SweetAlertComponent(context = context,
                                            title = "Peringatan!",
                                            contentText = "Saat ini anda menggunakan paket gratis 🥲, silahkan upgrade ke paket berbayar untuk menggunakan layanan ini 😉",
                                            type = "warning",
                                            isCancel = true,
                                        )
                                        return@IconButton
                                    }
                                    "Paket Reguler" -> {
                                        if (countUsageAI > SUBSCRIPTION_MAX_USAGE_AI_REGULAR) {
                                            SweetAlertComponent(context = context,
                                                title = "Peringatan!",
                                                contentText = "Saat ini anda sudah mencapai batas penggunaan harian yaitu ${SUBSCRIPTION_MAX_USAGE_AI_REGULAR}x tanya AI, silahkan upgrade ke paket lebih tinggi atau gunakan lagi besok 😉",
                                                type = "warning",
                                                isCancel = true,
                                            )
                                            return@IconButton
                                        }
                                    }
                                    "Paket Premium" -> {
                                        if (countUsageAI > SUBSCRIPTION_MAX_USAGE_AI_PREMIUM) {
                                            SweetAlertComponent(context = context,
                                                title = "Peringatan!",
                                                contentText = "Saat ini anda sudah mencapai batas penggunaan harian yaitu ${SUBSCRIPTION_MAX_USAGE_AI_PREMIUM}x tanya AI, silahkan gunakan lagi besok 😉",
                                                type = "warning",
                                                isCancel = true,
                                            )
                                            return@IconButton
                                        }
                                    }
                                }

                                // Cek berapa penggunaan
//                                Toast.makeText(context, "Penggunaan AI: $countUsageAI", Toast.LENGTH_SHORT).show()

                                if (message.text.isNotBlank()) {
                                    viewModel.sendChat(message.text)

                                    coroutineScope.launch {
                                        listState.animateScrollToItem(messages.size)
                                    }

                                    message = TextFieldValue()
                                }
                            },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(color = PrimaryGreen)
                                .align(Alignment.CenterVertically)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Send,
                                contentDescription = "Send",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            ) { innerPadding ->
                // Cek apakah messages kosong
                if (messages.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            AsyncImage(
                                model = R.drawable.chilli_vision_logo,
                                contentDescription = "Image Chat",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(RoundedCornerShape(32.dp))
                                    .graphicsLayer(alpha = 0.5f)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            TextRegular(
                                text = "Yuk, mulai tanya dengan Chilli AI terkait penyakit tanaman pada cabai anda",
                                sized = 14,
                                modifier = Modifier
                                    .widthIn(max = LocalConfiguration.current.screenWidthDp.dp * 0.7f)
                                    .graphicsLayer(alpha = 0.5f),
                                textAlign = TextAlign.Center
                            )

                            TextBold(
                                text = "*Tidak disarankan untuk bertanya terkait hal yang bersifat sensitif",
                                sized = 10,
                                modifier = Modifier
                                    .widthIn(max = LocalConfiguration.current.screenWidthDp.dp * 0.7f)
                                    .graphicsLayer(alpha = 0.5f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(start = 8.dp, end = 8.dp)
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp)) // jarak dari TopAppBar
                        }
                        items(messages) { msg ->
                            ChatBubble(
                                message = msg,
                                imageUser = imageUser.toString(),
                                fullname = fullname.toString()
                            )
                        }

                        item {
                            if (isLoading.value) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    ImageChat(model = R.drawable.chilli_vision_logo)
                                    AnimatedLoadingChat(modifier = Modifier.size(50.dp))
                                    TextBold(text = "Chilli AI sedang mengetik", sized = 12)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatModel.Message, imageUser: String, fullname: String = null.toString()) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (message.isFromMe) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!message.isFromMe) {
            ImageChat(model = R.drawable.chilli_vision_logo)
            Spacer(modifier = Modifier.width(8.dp))
        }

        if (message.isFromMe) {
            Box(
                modifier = Modifier
                    .widthIn(max = LocalConfiguration.current.screenWidthDp.dp * 0.82f)
                    .clip(
                        RoundedCornerShape(
                            topStart = 8.dp,
                            topEnd = 8.dp,
                            bottomStart = 8.dp,
                            bottomEnd = 0.dp
                        )
                    )
                    .background(Color(0xFFDCF8C6))
                    .padding(8.dp)
            ) {
                TextRegular(
                    text = message.text,
                    colors = BlackMode,
                    textAlign = TextAlign.Justify
                )
            }

            Spacer(modifier = Modifier.width(8.dp)) // jarak kecil antara bubble dan avatar

            InitialAvatar(
                fullname = fullname,
                imageUrl = imageUser,
                size = 25.dp,
                fs = 12,
            )
        } else {
            Box(
                modifier = Modifier
                    .widthIn(max = LocalConfiguration.current.screenWidthDp.dp * 0.82f)
                    .clip(
                        RoundedCornerShape(
                            topStart = 8.dp,
                            topEnd = 8.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 8.dp
                        )
                    )
                    .background(GraySoft)
                    .padding(8.dp)
            ) {
                TextRegular(
                    text = message.text,
                    colors = BlackMode,
                    textAlign = TextAlign.Justify
                )
            }
        }
    }

}


@Composable
fun ImageChat(model: Any) {
    AsyncImage(
        model = model,
        contentDescription = "Image Chat",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(25.dp)
            .clip(RoundedCornerShape(32.dp))
    )
}