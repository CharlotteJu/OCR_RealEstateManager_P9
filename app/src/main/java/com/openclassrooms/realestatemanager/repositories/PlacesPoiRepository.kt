package com.openclassrooms.realestatemanager.repositories

import com.openclassrooms.realestatemanager.api.PlacesApi
import com.openclassrooms.realestatemanager.models.PlacesPoiPOJO
import retrofit2.Response
import retrofit2.create

class PlacesPoiRepository
{
    suspend fun test(location : String, type : String, radius : Int, key : String ) : Response<PlacesPoiPOJO>
    {

        val placesApi = PlacesApi.retrofit.create(PlacesApi::class.java)

        return placesApi.getPoiCoroutine(location, radius, type, key)
    }

}