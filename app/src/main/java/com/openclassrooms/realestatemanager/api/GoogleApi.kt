package com.openclassrooms.realestatemanager.api

import com.openclassrooms.realestatemanager.models.PlacesPoiPOJO
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

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

    /*@GET("place/nearbysearch/json?")
    fun getPoiRx (@Query ("location") location : String,
                @Query ("radius") radius : Int,
                @Query ("type") type : String,
                @Query ("key") key : String) : Call<PlacesPoiPOJO>*/


    @GET("place/nearbysearch/json?")
    suspend fun getPoiCoroutine (@Query ("location") location : String,
                                        @Query ("radius") radius : Int,
                                        @Query ("type") type : String,
                                        @Query ("key") key : String) : PlacesPoiPOJO



    @GET("staticmap?")
    suspend fun getStaticMapRx(@Query ("center") address : String,
                        @Query("zoom") zoom : Int,
                        @Query("size") size : String,
                        @Query("key") key : String)  : String

}