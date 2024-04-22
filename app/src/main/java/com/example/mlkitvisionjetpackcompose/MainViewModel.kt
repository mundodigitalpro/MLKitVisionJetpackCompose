package com.example.mlkitvisionjetpackcompose

import android.app.Application
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.IOException


class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _triggerImagePicker = MutableLiveData<Boolean>()
    val triggerImagePicker: LiveData<Boolean> get() = _triggerImagePicker
    var recognizedText = mutableStateOf("")
    var imageUri = mutableStateOf<Uri?>(null)



    fun pickImage() {
        _triggerImagePicker.value = true
    }

    fun resetPickImageTrigger() {
        _triggerImagePicker.value = false
    }

    fun updateImageUri(uri: Uri?) {
        imageUri.value = uri
    }

    fun recognizeTextFromImage(uri: Uri) {
        val context = getApplication<Application>().applicationContext
        val image: InputImage
        try {
            image = InputImage.fromFilePath(context, uri)
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    recognizedText.value = visionText.text
                }
                .addOnFailureListener { e ->
                    recognizedText.value = "Error recognizing text: ${e.localizedMessage}"
                }
        } catch (e: IOException) {
            recognizedText.value = "Error loading image: ${e.localizedMessage}"
        }
    }

    fun createImageUri(): Uri? {
        val context = getApplication<Application>().applicationContext
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "captured_image_${System.currentTimeMillis()}")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }
        return context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
    }


    fun shareText() {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, recognizedText.value)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        getApplication<Application>().applicationContext.startActivity(shareIntent)
    }

}
