package com.steamedbunx.android.thumbnailcropper.ui.loadImageDialog

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
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
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener

import com.steamedbunx.android.thumbnailcropper.R
import com.steamedbunx.android.thumbnailcropper.REQUESTCODE_CAMERA
import com.steamedbunx.android.thumbnailcropper.REQUESTCODE_GALLERY
import com.steamedbunx.android.thumbnailcropper.databinding.LoadImageDialogFragmentBinding
import com.steamedbunx.android.thumbnailcropper.ui.main.MainViewModel
import com.steamedbunx.android.thumbnailcropper.ui.main.MainViewModelFactory
import kotlinx.android.synthetic.main.load_image_dialog_fragment.*
import java.io.File

class LoadImageDialogFragment : DialogFragment() {

    companion object {
        @JvmStatic
        fun newInstance() =
            LoadImageDialogFragment()
    }

    private lateinit var binding: LoadImageDialogFragmentBinding
    lateinit var sharedViewModel:SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.load_image_dialog_fragment, container, true)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // initialize the viewModels
        val viewModelFactory = MainViewModelFactory(requireActivity().application)
        sharedViewModel = requireActivity().run {
            ViewModelProviders.of(this, viewModelFactory).get(SharedViewModel::class.java)
        }
        sharedViewModel.resetImageToPlaceHolder()
        // observer
        sharedViewModel.imageLoaded.observe(this, Observer {
            setDisplayImage()
        })
        // onClickListeners
        binding.buttonFromCamera.setOnClickListener { loadFromCamera() }
        binding.buttonFromGallery.setOnClickListener { loadFromGallery() }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        sharedViewModel.resetImageToPlaceHolder()
    }

    // region LiveDataObservers
    // refresh the the image display.
    private fun setDisplayImage(){
        if(sharedViewModel.isImageNewlyLoaded.value == true && sharedViewModel.imageLoaded.value != null){
            binding.imageViewDialogFragment.setImageBitmap(sharedViewModel.imageLoaded.value)
        }else{
            binding.imageViewDialogFragment.setImageResource(R.drawable.place_holder)
        }
    }
    // endregion

    // region OnClickListeners
    fun loadFromGallery(){
        askStoragePermission()
        if (checkStoragePermission()) {
            lunchGalleryIntent()
        }
    }

    fun loadFromCamera(){
        askCameraPermission()
        if (checkCameraPermission()) {
            lunchCameraIntent()
        }
    }

    fun requestPermissions(){
        askStoragePermission()
        if (checkStoragePermission()) {
            lunchGalleryIntent()
        }
    }
    // endregion

    // region Permission And Loading
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

    fun lunchCameraIntent() {
//        Snackbar.make(requireView(), "Camera Intent Lunch here", Snackbar.LENGTH_LONG).show()
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, REQUESTCODE_CAMERA)
        }
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

    fun lunchGalleryIntent() {
//        Snackbar.make(requireView(), "Gallery Intent Lunch here", Snackbar.LENGTH_LONG).show()
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, REQUESTCODE_GALLERY)
        }
    }
    // endregion

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUESTCODE_CAMERA -> updateBitmapFromCamera(data?.extras?.get("data") as Bitmap)
                REQUESTCODE_GALLERY -> updateBitmapFromGallery(data?.data as Uri)
                else -> return
            }
        }
    }

    fun updateBitmapFromCamera(newBitmap: Bitmap?) {
        if (newBitmap != null) {
            sharedViewModel.setImageBitmap(newBitmap)
        }
    }

    fun updateBitmapFromGallery(newImageUri: Uri) {
        if (newImageUri != null) {
            if (android.os.Build.VERSION.SDK_INT >= 29) {
                var bitmap =
                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(File(newImageUri.path)))
                sharedViewModel.setImageBitmap(bitmap)
            } else {
                var bitmap =
                    MediaStore.Images.Media.getBitmap(requireContext().contentResolver, newImageUri)
                sharedViewModel.setImageBitmap(bitmap)
            }
        }
    }
}
