package com.candra.chillivision.ui.pages.analysis

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.candra.chillivision.R
import com.candra.chillivision.component.HeaderComponent
import com.candra.chillivision.component.HeaderComponentAnalysis
import com.candra.chillivision.component.HeaderComponentLoginRegister
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.component.dashedBorder
import com.candra.chillivision.ui.theme.PrimaryGreen

@Composable
fun AnalysisScreen(modifier: Modifier = Modifier, navController: NavController) {
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
            HeaderComponentAnalysis(context = context, text = "Hasil Analisis", modifier, navController)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp, 0.dp)
                    .dashedBorder(2.dp, 8.dp, PrimaryGreen),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ImageAnalysis()
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp, 0.dp),
            ) {
                Spacer(modifier = Modifier.padding(8.dp))

                // Content
                TextBold(text = "Title Analysis", sized = 24, colors = PrimaryGreen, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.padding(8.dp))

                TextBold(text = "Lorem ipsum dolor sit amet consectetur, adipisicing elit. Provident delectus iusto sint quidem sit, dolores exercitationem aspernatur maxime eius doloribus quam doloremque, libero autem itaque laudantium iste enim unde? Qui quas itaque impedit error in consequatur obcaecati corrupti, ad atque at distinctio laborum magni non totam, eligendi iure quod accusamus numquam. Nostrum, consequuntur. Fugit ducimus odio est, soluta iure tempore commodi aspernatur minima veniam, porro animi perferendis, tempora nihil provident exercitationem? Fugit excepturi quod, tenetur molestiae eligendi ipsam! Itaque voluptatum laboriosam, esse hic quam maxime repudiandae odit atque adipisci, nulla ipsa repellat maiores doloremque quod fugiat, molestias amet pariatur ad laudantium reiciendis animi explicabo totam necessitatibus. Repudiandae, reprehenderit. Fuga tenetur doloribus qui architecto rem quam explicabo, vero, voluptates dolore eaque sit necessitatibus. Aperiam laudantium cum cumque similique, veritatis dignissimos incidunt explicabo culpa, accusantium quidem nobis. Voluptate nisi magnam eaque, nemo, eveniet quia accusamus omnis quis mollitia ipsa cum. Culpa aliquid sunt, nesciunt debitis tempore iste veniam necessitatibus atque ea? Officia in dicta aspernatur pariatur ullam et mollitia! Optio quo blanditiis, sit eligendi modi alias reiciendis, ipsam, dolores eaque aut ullam explicabo fugiat in obcaecati fuga distinctio tempore veritatis. Maiores ratione esse voluptatum labore est neque voluptas, facere reiciendis sequi velit.", textAlign = TextAlign.Justify)

            }
        }
    }

}

@Composable
fun ImageAnalysis(modifier : Modifier = Modifier, linkImage : String = null.toString()){
    Image(
        painter = painterResource(id = R.drawable.upload_cloud),
        contentDescription = "camera_scan",
        modifier = Modifier.size(300.dp),
        colorFilter = ColorFilter.tint(Color.Gray)
    )
}