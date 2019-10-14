package com.steamedbunx.android.thumbnailcropper.ui.main

import android.graphics.Bitmap
import android.net.Uri

data class ImageModel(
    val uri: Uri,
    val bitmap: Bitmap
)