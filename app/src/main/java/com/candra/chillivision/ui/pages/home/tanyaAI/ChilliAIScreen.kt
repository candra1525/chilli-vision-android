package com.candra.chillivision.ui.pages.home.tanyaAI

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.candra.chillivision.R
import com.candra.chillivision.component.rememberImeState
import com.candra.chillivision.data.model.ChatModel
import com.candra.chillivision.data.vmf.ViewModelFactory
import com.candra.chillivision.ui.theme.PrimaryGreen


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
    val imeState = rememberImeState()
    val scrollState = rememberScrollState()
    LaunchedEffect (key1 = imeState.value) {
        if (imeState.value) {
            scrollState.animateScrollTo(0)
        }
    }

    val conversation = viewModel.conversation.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()

    Box(modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 32.dp) // Hindari ketumpukan di bawah
        ) {
            // ✅ Header tetap terlihat dan tidak tertutup
            HeaderChilliAI(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, start = 32.dp, end = 32.dp),
                navController = navController
            )
        }

        Column (modifier = Modifier.padding(top = 100.dp)) {
            Box(
                modifier = Modifier
                    .weight(1f) // Menggunakan Box agar bisa mengatur padding lebih fleksibel
                    .padding(top = 16.dp) // Beri jarak antara header dan chat
            ) {
                ChatScreen(
                    model = ChatModel(
                        messages = conversation.value,
                        addressee = ChatModel.Author.bot,
                    ),
                    isLoading = isLoading.value,
                    onSendChatClickListener = { },
                    modifier = Modifier.fillMaxSize()
                )
            }

        }
            ChatBox(
                onSendChatClickListener = { msg ->
                    viewModel.sendChat(msg)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                ,
            )
    }


}


@Composable
private fun HeaderChilliAI(modifier: Modifier = Modifier, navController: NavController) {
    Box(
        modifier = modifier
            .fillMaxWidth()

    ) {
        Image(
            painter = painterResource(id = R.drawable.back_arrow),
            contentDescription = "back header ${R.string.app_name}",
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    navController.popBackStack()
                },
        )

        Text(
            text = "Chilli AI", style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                color = PrimaryGreen,
                textAlign = TextAlign.Center
            ), modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        )


    }
}