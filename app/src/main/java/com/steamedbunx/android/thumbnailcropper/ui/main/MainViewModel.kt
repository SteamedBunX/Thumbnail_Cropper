package com.steamedbunx.android.thumbnailcropper.ui.main

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    // region LiveData
    // the Uri of the image that's displayed at the top
    val _currentImage = MutableLiveData<Uri>()
    val currentImage: LiveData<Uri>
        get() = _currentImage

    // the List of all images that's in the internal storage
    // TODO: learn how to save and access files within the internal storage

    // endRegion




}
