package com.openclassrooms.realestatemanager.repositories

import com.openclassrooms.realestatemanager.api.GoogleApi
import com.openclassrooms.realestatemanager.models.PlacesPoiPOJO

/**
 * Repository of [PlacesPoiPOJO]
 */
class PlacesPoiRepository
{
    suspend fun getPoiFromPlaces(location : String, radius : Int, key : String ) : PlacesPoiPOJO
    {

        val placesApi = GoogleApi.retrofit.create(GoogleApi::class.java)

        return placesApi.getPoiCoroutine(location, radius, key)
    }


}