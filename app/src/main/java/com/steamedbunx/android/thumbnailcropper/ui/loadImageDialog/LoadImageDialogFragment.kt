package com.steamedbunx.android.thumbnailcropper.ui.loadImageDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.steamedbunx.android.thumbnailcropper.R
import com.steamedbunx.android.thumbnailcropper.ui.main.MainViewModel
import com.steamedbunx.android.thumbnailcropper.ui.main.MainViewModelFactory
import kotlinx.android.synthetic.main.load_image_dialog_fragment.*

class LoadImageDialogFragment : DialogFragment() {

    companion object {
        @JvmStatic
        fun newInstance() =
            LoadImageDialogFragment()
    }

    lateinit var sharedViewModel:SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.load_image_dialog_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModelFactory = MainViewModelFactory(requireActivity().application)
        // initialize the viewModels
        sharedViewModel = requireActivity().run {
            ViewModelProviders.of(this, viewModelFactory).get(SharedViewModel::class.java)
        }
        sharedViewModel.resetImageToPlaceHolder()
        sharedViewModel.imageLoaded.observe(this, Observer {
            imageView_dialog_fragment.setImageBitmap(it)
        })
    }
}
