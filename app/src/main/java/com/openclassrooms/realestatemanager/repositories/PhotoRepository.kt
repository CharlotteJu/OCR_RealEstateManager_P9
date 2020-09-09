package com.openclassrooms.realestatemanager.repositories

import com.openclassrooms.realestatemanager.daos.PhotoDAO
import com.openclassrooms.realestatemanager.models.Photo

/**
 * Repository of [PhotoDAO]
 */
class PhotoRepository (private val photoDAO: PhotoDAO)
{
    suspend fun createPhoto(photo: Photo) = this.photoDAO.createPhoto(photo)

    suspend fun updatePhoto (photo: Photo) = this.photoDAO.updatePhoto(photo)

    suspend fun deletePhoto (photo: Photo) = this.photoDAO.deletePhoto(photo)

}