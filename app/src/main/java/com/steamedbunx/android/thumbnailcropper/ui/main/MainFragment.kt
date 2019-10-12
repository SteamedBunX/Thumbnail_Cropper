package com.steamedbunx.android.thumbnailcropper.ui.main

import android.app.Application
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.steamedbunx.android.thumbnailcropper.R
import com.steamedbunx.android.thumbnailcropper.ui.loadImageDialog.LoadImageDialogFragment
import com.steamedbunx.android.thumbnailcropper.ui.loadImageDialog.SharedViewModel
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModelFactory = MainViewModelFactory(requireActivity().application)
        // initialize the viewModels
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        sharedViewModel = requireActivity().run {
            ViewModelProviders.of(this, viewModelFactory).get(SharedViewModel::class.java)
        }
        sharedViewModel.resetImageToPlaceHolder()

        // set onclick listeners
        button_load_new_image.setOnClickListener { showLoadImageDialog() }
    }

    fun showLoadImageDialog() {
        val dialog = LoadImageDialogFragment.newInstance()
        dialog.show(requireFragmentManager(), "load_image_dialog_fragment")
    }

}
