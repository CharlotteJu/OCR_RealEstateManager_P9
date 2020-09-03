package com.openclassrooms.realestatemanager.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.Photo
import kotlinx.android.synthetic.main.item_photo_detail.view.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class UtilsKotlin
{

    companion object
    {
        fun getGeocoderAddress(address: String, context: Context?): String? {
            return try
            {
                val geocoder = Geocoder(context)
                val addressList = geocoder.getFromLocationName(address, 1)
                val lat = addressList[0].latitude
                val lng = addressList[0].longitude
                "$lat,$lng"
            } catch (e: IOException) {
                e.printStackTrace()
                ERROR_GEOCODER_ADDRESS
            }
        }

        fun getListTypePoi(context: Context) : List<String>
        {
            return context.resources.getStringArray(R.array.type_poi).asList()
        }

        fun convertStringToLongDate(stringDate : String?) : Long?
        {
            return if (stringDate.equals("null") || stringDate == null) null
            else
            {
                val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.US) //TODO : Obligé de mettre la locale ?
                val date = formatter.parse(stringDate)
                return date?.time
            }

        }

        fun displayPhoto(isInternetAvailable : Boolean, photo : Photo, itemView :View, imageView: ImageView)
        {
            if (isInternetAvailable && photo.url_firebase != null)
            {
                Glide.with(itemView)
                        .load(photo.url_firebase)
                        .apply(RequestOptions.centerCropTransform())
                        .into(imageView)
            }
            else
            {
                Glide.with(itemView)
                        .load(photo.uri)
                        .apply(RequestOptions.centerCropTransform())
                        .into(imageView)
            }
        }

    }
}