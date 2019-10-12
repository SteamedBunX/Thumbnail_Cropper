package com.steamedbunx.android.thumbnailcropper.ui.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.steamedbunx.android.thumbnailcropper.ui.loadImageDialog.LoadImageDialogViewModel

class MainViewModelFactory: ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel() as T
        }else if(modelClass.isAssignableFrom(LoadImageDialogViewModel::class.java)){
            return LoadImageDialogViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}