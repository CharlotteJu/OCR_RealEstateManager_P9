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
            /*val geocoder = Geocoder(context)
            val addressList : List<Address>?

            addressList = geocoder.getFromLocationName(address, 1)

            return if (addressList.isEmpty()) null
            else {
                val location = addressList[0]
                return "${location.latitude}, ${location.longitude}"
            }*/


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

        fun getListTypePo(context: Context) : List<String>
        {
            val list : MutableList<String> = ArrayList()

            list.add(context.getString(R.string.restaurant))
            list.add(context.getString(R.string.subway))
            list.add(context.getString(R.string.school))
            list.add(context.getString(R.string.park))
            list.add(context.getString(R.string.store))
            list.add(context.getString(R.string.museum))
            list.add(context.getString(R.string.doctor))
            list.add(context.getString(R.string.bank))
            list.add(context.getString(R.string.airport))
            list.add(context.getString(R.string.bar))
            list.add(context.getString(R.string.hospital))
            list.add(context.getString(R.string.gym))
            list.add(context.getString(R.string.spa))
            list.add(context.getString(R.string.train_station))

            return list
        }
    }
}