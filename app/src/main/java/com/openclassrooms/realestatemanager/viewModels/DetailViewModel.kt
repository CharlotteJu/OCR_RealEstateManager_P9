package com.openclassrooms.realestatemanager.viewModels

import android.content.Context
import androidx.lifecycle.*
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.openclassrooms.realestatemanager.api.CompleteHousingHelper
import com.openclassrooms.realestatemanager.models.*
import com.openclassrooms.realestatemanager.notifications.NotificationWorker
import com.openclassrooms.realestatemanager.repositories.*
import com.openclassrooms.realestatemanager.utils.ERROR_GEOCODER_ADDRESS
import com.openclassrooms.realestatemanager.utils.UtilsKotlin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * View Model to get [Housing], [Address], [Photo], [HousingEstateAgent], [HousingPoi] --> DetailFragment
 */
class DetailViewModel constructor(private val housingRepository: HousingRepository)
                                    : ViewModel()
{
    fun getCompleteHousing(reference : String) : LiveData<CompleteHousing> = this.housingRepository.getCompleteHousing(reference)
    fun getAllCompleteHousing() : LiveData<List<CompleteHousing>> = this.housingRepository.getAllCompleteHousing()
}