package com.candra.chillivision.ui.pages.home.tanyaAI

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.candra.chillivision.R
import com.candra.chillivision.component.InitialAvatar
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.component.TextRegular
import com.candra.chillivision.data.vmf.ViewModelFactory
import com.candra.chillivision.ui.theme.BlackMode
import com.candra.chillivision.ui.theme.GraySoft
import com.candra.chillivision.ui.theme.PrimaryGreen
import com.candra.chillivision.ui.theme.WhiteSoft
import kotlinx.coroutines.launch

data class ChatMessage(val text: String, val isUser: Boolean)

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

    val userPreferences = viewModel.getPreferences().collectAsState(initial = null)
    val imageUser = userPreferences.value?.image
    val fullname = userPreferences.value?.fullname

    var message by remember { mutableStateOf(TextFieldValue()) }
    var messages by remember {
        mutableStateOf(
            listOf(
                ChatMessage("Halo! Ada yang bisa saya bantu?", false),
                ChatMessage("Bagaimana cara mendeteksi penyakit cabai?", true),
                ChatMessage("Anda bisa menggunakan model YOLO untuk itu!", false),
                ChatMessage("Terima kasih!", true),
                ChatMessage("Sama-sama! 😊", false)
            )
        )
    }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

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
//                       viewModel.clearChat()
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
                    placeholder = { TextRegular(text = "💡 Tanyakan sesuatu pada Chilli AI...") },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = if (isSystemInDarkTheme()) WhiteSoft else GraySoft,
                        unfocusedContainerColor = if (isSystemInDarkTheme()) WhiteSoft else GraySoft,
                    ),
                    shape = RoundedCornerShape(16.dp),
                    textStyle = TextStyle(
                        fontFamily = FontFamily(Font(R.font.quicksand_regular)),
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (message.text.isNotBlank()) {
                            messages = messages + ChatMessage(message.text, true)

                            // Simulasi respons AI
                            messages = messages + ChatMessage(
                                "Saya sedang memproses: ${message.text}",
                                false
                            )

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
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(start = 8.dp, end = 8.dp) // Jarak dari TopAppBar
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
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage, imageUser: String, fullname: String = null.toString()) {
    val backgroundColor = if (message.isUser) Color(0xFFDCF8C6) else GraySoft
    val textColor = if (isSystemInDarkTheme()) WhiteSoft else BlackMode

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!message.isUser) {
            ImageChat(model = R.drawable.chilli_vision_logo)
            Spacer(modifier = Modifier.width(8.dp))
        }
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 24f,
                        topEnd = 24f,
                        bottomStart = if (message.isUser) 24f else 0f,
                        bottomEnd = if (message.isUser) 0f else 24f
                    )
                )
                .background(
                    color = backgroundColor,
                )
                .padding(8.dp)

        ) {
            TextRegular(
                text = message.text,
                colors = textColor
            )
        }
        if (message.isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            InitialAvatar(
                fullname = fullname,
                imageUrl = imageUser,
                size = 25.dp,
                fs = 12,
            )
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