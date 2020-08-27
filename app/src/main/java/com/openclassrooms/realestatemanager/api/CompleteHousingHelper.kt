package com.openclassrooms.realestatemanager.api

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.openclassrooms.realestatemanager.models.CompleteHousing

class CompleteHousingHelper
{
    companion object
    {
        private const val COLLECTION_NAME = "completeHousings"

        private fun getCollectionFirestore() : CollectionReference
        {
            return FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
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