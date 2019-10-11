package com.steamedbunx.android.thumbnailcropper.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.steamedbunx.android.thumbnailcropper.R
import com.steamedbunx.android.thumbnailcropper.ui.loadImageDialog.LoadImageDialogFragment
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        button_load_new_image.setOnClickListener { showLoadImageDialog() }
        // TODO: Use the ViewModel
    }

    fun showLoadImageDialog(){
        val dialog = LoadImageDialogFragment.newInstance()
        dialog.show(requireFragmentManager(), "load_image_dialog_fragment")
    }

}
