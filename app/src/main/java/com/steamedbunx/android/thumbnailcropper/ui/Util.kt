package com.steamedbunx.android.thumbnailcropper.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.steamedbunx.android.thumbnailcropper.ui.main.ImageModel
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class Util private constructor() {
    companion object {
        private val instance = Util()

        fun getInstence(): Util {
            return instance
        }
    }

    fun loadImageFromInternalStorage(): List<ImageModel> {
        return emptyList()
    }

    fun storeImageToInternalStorage(uri: Uri, context: Context) {
        val outputFileStream =
            FileOutputStream(File(context.filesDir, createFileName()))
        val outputBitmap = getBitmapFromUri(uri, context)
        outputBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputFileStream)
        outputFileStream.close()
    }

    private fun createFileName(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now().toString()
        } else {
            getCurrentDateTime().toString("yyyy-MM-dd-HH-mm-ss")
        }
    }

    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    fun getBitmapFromUri(uri: Uri, context: Context): Bitmap {
        return if (Build.VERSION.SDK_INT >= 29) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(File(uri.path)))
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
    }
}
