package com.steamedbunx.android.thumbnailcropper.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.ListenerUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener
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
    private lateinit var adapter: ImageRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater, R.layout.main_fragment, container, false
            )
        return binding.root
    }

    private fun initRecyclerView() {
        recycler_view_images.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        }
        adapter = ImageRecyclerAdapter(object : ImageRecyclerAdapter.OnClickListeners {
            override fun onDeleteClickListener(position: Int) {
                viewModel.removeImageAt(position)
            }

            override fun onImageClickListener(position: Int) {
                viewModel.setCurrentDisplayedImagePosition(position)
            }

        })
        binding.recyclerViewImages.adapter = adapter

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModelFactory = MainViewModelFactory()
        // initialize the viewModels
        viewModel = requireActivity().run {
            ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        }
        viewModel.updateImageList(requireContext())

        initRecyclerView()

        // Observers
        viewModel.currentImage.observe(this, Observer {
            displayCurrentSelection(it)
        })
        viewModel.imageList.observe(this, Observer {
            it?.let {
                adapter.submitList(it)
                adapter.notifyDataSetChanged()
            }
        })

        // set onclick listeners
        binding.buttonLoadNewImage.setOnClickListener { showLoadImageDialog() }
        binding.buttonSave.setOnClickListener {
            askSavePermission()
            if (checkSavePermission()) {
                viewModel.storeImageToGallery(requireContext())
                Snackbar.make(requireView(), "Image Saved ", Snackbar.LENGTH_LONG).show()
            }
        }
        displayCurrentSelection(viewModel.currentImage.value)
    }

    fun showLoadImageDialog() {
        val dialog = LoadImageDialogFragment.newInstance()
        dialog.show(requireFragmentManager(), "load_image_dialog_fragment")
    }

    private fun displayCurrentSelection(uri: Uri?) {
        if (uri != null) {
            binding.imageView.setImageURI(uri)
        } else {
            binding.imageView.setImageResource(R.drawable.place_holder)
        }
    }

    fun askSavePermission() {
        val dialogPermissionListener = DialogOnDeniedPermissionListener.Builder
            .withContext(requireContext())
            .withTitle("Permission For Write In External Storage")
            .withMessage("Storage Permission is required to save the image to gallery")
            .withButtonText(android.R.string.ok)
            .build()
        Dexter.withActivity(requireActivity())
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(dialogPermissionListener)
            .check()
    }

    fun checkSavePermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }


}
