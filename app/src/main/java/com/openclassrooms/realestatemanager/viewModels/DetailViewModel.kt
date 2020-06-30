package com.openclassrooms.realestatemanager.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.daos.HousingDAO
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.models.*
import com.openclassrooms.realestatemanager.repositories.*
import java.util.concurrent.Executor

/**
 * View Model to get [Housing], [Address], [Photo], [HousingEstateAgent], [HousingPoi] --> DetailFragment
 */
class DetailViewModel constructor(private val housingRepository: HousingRepository,
                                  private val addressRepository: AddressRepository,
                                  private val photoRepository: PhotoRepository,
                                  private val housingEstateAgentRepository: HousingEstateAgentRepository,
                                  private val housingPoiRepository: HousingPoiRepository)
                                    : ViewModel()
{
    lateinit var address : LiveData<Address>
    lateinit var photoList : LiveData<List<Photo>>
    lateinit var poiList : LiveData<List<HousingPoi>>
    lateinit var estateAgentList : LiveData<List<HousingEstateAgent>>
    lateinit var housing : LiveData<Housing>

    fun getHousing(reference : String) : LiveData<Housing>
    {
        this.housing = this.housingRepository.getHousing(reference)
        this.getAllInfoForOneHousing(reference)

        housing.value!!.photoList = photoList.value
        housing.value!!.address = address.value
        housing.value!!.estateAgentList = estateAgentList.value
        housing.value!!.poiList = poiList.value

        return housing
    }

    private fun getAllInfoForOneHousing(reference: String)
    {
        this.address = this.getAddress(reference)
        this.photoList = this.getPhotoList(reference)
        this.estateAgentList = this.getEstateAgentList(reference)
        this.poiList = this.getPoiList(reference)

    }

    private fun getAddress(reference: String) : LiveData<Address> = this.addressRepository.getAddressFromHousing(reference)

    private fun getPhotoList(reference: String) : LiveData<List<Photo>> = this.photoRepository.getPhotoListFromHousing(reference)

    private fun getPoiList(reference: String) : LiveData<List<HousingPoi>> = this.housingPoiRepository.getHousingPoiFromHousing(reference)

    private fun getEstateAgentList(reference: String) : LiveData<List<HousingEstateAgent>> = this.housingEstateAgentRepository.getHousingEstateAgentFromHousing(reference)

}