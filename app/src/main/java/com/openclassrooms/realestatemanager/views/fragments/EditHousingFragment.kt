package com.openclassrooms.realestatemanager.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.CompleteHousing
import com.openclassrooms.realestatemanager.models.Housing
import kotlinx.android.synthetic.main.dialog_filter.view.*
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
        this.getHousing()

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
        /*this.mView.add_housing_fragment_final_button.setOnClickListener {
            this.addFinal()
            this.findNavController().navigate(R.id.listFragment)
        }*/

        return mView
    }

    private fun getHousing()
    {
        this.mViewModel.getCompleteHousing(housingReference).observe(this.viewLifecycleOwner, Observer {
            housingToCompare = it
            configureDesign()
        })
    }

    private fun configureDesign()
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




}