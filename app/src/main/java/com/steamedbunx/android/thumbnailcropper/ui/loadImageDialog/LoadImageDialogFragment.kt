package com.steamedbunx.android.thumbnailcropper.ui.loadImageDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

import com.steamedbunx.android.thumbnailcropper.R

class LoadImageDialogFragment : DialogFragment() {

    companion object {
        @JvmStatic
        fun newInstance() =
            LoadImageDialogFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.load_image_dialog_fragment, container, false)
    }
}
