package com.example.kts_android_kmp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonShapes
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import ktsandroidkmp.composeapp.generated.resources.Res
import ktsandroidkmp.composeapp.generated.resources.hello_screen_img_url
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HelloScreen(
    onLoginButtonClick: () -> Unit,
    modifier: Modifier
){
    Column(
        modifier = modifier
                .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
    ){
        PrintHelloImg()

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
            text = "Подберите для себя идеальный автомобиль!",
            style = MaterialTheme.typography.headlineLarge,
            color = Color(0xFF325FBF),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                onLoginButtonClick()
            },
            shapes = ButtonShapes(
                shape = MaterialTheme.shapes.large,
                pressedShape = MaterialTheme.shapes.large,
            ),
            content = {
                Text(text = "LOGIN", style = MaterialTheme.typography.headlineMedium)
            },
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .background(MaterialTheme.colorScheme.onPrimary)
        )
    }
}

@Composable
fun PrintHelloImg() {
    val helloImageUrl = stringResource(Res.string.hello_screen_img_url)
    val helloImagePainter = rememberAsyncImagePainter(helloImageUrl)
    val helloImageState by helloImagePainter.state.collectAsState()
    when (helloImageState) {
        is AsyncImagePainter.State.Empty,
        is AsyncImagePainter.State.Loading -> {
            Box(modifier = Modifier.fillMaxWidth()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        is AsyncImagePainter.State.Error -> {
            Text(
                text = "Не удалось загрузить картинку",
                color = MaterialTheme.colorScheme.error
            )
        }
        is AsyncImagePainter.State.Success -> {
            Image(
                painter = helloImagePainter,
                contentDescription = "Hello screen image",
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(2.1f)
                    .padding(top = 70.dp, bottom = 50.dp)
            )
        }
    }
}

@Preview
@Composable
fun HelloScreenPreview(){
    Scaffold{ innerPadding,  ->
        Surface(
            modifier = Modifier.padding(innerPadding)
        ){
            val modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 20.dp,
                    end = 20.dp
                )
            HelloScreen(
                onLoginButtonClick = {},
                modifier = modifier
            )
        }
    }
}