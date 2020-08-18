package com.openclassrooms.realestatemanager.repositories

import com.openclassrooms.realestatemanager.api.GoogleApi
import com.openclassrooms.realestatemanager.models.PlacesPoiPOJO
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlacesPoiRepository
{
    suspend fun getPoiFromPlaces(location : String, radius : Int, key : String ) : PlacesPoiPOJO
    {

        val placesApi = GoogleApi.retrofit.create(GoogleApi::class.java)

        return placesApi.getPoiCoroutine(location, radius, key)
    }


}