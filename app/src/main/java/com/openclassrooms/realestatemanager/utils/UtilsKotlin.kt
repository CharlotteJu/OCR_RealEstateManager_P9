package com.openclassrooms.realestatemanager.utils

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.Build
import androidx.core.content.ContentProviderCompat.requireContext
import java.io.IOException
import java.util.*

class UtilsKotlin
{

    companion object
    {
        fun getGeocoderAddress(address: String, context: Context?): String? {
            val geocoder = Geocoder(context)
            return try
            {
                val addressList = geocoder.getFromLocationName(address, 1)
                val lat = addressList[0].latitude
                val lng = addressList[0].longitude
                "$lat,$lng"
            } catch (e: IOException) {
                e.printStackTrace()
                ERROR_GEOCODER_ADDRESS
            }

            /* KOTLIN
            val geocoder : Geocoder = Geocoder(context)
            val listGeocoder  = geocoder.getFromLocationName(housing.address.toString(), 1)
            val lat  = listGeocoder[0].latitude
            val lng = listGeocoder[0].longitude
            val location = "$lat,$lng"*/
        }

        fun getDatePickerDialog(context: Context) : String
        {
            var date = ""
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                val month1 = month+1
                val monthString = if (month1 < 10) "0$month1"
                else month1.toString()

                date = "$dayOfMonth/$monthString/$year"

                //TODO-Q : Possible de faire le return ici ?

                //housing.dateSale = Utils.getDateFormat(calendar.time) //TODO-Q : Pourquoi ça met la Date du jour ?
            }, year, month, dayOfMonth)

            datePickerDialog.show()

            return date
        }



    }




}