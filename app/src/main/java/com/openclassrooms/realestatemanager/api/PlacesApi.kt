package com.openclassrooms.realestatemanager.api

import com.openclassrooms.realestatemanager.models.PlacesPoiPOJO
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApi
{
    companion object
    {
        val retrofit : Retrofit = Retrofit.Builder()
                    .baseUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/json?")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()



    }

    @GET("")
    fun getPoiRx (@Query ("location") location : String,
                @Query ("radius") radius : Int,
                @Query ("type") type : String,
                @Query ("key") key : String) : Observable<PlacesPoiPOJO>

    @GET("")
    suspend fun getPoiCoroutine (@Query ("location") location : String,
                                        @Query ("radius") radius : Int,
                                        @Query ("type") type : String,
                                        @Query ("key") key : String) : Response<PlacesPoiPOJO>



}