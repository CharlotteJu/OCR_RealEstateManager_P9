package com.openclassrooms.realestatemanager.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.Photo
import com.openclassrooms.realestatemanager.utils.UtilsKotlin
import kotlinx.android.synthetic.main.item_photo_add.view.*

class ListPhotoAddAdapter (private var photoList : List<Photo>, private val onItemClickEdit: OnItemClickEdit, private val isInternetAvailable : Boolean, private val context : Context) : RecyclerView.Adapter<ListPhotoAddAdapter.ListPhotoAddViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPhotoAddViewHolder {
        return ListPhotoAddViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_photo_add, parent, false),this.onItemClickEdit, this.isInternetAvailable, this.context)
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

    class ListPhotoAddViewHolder(itemView : View, private val onItemClickEdit: OnItemClickEdit, private val isInternetAvailable : Boolean, private val context : Context) : RecyclerView.ViewHolder(itemView)
    {
        fun configureDesign(photo : Photo)
        {
            UtilsKotlin.displayPhoto(isInternetAvailable, photo, itemView, itemView.item_photo_add_image, context)

            if (photo.description != null) itemView.item_photo_add_description.text = photo.description
            else itemView.item_photo_add_description.visibility = View.GONE

            this.itemView.item_photo_add_edit_button.setOnClickListener { this.onItemClickEdit.onClickEditPhoto(adapterPosition) }
            this.itemView.item_photo_add_delete_button.setOnClickListener { this.onItemClickEdit.onClickDeletePhoto(adapterPosition) }
        }
    }


}