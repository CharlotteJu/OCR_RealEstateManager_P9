package com.openclassrooms.realestatemanager.api

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.openclassrooms.realestatemanager.models.EstateAgent
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Communication class with Firebase about [EstateAgent]
 */
class EstateAgentHelper
{
    companion object {
        private const val COLLECTION_NAME = "estateAgent"

        private fun getCollectionFirestore(): CollectionReference {
            return FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
        }

        suspend fun getEstateAgentListFromFirestore(): QuerySnapshot? {
            return try {
                val firestore = Firebase.firestore
                firestore.collection(COLLECTION_NAME).get().await()
            } catch (e: Exception) {
                null
            }
        }

        fun createEstateAgentListInFirestore(estateAgent: EstateAgent): Task<Void> {
            return this.getCollectionFirestore().document(estateAgent.lastName).set(estateAgent)
        }

        /**
         * Generated method to use coroutine
         */
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
    }
}