package com.openclassrooms.realestatemanager.views.adapters

import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.Photo
import kotlinx.android.synthetic.main.item_photo.view.*

class ListPhotoAdapter(private val photoList : List<Photo>) : RecyclerView.Adapter<ListPhotoAdapter.ListPhotoViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPhotoViewHolder
    {
        return ListPhotoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false))
    }

    override fun onBindViewHolder(holder: ListPhotoViewHolder, position: Int) {
        val photo = this.photoList[position]
        holder.configureDesign(photo)
    }

    override fun getItemCount(): Int = this.photoList.size

    class ListPhotoViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        fun configureDesign(photo : Photo)
        {
            photo.uri.let {
                Glide.with(itemView)
                        .load(it)
                        .apply(RequestOptions.centerCropTransform())
                        .into(itemView.item_photo_image)
            }

            if (photo.description != null)
            {
                photo.description.let {itemView.item_photo_description.text = it }
            }
            else
            {
                itemView.item_photo_description.visibility = View.GONE
            }
        }
    }

}