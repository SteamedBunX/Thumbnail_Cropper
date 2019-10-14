package com.steamedbunx.android.thumbnailcropper.ui.loadImageDialog

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener

import com.steamedbunx.android.thumbnailcropper.R
import com.steamedbunx.android.thumbnailcropper.REQUESTCODE_PICK_IMAGE
import com.steamedbunx.android.thumbnailcropper.databinding.LoadImageDialogFragmentBinding
import com.steamedbunx.android.thumbnailcropper.ui.Util
import com.steamedbunx.android.thumbnailcropper.ui.main.MainViewModel
import com.steamedbunx.android.thumbnailcropper.ui.main.MainViewModelFactory
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImage.getPickImageChooserIntent
import com.theartofdev.edmodo.cropper.CropImage.getPickImageResultUri
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class LoadImageDialogFragment : DialogFragment() {

    companion object {
        @JvmStatic
        fun newInstance() =
            LoadImageDialogFragment()
    }

    private lateinit var binding: LoadImageDialogFragmentBinding
    lateinit var viewModel: LoadImageDialogViewModel
    lateinit var mainViewModel: MainViewModel
    lateinit var util:Util

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater, R.layout.load_image_dialog_fragment, container, true
            )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        util = Util.getInstence()
        // initialize the viewModels
        val viewModelFactory = MainViewModelFactory()
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(LoadImageDialogViewModel::class.java)
        viewModel.resetImageToPlaceHolder()
        mainViewModel = requireActivity().run {
            ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        }

        // observer
        viewModel.imageLoaded.observe(this, Observer {
            setDisplayImage()
        })
        // onClickListeners
        binding.buttonLoadImage.setOnClickListener { loadImageOnClick() }
        binding.buttonStoreImage.setOnClickListener { storeImageOnClick() }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.resetImageToPlaceHolder()
    }

    // region LiveDataObservers
    // refresh the the image display.
    private fun setDisplayImage() {
        if (viewModel.isImageNewlyLoaded.value == true && viewModel.imageLoaded.value != null) {
            binding.imageViewDialogFragment.setImageBitmap(viewModel.imageLoaded.value)
        } else {
            binding.imageViewDialogFragment.setImageResource(R.drawable.place_holder)
        }
    }
    // endregion

    // region OnClickListeners
    fun storeImageOnClick() {
        askStoragePermission()
        if (checkStoragePermission()) {
            storeImage()
        }
    }

    fun loadImageOnClick() {
        askPermissions()
        if (checkPermissions()) {
            lunchImagePickingIntent()
        }
    }

    // endregion

    // region Permission And Loading
    fun askPermissions() {
        askCameraPermission()
        askStoragePermission()
    }

    fun checkPermissions(): Boolean {
        return (checkCameraPermission() && checkStoragePermission())
    }

    fun askCameraPermission() {
        val dialogPermissionListener = DialogOnDeniedPermissionListener.Builder
            .withContext(requireContext())
            .withTitle("Camera permission")
            .withMessage("Camera permission is needed to take pictures of the item")
            .withButtonText(android.R.string.ok)
            .build()
        Dexter.withActivity(requireActivity())
            .withPermission(Manifest.permission.CAMERA)
            .withListener(dialogPermissionListener)
            .check()
    }

    fun checkCameraPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun askStoragePermission() {
        val dialogPermissionListener = DialogOnDeniedPermissionListener.Builder
            .withContext(requireContext())
            .withTitle("Read Storage permission")
            .withMessage("Read Storage permission is needed to load image from gallery")
            .withButtonText(android.R.string.ok)
            .build()
        Dexter.withActivity(requireActivity())
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(dialogPermissionListener)
            .check()
    }

    fun checkStoragePermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun lunchImagePickingIntent() {
        startActivityForResult(getPickImageChooserIntent(requireContext()), REQUESTCODE_PICK_IMAGE);
    }

    // endregion

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUESTCODE_PICK_IMAGE -> lunchImageCrop(
                    getPickImageResultUri(
                        requireContext(),
                        data
                    )
                )
                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    val result = CropImage.getActivityResult(data)
                    updateBitmapFromUri(result.uri)
                }
                else -> return
            }
        }
    }

    fun updateBitmapFromUri(newImageUri: Uri) {
        if (newImageUri != null) {
            viewModel.storeImageUri(newImageUri)
            viewModel.loadImage(util.getBitmapFromUri(newImageUri, requireContext()))
        }
    }

    fun lunchImageCrop(uri: Uri) {
        CropImage.activity(uri)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setAspectRatio(300, 300)
            .start(requireContext(), this)
    }

    fun storeImage() {
        if (viewModel.isImageNewlyLoaded.value == true) {
            val uri = viewModel.imageStored.value
            if (uri != null) {
                mainViewModel.setCurrentDisplayedImageUri(uri)
                util.storeImageToInternalStorage(uri, requireContext())
            }
            this.dismiss()
        }
    }
}
