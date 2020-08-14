package com.openclassrooms.realestatemanager.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.Photo
import kotlinx.android.synthetic.main.item_photo_add.view.*

class ListPhotoAddAdapter (private var photoList : List<Photo>, private val onItemClickEdit: OnItemClickEdit) : RecyclerView.Adapter<ListPhotoAddAdapter.ListPhotoAddViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPhotoAddViewHolder {
        return ListPhotoAddViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_photo_add, parent, false),this.onItemClickEdit)
    }

    override fun onBindViewHolder(holder: ListPhotoAddViewHolder, position: Int) {
        val photo = this.photoList[position]
        holder.configureDesign(photo)
    }

    override fun getItemCount(): Int {
        return this.photoList.size
    }

    fun updateList(photoList: List<Photo>)
    {
        this.photoList = photoList
        this.notifyDataSetChanged()
    }

    class ListPhotoAddViewHolder(itemView : View, private val onItemClickEdit: OnItemClickEdit) : RecyclerView.ViewHolder(itemView)
    {
        fun configureDesign(photo : Photo)
        {
            photo.uri.let {
                Glide.with(itemView)
                        .load(it)
                        .apply(RequestOptions.centerCropTransform())
                        .into(itemView.item_photo_add_image)
            }

            if (photo.description != null) itemView.item_photo_add_description.text = photo.description
            else itemView.item_photo_add_description.visibility = View.GONE

            this.itemView.item_photo_add_edit_button.setOnClickListener { this.onItemClickEdit.onClickEditPhoto(adapterPosition) }
            this.itemView.item_photo_add_delete_button.setOnClickListener { this.onItemClickEdit.onClickDeletePhoto(adapterPosition) }
        }
    }


}