package com.steamedbunx.android.thumbnailcropper.ui.loadImageDialog

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SharedViewModel(application: Application): AndroidViewModel(application){

    // the place holder image

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
    private val _isImageStored = MutableLiveData<Boolean>()
    val isImageNewlyStored: LiveData<Boolean>
        get() = _isImageStored

    // if the user had performed loading at least once since this dialog was shown
    // if it's false, the placeHolder image will be displayed instead
    private val _isImageNewlyLoaded = MutableLiveData<Boolean>()
    val isImageNewlyLoaded: LiveData<Boolean>
        get() = _isImageNewlyLoaded

    // endregion

    // region function
    // store the image and set isImageNewlyLoaded to true
    fun load(){
        _isImageNewlyLoaded.value = true
    }

    fun store(){
        _isImageStored.value = true
    }


    // should be called upon dialog's dismiss
    fun resetImageToPlaceHolder(){
        _isImageNewlyLoaded.value = false
    }

    fun setImageBitmap(newBitmap: Bitmap) {
        _isImageNewlyLoaded.value = true
        _imageLoaded.value = newBitmap
    }

    // endregion
}