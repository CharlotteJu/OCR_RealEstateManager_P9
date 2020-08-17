package com.openclassrooms.realestatemanager.viewModels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.*
import com.openclassrooms.realestatemanager.repositories.*
import com.openclassrooms.realestatemanager.utils.ERROR_GEOCODER_ADDRESS
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.utils.UtilsKotlin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


/**
 * View Model to add and update [Housing], [Address], [Photo], [HousingEstateAgent], [HousingPoi] --> AddHousingFragment & EditHousingFragment
 */
class AddUpdateHousingViewModel(private val housingRepository: HousingRepository,
                                private val addressRepository: AddressRepository,
                                private val photoRepository: PhotoRepository,
                                private val housingEstateAgentRepository: HousingEstateAgentRepository,
                                private val housingPoiRepository: HousingPoiRepository,
                                private val estateAgentRepository: EstateAgentRepository,
                                private val placesPoiRepository : PlacesPoiRepository,
                                private val poiRepository: PoiRepository)
                                : ViewModel()
{

    //////////////////// GET ////////////////////

    fun getEstateAgentList() : LiveData<List<EstateAgent>> = this.estateAgentRepository.getAllEstateAgent()

    fun getHousing(reference : String) : LiveData<Housing> = this.housingRepository.getHousing(reference)

    fun getCompleteHousing(reference: String) : LiveData<CompleteHousing> = this.housingRepository.getCompleteHousing(reference)

    fun getPoiList() : LiveData<List<Poi>> = this.poiRepository.getAllPoi()



    //////////////////// CREATE ////////////////////


    private suspend fun createHousing(housing : Housing) = this.housingRepository.createHousing(housing)

    private suspend fun createAddress(address: Address) = this.addressRepository.createAddress(address)

    private suspend fun createPhoto(photo: Photo) = this.photoRepository.createPhoto(photo)

    private suspend fun createHousingEstateAgent(housingEstateAgent: HousingEstateAgent) = this.housingEstateAgentRepository.createHousingEstateAgent(housingEstateAgent)

    private suspend fun createHousingPoi(housingPoi: HousingPoi) = this.housingPoiRepository.createHousingPoi(housingPoi)

    private suspend fun getPoi(location : String, type : String, radius : Int, key : String) = this.placesPoiRepository.test(location, type, radius, key)

    private suspend fun createPoi(housingPoi: HousingPoi) = this.housingPoiRepository.createHousingPoi(housingPoi)

    private suspend fun configurePoi(housing : Housing, location: String, context: Context, key : String)
    {
        val listTypePoi = getTypePoi(context)
        val listPoiPlaces = getPoi(location, "park", 500, key).results //TODO : Voir pour plusieurs types

        for (place in listPoiPlaces)
        {
            for (type in listTypePoi)
            {
                if (place.types.contains(type))
                {
                    val housingPoi = HousingPoi(housing.ref, type)
                    createHousingPoi(housingPoi)
                }
            }

        }
        //TODO : Faire un return si c'est ERROR
    }

    private fun getTypePoi(context: Context) : List<String>
    {
        val list : MutableList<String> = ArrayList()

        list.add(context.getString(R.string.poi_type_restaurant))
        list.add(context.getString(R.string.poi_type_subway))
        list.add(context.getString(R.string.poi_type_school))
        list.add(context.getString(R.string.poi_type_park))
        list.add(context.getString(R.string.poi_type_store))

        return list
    }

    fun createGlobalHousing (housing: Housing, address: Address?, photoList: List<Photo>?, estateAgentList: List<HousingEstateAgent>?, context: Context, key : String )
    {
       val job =  viewModelScope.launch  (Dispatchers.IO)
        {

            val thread = viewModelScope.async {  createHousing(housing) }
            thread.await()
            //createHousing(housing) //Mettre dans un async puis await
            //MainThread : Voir lien

            if (address != null)
            {
                val location = UtilsKotlin.getGeocoderAddress(address.toString(), context)

                if (location != null && location != ERROR_GEOCODER_ADDRESS)
                {
                    createAddress(address)
                    configurePoi(housing, location, context, key)
                }

            }


            if (estateAgentList != null)
            {
                for (estate in estateAgentList)
                {
                    createHousingEstateAgent(estate)
                }
            }

            if (photoList != null)
            {
                for (photo in photoList)
                {
                    createPhoto(photo)
                }
            }


        }


        //TODO : Mettre un Listener pour savoir quand c'est fini

       // Toast.makeText(context, "OK", Toast.LENGTH_LONG).show()
        //test1.join()

    }


    //////////////////// UPDATE ////////////////////

    private suspend fun updateHousing(housing: Housing) = this.housingRepository.updateHousing(housing)

    private suspend fun updateAddress(address: Address) = this.addressRepository.updateAddress(address)

    private suspend fun deleteAddress(address: Address) = this.addressRepository.deleteAddress(address)

    private suspend fun updatePhoto(photo: Photo) = this.photoRepository.updatePhoto(photo)

    private suspend fun deletePhoto(photo: Photo) = this.photoRepository.deletePhoto(photo)

    private suspend fun updateHousingEstateAgent(housingEstateAgent: HousingEstateAgent) = this.housingEstateAgentRepository.updateHousingEstateAgent(housingEstateAgent)

    private suspend fun deleteHousingEstateAgent(housingEstateAgent: HousingEstateAgent) = this.housingEstateAgentRepository.deleteHousingEstateAgent(housingEstateAgent)

    private suspend fun updateHousingPoi(housingPoi: HousingPoi) = this.housingPoiRepository.updateHousingPoi(housingPoi)

    fun updateGlobalHousing (completeHousing: CompleteHousing, housing: Housing, address: Address?, photoList: List<Photo>?, estateAgentList: List<HousingEstateAgent>?, context: Context, key : String)
    {
        viewModelScope.launch (Dispatchers.IO)
        {

            if (housing.ref == completeHousing.housing.ref)
            {
                if (housing != completeHousing.housing) updateHousing(housing)

                if (address != null)
                    if (completeHousing.address != null && address != completeHousing.address) {

                    val location = UtilsKotlin.getGeocoderAddress(address.toString(), context)

                    if (location != null && location != ERROR_GEOCODER_ADDRESS)
                    {
                        updateAddress(address)
                        //TODO : Delete liste POI
                        configurePoi(housing, location, context, key)
                    }
                }
                else deleteAddress(address) //TODO : Améliorer


                if (estateAgentList != null && completeHousing.estateAgentList != null && estateAgentList != completeHousing.estateAgentList)
                {
                    for (i in estateAgentList)
                    {
                        if (!completeHousing.estateAgentList!!.contains(i)) createHousingEstateAgent(i)
                    }

                    for (i in completeHousing.estateAgentList!!)
                    {
                        if (!estateAgentList.contains(i)) deleteHousingEstateAgent(i)
                    }
                }

                if (photoList != null && completeHousing.photoList != null && photoList != completeHousing.photoList)
                {
                    for (i in photoList)
                    {
                        if (!completeHousing.photoList!!.contains(i)) {
                            createPhoto(i)
                        }

                        else
                        {
                            val index = completeHousing.photoList!!.indexOf(i)
                            if (completeHousing.photoList!![index].description != i.description) {
                                updatePhoto(i)
                            }
                        }
                    }

                    for (i in completeHousing.photoList!!)
                    {
                        if (!photoList.contains(i))
                        {
                            deletePhoto(i)
                        }
                    }
                }
            }
        }

    }











}


