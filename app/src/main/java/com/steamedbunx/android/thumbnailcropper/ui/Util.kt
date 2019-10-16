package com.steamedbunx.android.thumbnailcropper.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.net.toFile
import com.steamedbunx.android.thumbnailcropper.ui.main.ImageModel
import java.io.File
import java.io.FileFilter
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

@Suppress("DEPRECATION")
class Util private constructor() {
    companion object {
        private val instance = Util()

        fun getInstence(): Util {
            return instance
        }
    }

    fun loadAllPngUriFromInternalStorage(context: Context): List<Uri> {
        val root = context.filesDir
        // get only the PNG files via  filter
        val pngFilter = object: FileFilter{
            override fun accept(pathname: File?): Boolean {
                //return (pathname?.path?.endsWith(".png") == true)
                return true
            }
        }
        // get Uri out of the files
        return root.listFiles(pngFilter).
            mapNotNull{ if(it!=null) Uri.fromFile(it) else null}
    }

    fun storeImageToInternalStorage(uri: Uri, context: Context) : Uri{
        val file = File(context.filesDir, createFileName())
        val outputFileStream =
            FileOutputStream(file)
        val outputBitmap = getBitmapFromUri(uri, context)
        outputBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputFileStream)
        outputFileStream.close()
        return Uri.fromFile(file)
    }

    private fun createFileName(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now().toString() + ".png"
        } else {
            getCurrentDateTime().toString("yyyy-MM-dd-HH-mm-ss") + ".png"
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
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(File(uri.path?:"")))
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
    }

    fun deleteImage(uri: Uri){
        uri.toFile().delete()
    }
}
