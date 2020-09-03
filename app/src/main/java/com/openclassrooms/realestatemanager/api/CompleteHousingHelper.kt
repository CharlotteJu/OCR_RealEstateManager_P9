package com.openclassrooms.realestatemanager.api

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.openclassrooms.realestatemanager.models.CompleteHousing
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
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

        suspend fun testGetFirestore() : QuerySnapshot?
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

        suspend fun testCreateFirestore(completeHousing: CompleteHousing) : Void?
        {
            return try
            {
                val firestore = Firebase.firestore
                firestore.collection(COLLECTION_NAME).document(completeHousing.housing.ref).set(completeHousing).await()
            }
            catch (e : Exception)
            {
                null
            }

        }

        fun getListCompleteHousingFromFirestore() : Query
        {
            return this.getCollectionFirestore()
        }

        fun getCompleteHousingFromFirestore(completeHousing: CompleteHousing) : Task<DocumentSnapshot>
        {
            return this.getCollectionFirestore().document(completeHousing.housing.ref).get()
        }

        fun createCompleteHousingFromFirestore(completeHousing: CompleteHousing) : Task<Void>
        {
            return this.getCollectionFirestore().document(completeHousing.housing.ref).set(completeHousing)
        }

        fun deleteCompleteHousingFromFirestore(completeHousing: CompleteHousing) : Task<Void>
        {
            return this.getCollectionFirestore().document(completeHousing.housing.ref).delete()
        }



    }
}