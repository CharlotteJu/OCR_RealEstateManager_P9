package com.openclassrooms.realestatemanager.utils

import android.content.Context
import android.location.Geocoder
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.Photo
import kotlinx.android.synthetic.main.item_housing.view.*
import java.io.IOException
import java.text.SimpleDateFormat
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
                val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                val date = formatter.parse(stringDate)
                return date?.time
            }

        }

        fun displayPhoto(isInternetAvailable : Boolean, photo : Photo, itemView :View, imageView: ImageView, context: Context)
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
                if (isFileExists(context, photo.uri.toUri()))
                {
                    Glide.with(itemView)
                            .load(photo.uri)
                            .apply(RequestOptions.centerCropTransform())
                            .into(imageView)
                }
                else
                {
                    imageView.setImageResource(R.drawable.ic_baseline_no_internet)
                }

            }
        }

        private fun isFileExists(context: Context, uri: Uri) : Boolean
        {
            val documentFile = DocumentFile.fromSingleUri(context, uri)
            return documentFile?.exists() ?: false
        }

    }
}