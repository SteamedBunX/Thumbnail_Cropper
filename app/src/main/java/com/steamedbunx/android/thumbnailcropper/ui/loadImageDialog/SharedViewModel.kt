package com.steamedbunx.android.thumbnailcropper.ui.loadImageDialog

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.steamedbunx.android.thumbnailcropper.R

class SharedViewModel(application: Application): AndroidViewModel(application){

    // the place holder image
    val placeHolderImage: Bitmap

    init{
        // load the placeholder
        var drawable = application.applicationContext.getDrawable(R.drawable.place_holder) as BitmapDrawable
        placeHolderImage = drawable.bitmap
        // put the placeholder onto display
        resetImageToPlaceHolder()
    }

    // region LiveData
    // the Uri of the newly stored image
    private val _imageStored = MutableLiveData<Uri>()
    val imageStored: LiveData<Uri>
        get() = _imageStored

    // the Bitmap of currently loaded image
    private val _imageLoaded = MutableLiveData<Bitmap>()
    val imageLoaded : LiveData<Bitmap>
        get() = _imageLoaded

    // if the user has decided to store the current image
    // if it's true, the main fragment's current image display will update to imageStored.
    private val _isImageNewlyStored = MutableLiveData<Boolean>()
    val isImageNewlyStored: LiveData<Boolean>
        get() = _isImageNewlyStored

    // endregion

    // region function
    // store the image and set isImageNewlyLoaded to true
    fun store(){
        _isImageNewlyStored.value = true
    }

    fun resetImageToPlaceHolder(){
        _imageLoaded.value = placeHolderImage
    }

    // endregion
}