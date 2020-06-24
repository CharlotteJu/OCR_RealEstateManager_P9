package com.openclassrooms.realestatemanager.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.openclassrooms.realestatemanager.models.Housing
import com.openclassrooms.realestatemanager.models.Photo

@Dao
interface PhotoDAO
{
    @Query("SELECT * FROM photo")
    fun getAllPhoto() : LiveData<List<Photo>>

    @Query("SELECT * FROM photo WHERE housing_reference =:reference")
    fun getPhotoFromHousing(reference : String) : LiveData<Photo>

    @Query("SELECT * FROM photo WHERE uri =:uri")
    fun getPhotoFromUri(uri : String) : LiveData<Photo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createPhoto (photo: Photo) : Long

    @Update
    fun updatePhoto (photo: Photo) : Int
}