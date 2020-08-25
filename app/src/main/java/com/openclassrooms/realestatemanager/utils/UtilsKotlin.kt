package com.openclassrooms.realestatemanager.utils

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.R
import java.io.IOException
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
    }
}