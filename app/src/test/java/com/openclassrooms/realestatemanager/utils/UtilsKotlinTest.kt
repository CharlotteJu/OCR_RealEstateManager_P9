package com.openclassrooms.realestatemanager.utils

import android.content.Context
import android.location.Geocoder
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.openclassrooms.realestatemanager.R
import org.junit.Assert.*
import org.junit.Test



class UtilsKotlinTest
{
   /* @Test
    fun getGeocoderAddress_Success()
    {
        val context = mock(Context::class.java)
        val geocoder = mock(Geocoder::class.java)



        assertEquals("48.837127685546875,2.230478286743164",
                UtilsKotlin.getGeocoderAddress("38 Rue de Bellevue, 92100, Boulogne-Billancourt", context))
    }*/

    @Test
    fun getListTypePoi_Success()
    {
        val typeList = listOf("restaurant", "subway", "school", "park",
                "store", "museum", "doctor", "bank", "airport",
                "bar", "hospital", "gym", "spa", "train_station")
        val context = mock<Context>()
        {
            on(it.resources.getStringArray(R.array.type_poi).asList()) doReturn typeList
        }

        /*when(context.resources.getStringArray(R.array.type_poi).asList())
                .thenReturn(typeList)*/

        assertTrue(typeList == UtilsKotlin.getListTypePoi(context))
    }

}