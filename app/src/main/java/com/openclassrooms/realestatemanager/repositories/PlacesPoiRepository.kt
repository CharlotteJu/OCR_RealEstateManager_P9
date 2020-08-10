package com.openclassrooms.realestatemanager.repositories

import com.openclassrooms.realestatemanager.api.GoogleApi
import com.openclassrooms.realestatemanager.models.PlacesPoiPOJO
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
const val RADIUS = 500
const val TYPES = "park, store, restaurant, school, subway"
class PlacesPoiRepository
{
    suspend fun test(location : String, type : String, radius : Int, key : String ) : PlacesPoiPOJO
    {

        val placesApi = GoogleApi.retrofit.create(GoogleApi::class.java)

        return placesApi.getPoiCoroutine(location, radius, type, key)
    }





}