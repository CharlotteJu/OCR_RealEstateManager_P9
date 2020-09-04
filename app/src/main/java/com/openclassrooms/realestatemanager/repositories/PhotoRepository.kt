package com.openclassrooms.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.daos.AddressDAO
import com.openclassrooms.realestatemanager.daos.PhotoDAO
import com.openclassrooms.realestatemanager.models.Photo
import kotlinx.coroutines.coroutineScope

/**
 * Repository of [PhotoDAO]
 */
class PhotoRepository (private val photoDAO: PhotoDAO)
{
    fun getAllPhoto() : LiveData<List<Photo>> = this.photoDAO.getAllPhoto()

    fun getPhotoListFromHousing(reference : String): LiveData<List<Photo>> = this.photoDAO.getPhotoListFromHousing(reference)

    fun getPhotoFromUri(uri : String) : LiveData<Photo> = this.photoDAO.getPhotoFromUri(uri)

    suspend fun createPhoto(photo: Photo) = this.photoDAO.createPhoto(photo)

    suspend fun updatePhoto (photo: Photo) = this.photoDAO.updatePhoto(photo)

    suspend fun deletePhoto (photo: Photo) = this.photoDAO.deletePhoto(photo)

    suspend fun updatePhotoFirebase (photo: Photo) = coroutineScope { photoDAO.updatePhoto(photo) }

}