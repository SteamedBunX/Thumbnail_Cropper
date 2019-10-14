package com.steamedbunx.android.thumbnailcropper.ui

import android.net.Uri
import com.steamedbunx.android.thumbnailcropper.ui.main.ImageModel

class Util private constructor(){
     companion object{
         private val instance = Util()

         fun getInstence(): Util{
             return instance
         }

         fun loadImageFromInternalStorage(): List<ImageModel>{
             return emptyList()
         }

         fun removeImageFromInternalStorage(uri: Uri){

         }
     }
 }