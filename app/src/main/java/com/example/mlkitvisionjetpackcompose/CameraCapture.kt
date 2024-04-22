package com.example.mlkitvisionjetpackcompose

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraCapture(viewModel: MainViewModel) {
    val context = LocalContext.current
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    val uri = remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            uri.value?.let {
                viewModel.updateImageUri(it)
                viewModel.recognizeTextFromImage(it)
            }
        } else {
            Toast.makeText(context, "Image capture failed. Please try again.", Toast.LENGTH_SHORT).show()
        }
    }

    // Prepara el URI antes de capturar la imagen
    Button(onClick = {
        if (cameraPermissionState.status.isGranted) {
            val newUri = viewModel.createImageUri()
            uri.value = newUri
            newUri?.let { safeUri ->
                cameraLauncher.launch(safeUri)
            }
        } else {
            Toast.makeText(context, "Camera permission is required to use this feature.", Toast.LENGTH_SHORT).show()
        }
    }) {
        Text("Capture")
    }

    // Solicitar permisos de cámara al cargar el composable
    LaunchedEffect(key1 = true) {
        cameraPermissionState.launchPermissionRequest()
    }
}

