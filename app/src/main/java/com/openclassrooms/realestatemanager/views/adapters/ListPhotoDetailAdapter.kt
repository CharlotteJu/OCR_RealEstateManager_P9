package com.openclassrooms.realestatemanager.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.Photo
import com.openclassrooms.realestatemanager.utils.UtilsKotlin
import kotlinx.android.synthetic.main.item_photo_detail.view.*

class ListPhotoDetailAdapter(private var photoList : List<Photo>, private val isInternetAvailable : Boolean) : RecyclerView.Adapter<ListPhotoDetailAdapter.ListPhotoViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPhotoViewHolder
    {
        return ListPhotoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_photo_detail, parent, false), this.isInternetAvailable)
    }

    override fun onBindViewHolder(holder: ListPhotoViewHolder, position: Int)
    {
        val photo = this.photoList[position]
        holder.configureDesign(photo)
    }

    override fun getItemCount(): Int = this.photoList.size

    class ListPhotoViewHolder (itemView : View, private val isInternetAvailable: Boolean) : RecyclerView.ViewHolder(itemView)
    {
        fun configureDesign(photo : Photo)
        {
            UtilsKotlin.displayPhoto(isInternetAvailable, photo, itemView, itemView.item_photo_detail_image)

            if (photo.description != null)
            {
                photo.description.let {itemView.item_photo_detail_description.text = it }
            }
            else
            {
                itemView.item_photo_detail_description.visibility = View.GONE
            }
        }
    }

}