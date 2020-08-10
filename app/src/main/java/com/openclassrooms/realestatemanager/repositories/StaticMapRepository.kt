package com.openclassrooms.realestatemanager.repositories

import com.openclassrooms.realestatemanager.api.GoogleApi


class StaticMapRepository
{
    companion object{
        const val ZOOM = 15
        const val SIZE = "400x400"
    }

    suspend fun getStaticMap(location: String, key : String) : String
    {
        val staticMap = GoogleApi.retrofit.create(GoogleApi::class.java)

        return staticMap.getStaticMapRx(location, ZOOM, SIZE, key)
    }

}