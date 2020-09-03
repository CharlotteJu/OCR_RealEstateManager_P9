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
import androidx.core.net.toUri
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
import kotlinx.android.synthetic.main.progress_bar.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList


class AddHousingFragment : BaseEditHousingFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        super.onCreateView(inflater, container, savedInstanceState)
        this.mView.progress_bar_layout.visibility = View.GONE
        this.configureSpinners()
        this.mView.add_housing_fragment_final_button.setOnClickListener{
            this.addFinal()
            this.findNavController().navigate(R.id.listFragment)
        }
        return mView
    }


    private fun addFinal()
    {
        this.checkAddress()

        context?.let {
            housing.lastUpdate = Utils.getTodayDateGood()
            this.isInternetAvailable = Utils.isInternetAvailableGood(it)
            this.mViewModel.createGlobalHousing(housing, address, photoList, estateAgentList, it, mApiKey, isInternetAvailable)
        }

    }

    override fun onClickDeleteEstateAgent(position: Int) {

        val builder = AlertDialog.Builder(context)
        builder.setMessage(resources.getString(R.string.sure_delete))
                .setPositiveButton(getString(R.string.yes), DialogInterface.OnClickListener { dialog, which ->
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
                })
                .setNegativeButton(getString(R.string.no), DialogInterface.OnClickListener { dialog, _ -> dialog.cancel() })

        val alertDialog = builder.create()
        alertDialog.show()

    }

    override fun onClickEditPhoto(position: Int) {
        val photoToEdit = this.photoList[position]

        Glide.with(requireContext())
                .load(photoToEdit.uri)
                .apply(RequestOptions.centerCropTransform())
                .into(this.mView.add_housing_fragment_photo_image)

        photoToEdit.description?.let { this.mView.add_housing_fragment_image_description_editTxt.setText(it) }

        this.mView.add_housing_fragment_photo_image.isEnabled = false
        var description = STRING_EMPTY

        this.mView.add_housing_fragment_image_description_editTxt.doAfterTextChanged { description = it.toString() }

        this.mView.add_housing_fragment_photo_button.setOnClickListener {
            val photo = Photo(photoToEdit.uri, description, housingReference)
            photoList[position] = photo
            mAdapterPhotoAddRcv.updateList(photoList)
            //Clear photo and description
            this.mView.add_housing_fragment_photo_image.setImageResource(R.drawable.ic_baseline_add_48)
            description = STRING_EMPTY
            this.mView.add_housing_fragment_image_description_editTxt.text.clear()

        }
    }

    override fun onClickDeletePhoto(position: Int) {

        val builder = AlertDialog.Builder(context)
        builder.setMessage(resources.getString(R.string.sure_delete))
                .setPositiveButton(getString(R.string.yes), DialogInterface.OnClickListener { dialog, which ->
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
                })
                .setNegativeButton(getString(R.string.no), DialogInterface.OnClickListener { dialog, _ -> dialog.cancel() })

        val alertDialog = builder.create()
        alertDialog.show()
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
        this.mView.add_housing_fragment_country_spinner.adapter = configureSpinnerAdapter(R.array.country_spinner)
        this.mView.add_housing_fragment_country_spinner.prompt = getString(R.string.spinners_country)
        //TODO : NameSpinner && Prompt ne fonctionne pas

    }

}