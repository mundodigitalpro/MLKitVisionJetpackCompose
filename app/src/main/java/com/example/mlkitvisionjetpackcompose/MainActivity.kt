package com.example.mlkitvisionjetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.mlkitvisionjetpackcompose.ui.theme.MLKitVisionJetpackComposeTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MLKitVisionJetpackComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    MLKitVisionJetpackComposeTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {

                    viewModel.imageUri.value?.let { uri ->
                        Image(
                            painter = rememberImagePainter(
                                data = uri,
                                builder = {
                                    // Apply builder configurations if needed, for example:
                                    crossfade(true)
                                    // You can add more configuration options here
                                }
                            ),
                            contentDescription = "Displayed Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .background(Color.LightGray),
                            contentScale = ContentScale.Crop
                        )
                    }

                    ResultText(text = viewModel.recognizedText.value)
                    ButtonRow(viewModel)
                }
            }
        }
    }
}



@Composable
fun ResultText(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text.ifBlank { "AQUÍ SE MOSTRARÁ EL TEXTO RECONOCIDO" },
            textAlign = TextAlign.Center,
            color = Color.Black
        )
    }
}

@Composable
fun ButtonRow(viewModel: MainViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CameraCapture(viewModel)
        ImagePicker(viewModel)
        Button(onClick = { viewModel.shareText() }) {
            Text("Share")
        }
    }
}
