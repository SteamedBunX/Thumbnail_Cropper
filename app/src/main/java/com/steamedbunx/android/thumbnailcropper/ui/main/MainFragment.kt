package com.steamedbunx.android.thumbnailcropper.ui.main

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.steamedbunx.android.thumbnailcropper.R
import com.steamedbunx.android.thumbnailcropper.databinding.MainFragmentBinding
import com.steamedbunx.android.thumbnailcropper.ui.loadImageDialog.LoadImageDialogFragment
import com.steamedbunx.android.thumbnailcropper.ui.loadImageDialog.LoadImageDialogViewModel
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var binding: MainFragmentBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater, R.layout.main_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModelFactory = MainViewModelFactory()
        // initialize the viewModels
        viewModel = requireActivity().run{
            ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)}

        // Observers
        viewModel.currentImage.observe(this, Observer {
            displayCurrentSelection(it)
        })

        // set onclick listeners
        binding.buttonLoadNewImage.setOnClickListener { showLoadImageDialog() }
        displayCurrentSelection( viewModel.currentImage.value)
    }

    fun showLoadImageDialog() {
        val dialog = LoadImageDialogFragment.newInstance()
        dialog.show(requireFragmentManager(), "load_image_dialog_fragment")
    }

    private fun displayCurrentSelection(uri: Uri?) {
        if(uri != null) {
            binding.imageView.setImageURI(uri)
        }
        else{
            binding.imageView.setImageResource(R.drawable.place_holder)
        }
    }


}
