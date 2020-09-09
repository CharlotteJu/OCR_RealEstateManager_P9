package com.openclassrooms.realestatemanager.api

import com.openclassrooms.realestatemanager.models.PlacesPoiPOJO
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface to communicate with Google API PLACES
 */
interface GoogleApi
{
    companion object
    {
        val retrofit : Retrofit = Retrofit.Builder()
                    .baseUrl("https://maps.googleapis.com/maps/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
    }


    @GET("place/nearbysearch/json?")
    suspend fun getPoiCoroutine (@Query ("location") location : String,
                                        @Query ("radius") radius : Int,
                                        @Query ("key") key : String) : PlacesPoiPOJO

}