package com.steamedbunx.android.thumbnailcropper.ui.main

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.steamedbunx.android.thumbnailcropper.ui.Util

class MainViewModel : ViewModel() {

    val util = Util.getInstence()
    // region LiveData
    // the Uri of the image that's displayed at the top
    private val _currentImage = MutableLiveData<Uri>()
    val currentImage: LiveData<Uri>
        get() = _currentImage

    private val imageArrayList = ArrayList<ImageModel>()
    private val _imageList = MutableLiveData<List<ImageModel>>()
    val imageList: MutableLiveData<List<ImageModel>>
        get() = _imageList
    // the List of all images that's in the internal storage

    // endRegion
    fun setCurrentDisplayedImageUri(uri: Uri) {
        _currentImage.value = uri
    }

    fun updateImageList(context: Context) {
        var allUri: List<Uri> = util.loadAllPngUriFromInternalStorage(context)
        for (uri in allUri) {
            if (!imageArrayList.any { it.uri == uri }) {
                imageArrayList.add(ImageModel(uri, util.getBitmapFromUri(uri, context)))

            }
            Log.i("ITEM_LOADING", uri.path)
        }
        _imageList.value = imageArrayList.toList()
    }


}
