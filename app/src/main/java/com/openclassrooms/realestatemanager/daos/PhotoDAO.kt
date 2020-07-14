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
    fun getPhotoListFromHousing(reference : String) : LiveData<List<Photo>>

    @Query("SELECT * FROM photo WHERE uri =:uri")
    fun getPhotoFromUri(uri : String) : LiveData<Photo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createPhoto (photo: Photo) : Long

    @Update
    suspend fun updatePhoto (photo: Photo) : Int

    @Delete
    suspend fun deletePhoto (photo: Photo)
}