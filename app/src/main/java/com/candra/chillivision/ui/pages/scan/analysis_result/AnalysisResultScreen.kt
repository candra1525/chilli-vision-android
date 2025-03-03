package com.candra.chillivision.ui.pages.scan.analysis_result

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.candra.chillivision.R
import com.candra.chillivision.component.AnimatedLoading
import com.candra.chillivision.component.HeaderComponent
import com.candra.chillivision.component.SweetAlertComponent
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.component.TextRegular
import com.candra.chillivision.data.vmf.ViewModelFactory
import com.candra.chillivision.ui.theme.PrimaryGreen
import com.candra.chillivision.ui.theme.Red

@Composable
fun AnalysisResultScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AnalysisResultScreenViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    )
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var isLoading by remember { mutableStateOf(false) }
    val url =
        "https://plus.unsplash.com/premium_photo-1676637656166-cb7b3a43b81a?q=80&w=2664&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D";

    Column(modifier = modifier.fillMaxWidth()) {
        HeaderComponent(
            text = "Hasil Analisis",
            modifier = modifier,
            navController = navController,
            icon = ImageVector.vectorResource(id = R.drawable.outline_save_alt_24),
            iconColor = PrimaryGreen,
            fontSized = 18,
            onIconClick = {
                SweetAlertComponent(
                    context = context,
                    title = "Berhasil",
                    contentText = "Hasil analisis berhasil disimpan pada riwayat",
                    type = "success",
                    confirmYes = {

                    }
                )
            },
        )


        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimatedLoading(modifier = Modifier.size(120.dp))
                    TextBold(
                        text = "Sedang memuat...",
                        sized = 14,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .imePadding()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp, 0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Content
                    ImageAnalysis(linkImage = url)
                    Spacer(modifier = Modifier.padding(8.dp))
                    TextBold(
                        text = "Title Analysis",
                        sized = 24,
                        colors = PrimaryGreen,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.padding(8.dp))

                    TextRegular(
                        text = "Lorem ipsum dolor sit amet consectetur, adipisicing elit. Provident delectus iusto sint quidem sit, dolores exercitationem aspernatur maxime eius doloribus quam doloremque, libero autem itaque laudantium iste enim unde? Qui quas itaque impedit error in consequatur obcaecati corrupti, ad atque at distinctio laborum magni non totam, eligendi iure quod accusamus numquam. Nostrum, consequuntur. Fugit ducimus odio est, soluta iure tempore commodi aspernatur minima veniam, porro animi perferendis, tempora nihil provident exercitationem? Fugit excepturi quod, tenetur molestiae eligendi ipsam! Itaque voluptatum laboriosam, esse hic quam maxime repudiandae odit atque adipisci, nulla ipsa repellat maiores doloremque quod fugiat, molestias amet pariatur ad laudantium reiciendis animi explicabo totam necessitatibus. Repudiandae, reprehenderit. Fuga tenetur doloribus qui architecto rem quam explicabo, vero, voluptates dolore eaque sit necessitatibus. Aperiam laudantium cum cumque similique, veritatis dignissimos incidunt explicabo culpa, accusantium quidem nobis. Voluptate nisi magnam eaque, nemo, eveniet quia accusamus omnis quis mollitia ipsa cum. Culpa aliquid sunt, nesciunt debitis tempore iste veniam necessitatibus atque ea? Officia in dicta aspernatur pariatur ullam et mollitia! Optio quo blanditiis, sit eligendi modi alias reiciendis, ipsam, dolores eaque aut ullam explicabo fugiat in obcaecati fuga distinctio tempore veritatis. Maiores ratione esse voluptatum labore est neque voluptas, facere reiciendis sequi velit.",
                        textAlign = TextAlign.Justify,
                        sized = 16
                    )
                }
            }
        }
    }


}

@Composable
fun ImageAnalysis(modifier: Modifier = Modifier, linkImage: String = null.toString()) {
    AsyncImage(
        model = linkImage,
        contentDescription = "Subscription Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .padding(8.dp)
    )
}