package com.openclassrooms.realestatemanager.api

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.openclassrooms.realestatemanager.models.CompleteHousing

class CompleteHousingHelper
{
    companion object
    {
        const val COLLECTION_NAME = "completeHousings"

        fun getCollectionFirestore() : CollectionReference
        {
            return FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
        }

        fun getCompleteHousingFromFirestore(completeHousing: CompleteHousing) : Task<DocumentSnapshot>
        {
            return CompleteHousingHelper.getCollectionFirestore().document(completeHousing.housing.ref).get()
        }
    }
}