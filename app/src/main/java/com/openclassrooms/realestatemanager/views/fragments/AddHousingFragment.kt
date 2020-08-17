package com.openclassrooms.realestatemanager.views.fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentResolver
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.SyncStateContract.Helpers.insert
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.*
import com.openclassrooms.realestatemanager.utils.*
import com.openclassrooms.realestatemanager.viewModels.AddUpdateHousingViewModel
import com.openclassrooms.realestatemanager.views.adapters.ListEstateAgentAdapter
import com.openclassrooms.realestatemanager.views.adapters.ListPhotoAddAdapter
import com.openclassrooms.realestatemanager.views.adapters.ListPhotoDetailAdapter
import com.openclassrooms.realestatemanager.views.adapters.OnItemClickListener
import kotlinx.android.synthetic.main.dialog_photo.view.*
import kotlinx.android.synthetic.main.fragment_add_housing.view.*
import kotlinx.android.synthetic.main.fragment_detail.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList


class AddHousingFragment : BaseEditHousingFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        super.onCreateView(inflater, container, savedInstanceState)
        this.configureSpinners()
        this.mView.add_housing_fragment_final_button.setOnClickListener{
            this.addFinal()
            this.findNavController().navigate(R.id.listFragment)
        }
        return mView
    }

    override fun onClickDeleteEstateAgent(position: Int) {
        if (estateAgentList.size <= 1)
        {
            photoList.clear()
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

        this.photoList.set(position, photoToEdit)
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


    private fun addFinal()
    {
        this.checkAddress()
        context?.let { this.mViewModel.createGlobalHousing(housing, address, photoList, estateAgentList, it, mApiKey) }

        //TODO : Si connecté à internet --> Push sur Firebase
    }

    private fun configureSpinners()
    {
        this.mView.add_housing_fragment_type_spinner.adapter = configureSpinnerAdapter(R.array.type_housing_spinner)
        this.mView.add_housing_fragment_type_spinner.prompt = getString(R.string.spinners_type)
        this.mView.add_housing_fragment_state_spinner.adapter = configureSpinnerAdapter(R.array.state_spinner)
        this.mView.add_housing_fragment_state_spinner.prompt = getString(R.string.spinners_state)
        this.mView.add_housing_fragment_number_rooms_spinner.adapter = configureSpinnerAdapter(R.array.number_rooms)
        this.mView.add_housing_fragment_number_rooms_spinner.prompt = getString(R.string.spinners_rooms)
        this.mView.add_housing_fragment_number_bedrooms_spinner.adapter = configureSpinnerAdapter(R.array.number_rooms)
        this.mView.add_housing_fragment_number_bedrooms_spinner.prompt = getString(R.string.spinners_bedrooms)
        this.mView.add_housing_fragment_number_bathrooms_spinner.adapter = configureSpinnerAdapter(R.array.number_rooms)
        this.mView.add_housing_fragment_number_bathrooms_spinner.prompt = getString(R.string.spinners_bathrooms)
        //TODO : NameSpinner && Prompt ne fonctionne pas

    }

}