package com.openclassrooms.realestatemanager.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.libraries.places.api.Places
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.CompleteHousing
import com.openclassrooms.realestatemanager.models.Housing
import com.openclassrooms.realestatemanager.models.HousingEstateAgent
import com.openclassrooms.realestatemanager.models.Photo
import com.openclassrooms.realestatemanager.utils.BUNDLE_REFERENCE
import com.openclassrooms.realestatemanager.utils.STRING_EMPTY
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.views.adapters.ListEstateAgentAdapter
import com.openclassrooms.realestatemanager.views.adapters.ListPhotoAddAdapter
import kotlinx.android.synthetic.main.fragment_add_housing.view.*
import kotlinx.android.synthetic.main.fragment_add_housing.view.add_housing_fragment_zipCode_editTxt

/**
 * A simple [Fragment] subclass.
 */
class EditHousingFragment : BaseEditHousingFragment() {

    private lateinit var housingToCompare : CompleteHousing
    private  var mAdapterType : ArrayAdapter<CharSequence>? = null
    private  var mAdapterState : ArrayAdapter<CharSequence>? = null
    private  var mAdapterRooms : ArrayAdapter<CharSequence>? = null
    private  var mAdapterBedRooms : ArrayAdapter<CharSequence>? = null
    private  var mAdapterBathRooms : ArrayAdapter<CharSequence>? = null
    private  var mAdapterCity : ArrayAdapter<CharSequence>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        this.configureSpinnersEdit()
        this.getHousing()

        this.mView.add_housing_fragment_final_button.setImageResource(R.drawable.ic_baseline_save_24)
        this.mView.add_housing_fragment_final_button.setOnClickListener {
            this.updateFinal()
            this.findNavController().navigate(R.id.listFragment)
        }
        return mView
    }

    private fun updateFinal()
    {
        this.checkAddress()
        context?.let {
            this.isInternetAvailable = Utils.isInternetAvailable(it)
            this.mViewModel.updateGlobalHousing(housingToCompare ,housing, address, photoList, estateAgentList, it, mApiKey, isInternetAvailable)
        }
    }

    private fun getHousing()
    {
        this.mViewModel.getCompleteHousing(housingReference).observe(this.viewLifecycleOwner, Observer {
            housingToCompare = it
            configureData()
        })
    }

    private fun configureData()
    {
        this.housing = this.housingToCompare.housing.copy()

        housingToCompare.housing.price.let { this.mView.add_housing_fragment_price_editTxt.setText(it.toString()) }
        housingToCompare.housing.area.let { this.mView.add_housing_fragment_area_editTxt.setText(it.toString()) }
        housingToCompare.housing.area.let { this.mView.add_housing_fragment_area_editTxt.setText(it.toString()) }
        housingToCompare.housing.description?.let { this.mView.add_housing_fragment_description_editTxt.setText(it) }

        housingToCompare.housing.type.let { this.mView.add_housing_fragment_type_spinner.setSelection(mAdapterType!!.getPosition(it))}
        housingToCompare.housing.state.let { this.mView.add_housing_fragment_state_spinner.setSelection(mAdapterState!!.getPosition(it)) }
        housingToCompare.housing.rooms?.let { this.mView.add_housing_fragment_number_rooms_spinner.setSelection(mAdapterRooms!!.getPosition(it.toString())) }
        housingToCompare.housing.bedrooms?.let { this.mView.add_housing_fragment_number_rooms_spinner.setSelection(mAdapterBedRooms!!.getPosition(it.toString())) }
        housingToCompare.housing.bathrooms?.let { this.mView.add_housing_fragment_number_rooms_spinner.setSelection(mAdapterBathRooms!!.getPosition(it.toString())) }

        if (housingToCompare.address != null)
        {
            this.address = this.housingToCompare.address!!.copy()
            housingToCompare.address!!.street?.let { this.mView.add_housing_fragment_address_editTxt.setText(it) }
            housingToCompare.address!!.zipCode?.let { this.mView.add_housing_fragment_zipCode_editTxt.setText(it) }
            housingToCompare.address!!.city?.let { this.mView.add_housing_fragment_city_editTxt.setText(it) }
            housingToCompare.address!!.country?.let {  }
        }

        housingToCompare.photoList?.let {
            for (i in it)
            {
                this.photoList.add(i)
            }
            //this.photoList = it as MutableList<Photo>
            mAdapterPhotoAddRcv.updateList(this.photoList)

        }
        housingToCompare.estateAgentList?.let {
            for (i in it)
            {
                this.estateAgentList.add(i)
            }
            //this.estateAgentList = it as MutableList<HousingEstateAgent>
            mAdapterEstateAgentRcv.updateList(this.estateAgentList)
        }
    }

    private fun configureSpinnersEdit()
    {
        configureSpinnerAdapter(R.array.type_housing_spinner)?.let { this.mAdapterType = it }
        this.mView.add_housing_fragment_type_spinner.adapter = mAdapterType
        configureSpinnerAdapter(R.array.state_spinner)?.let { this.mAdapterState = it }
        this.mView.add_housing_fragment_state_spinner.adapter = mAdapterState
        configureSpinnerAdapter(R.array.number_rooms)?.let {
            this.mAdapterRooms = it
            this.mAdapterBedRooms = it
            this.mAdapterBathRooms = it
        }
        this.mView.add_housing_fragment_number_rooms_spinner.adapter = mAdapterRooms
        this.mView.add_housing_fragment_number_bedrooms_spinner.adapter = mAdapterBedRooms
        this.mView.add_housing_fragment_number_bathrooms_spinner.adapter = mAdapterBathRooms
    }

    override fun onClickDeleteEstateAgent(position: Int) {

        if (estateAgentList.size <= 1)
        {
            estateAgentList.clear()
        }
        else
        {
            val estateAgentToDelete = this.estateAgentList[position]
            this.estateAgentList.remove(estateAgentToDelete)
        }
        this.mAdapterEstateAgentRcv.updateList(estateAgentList)

    }

    override fun onClickEditPhoto(position: Int) {
        val photoToEdit = this.photoList[position]

        Glide.with(requireContext())
                .load(photoToEdit.uri)
                .apply(RequestOptions.centerCropTransform())
                .into(this.mView.add_housing_fragment_photo_image)

        photoToEdit.description?.let { this.mView.add_housing_fragment_image_description_editTxt.setText(it) }


        var description = STRING_EMPTY
        this.mView.add_housing_fragment_image_description_editTxt.doAfterTextChanged { description = it.toString() }

        this.mView.add_housing_fragment_photo_button.setOnClickListener {
            val photo = Photo(photoToEdit.uri, description, housingReference)
            photoList.set(position, photo)
            mAdapterPhotoAddRcv.updateList(photoList)

            //Clear photo and description
            this.mView.add_housing_fragment_photo_image.setImageResource(R.drawable.ic_baseline_add_48)

            /* photoUri = STRING_EMPTY
         description = STRING_EMPTY*/ //TODO-Q : Où est-ce que je peux clear ça ?
            this.mView.add_housing_fragment_image_description_editTxt.text.clear()

        }
    }

    override fun onClickDeletePhoto(position: Int) {
        if (photoList.size <= 1)
        {
            photoList.clear()
        }
        else
        {
            val photoToDelete = this.photoList[position]
            this.photoList.remove(photoToDelete)
        }
        this.mAdapterPhotoAddRcv.updateList(photoList)
    }




}