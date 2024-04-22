package com.example.mlkitvisionjetpackcompose

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState

@Composable
fun ImagePicker(viewModel: MainViewModel) {
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.updateImageUri(uri)
            viewModel.recognizeTextFromImage(uri)
        }
    }

    // Observing the LiveData to trigger the picker
    val triggerPicker = viewModel.triggerImagePicker.observeAsState()
    if (triggerPicker.value == true) {
        imagePickerLauncher.launch("image/*")
        viewModel.resetPickImageTrigger() // Reset the trigger
    }

    Button(onClick = { viewModel.pickImage() }) {
        Text("Upload")
    }
}
