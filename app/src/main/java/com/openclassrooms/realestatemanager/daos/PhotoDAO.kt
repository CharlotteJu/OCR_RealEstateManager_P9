package com.openclassrooms.realestatemanager.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.openclassrooms.realestatemanager.models.Housing
import com.openclassrooms.realestatemanager.models.Photo

@Dao
interface PhotoDAO
{
    @Query("SELECT * FROM photo")
    fun getAllPhoto() : LiveData<List<Housing>>

    @Query("SELECT * FROM photo WHERE url =:url")
    fun getPhoto(url : String) : LiveData<Photo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createPhoto (photo: Photo) : Long

    @Update
    fun updatePhoto (photo: Photo) : Int
}