package com.steamedbunx.android.thumbnailcropper.ui.main

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("image")
fun ImageView.setImage(item: Bitmap){
    setImageBitmap(item)
}