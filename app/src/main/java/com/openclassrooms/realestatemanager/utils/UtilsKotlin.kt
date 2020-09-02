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
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.R
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

        fun convertStringToDateTEST(stringDate : String?) : Date?
        {
            return if (stringDate.equals("null") || stringDate == null) null
            else
            {
                val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.US) //TODO : Obligé de mettre la locale ?
                formatter.parse(stringDate)
            }

        }

        fun convertStringToDate(stringDate : String?) : Date?
        {
            return if (stringDate.equals("null") || stringDate == null) null
            else
            {
                val formatter = SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.US) //TODO : Obligé de mettre la locale ?
                formatter.parse(stringDate)
            }

        }

        fun getDateAndHoursOfToday() : String
        {
            val date = Calendar.getInstance().time
            val formatter = SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.US)
            return formatter.format(date)
        }

    }
}