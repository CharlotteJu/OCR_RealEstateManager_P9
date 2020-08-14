package com.openclassrooms.realestatemanager.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.libraries.places.api.Places
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.CompleteHousing
import com.openclassrooms.realestatemanager.models.Housing
import com.openclassrooms.realestatemanager.utils.BUNDLE_REFERENCE
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments!= null)
        {
            housingReference = requireArguments().getString(BUNDLE_REFERENCE).toString()
            housing = Housing(ref = housingReference)
        }
        this.currency = getCurrencyFromSharedPreferences()
        this.mApiKey = resources.getString(R.string.google_api_key)
        this.mAdapterEstateAgentRcv = ListEstateAgentAdapter(estateAgentList, this)
        this.mAdapterPhotoAddRcv = ListPhotoAddAdapter(photoList, this)

        Places.initialize(requireContext(), mApiKey)
        this.placesClient = Places.createClient(requireContext())


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        this.mView = inflater.inflate(R.layout.fragment_add_housing, container, false)
        this.getEstateAgentList()
        this.configureSpinnersEdit()
        this.getAllInfo()
        this.displayEstateAgentRcv()
        this.displayPhotoRcv()

        this.mView.add_housing_fragment_final_button.visibility = View.INVISIBLE
        this.mView.add_housing_fragment_final_button.isEnabled = false

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
            housingToCompare.address!!.street?.let { this.mView.add_housing_fragment_address_editTxt.setText(it) }
            housingToCompare.address!!.zipCode?.let { this.mView.add_housing_fragment_zipCode_editTxt.setText(it) }
            housingToCompare.address!!.city?.let { this.mView.add_housing_fragment_city_editTxt.setText(it) }
            housingToCompare.address!!.country?.let {  }
        }

        housingToCompare.photoList?.let { mAdapterPhotoAddRcv.updateList(it) }
        housingToCompare.estateAgentList?.let { mAdapterEstateAgentRcv.updateList(it) }
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
        val estateAgentToDelete = this.estateAgentList[position]
        this.estateAgentList.remove(estateAgentToDelete)
    }

    override fun onClickEditPhoto(position: Int) {
        val photoToEdit = this.photoList[position]

        Glide.with(requireContext())
                .load(photoToEdit.uri)
                .apply(RequestOptions.centerCropTransform())
                .into(this.mView.add_housing_fragment_photo_image)

        photoToEdit.description?.let { this.mView.add_housing_fragment_image_description_editTxt.setText(it) } //TODO : Vraiment un Edit ? Ou juste Delete ? Je peux supprimer ?

        this.photoList.set(position, photoToEdit)
    }

    override fun onClickDeletePhoto(position: Int) {
        val photoToDelete = this.photoList[position]
        this.photoList.remove(photoToDelete)
    }




}