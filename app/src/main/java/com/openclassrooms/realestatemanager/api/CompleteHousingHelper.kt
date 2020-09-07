package com.openclassrooms.realestatemanager.api

import androidx.core.net.toUri
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.openclassrooms.realestatemanager.models.CompleteHousing
import com.openclassrooms.realestatemanager.models.Photo
import com.openclassrooms.realestatemanager.utils.FIREBASE_STORAGE_REF
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class CompleteHousingHelper
{
    companion object
    {
        private const val COLLECTION_NAME = "completeHousings"

        private fun getCollectionFirestore() : CollectionReference
        {
            return FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
        }

        private suspend fun <T> Task<T>.await(): T? {
            if (isComplete) {
                val e = exception
                return if (e == null) {
                    if (isCanceled) {
                        throw CancellationException(
                                "Task $this was cancelled normally.")
                    } else {
                        this.result
                    }
                } else {
                    throw e
                }
            }

            return suspendCancellableCoroutine { cont ->
                addOnCompleteListener {
                    val e = exception
                    if (e == null) {
                        if (isCanceled) cont.cancel() else cont.resume(result)
                    } else {
                        cont.resumeWithException(e)
                    }
                }
            }
        }

        suspend fun getCompleteHousingListFromFirestore() : QuerySnapshot?
        {
            return try
            {
                val firestore = Firebase.firestore
                firestore.collection(COLLECTION_NAME).get().await()
            }
            catch (e : Exception)
            {
                null
            }
        }

        suspend fun pushPhotoOnFirebaseStorage(photo: Photo) : UploadTask.TaskSnapshot?
        {
            return try {
                val ref = FirebaseStorage.getInstance().getReference(FIREBASE_STORAGE_REF)
                return ref.child(photo.uri).putFile(photo.uri.toUri()).await()
            }
            catch (e : Exception)
            {
                null
            }
        }

        /*fun getListCompleteHousingFromFirestore() : Query
        {
            return this.getCollectionFirestore()
        }*/


        fun createCompleteHousingInFirestore(completeHousing: CompleteHousing) : Task<Void>
        {
            return this.getCollectionFirestore().document(completeHousing.housing.ref).set(completeHousing)
        }





    }
}