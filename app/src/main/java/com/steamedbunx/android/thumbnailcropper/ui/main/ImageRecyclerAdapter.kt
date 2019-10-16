package com.steamedbunx.android.thumbnailcropper.ui.main

import android.media.Image
import android.net.Uri
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.steamedbunx.android.thumbnailcropper.R
import com.steamedbunx.android.thumbnailcropper.databinding.ImageListItemBinding
import kotlinx.android.synthetic.main.image_list_item.view.*

class ImageRecyclerAdapter(val onClickListners: OnClickListeners) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<ImageModel> = ArrayList<ImageModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ImageModelViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.image_list_item,parent, false), onClickListners)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ImageModelViewHolder ->{
                holder.bind(items.get(position))
            }
        }
    }

    fun submitList(newList: List<ImageModel>){
        items = newList
    }

    class ImageModelViewHolder constructor(
        itemView: View, val onClickListners: OnClickListeners
    ) : RecyclerView.ViewHolder(itemView){

        val imageThumbnail = itemView.image_thumbnail
        val buttonDelete = itemView.button_delete

        fun bind(imageModel: ImageModel){
            imageThumbnail.setImageBitmap(imageModel.bitmap)
            itemView.setOnClickListener { onClickListners.onImageClickListener(adapterPosition) }
            buttonDelete.setOnClickListener { onClickListners.onDeleteClickListener(adapterPosition) }
        }
    }

    interface OnClickListeners{
        fun onDeleteClickListener(position: Int)
        fun onImageClickListener(position: Int)
    }

}