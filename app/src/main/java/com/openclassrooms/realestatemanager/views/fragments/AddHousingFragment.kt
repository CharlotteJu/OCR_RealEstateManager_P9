package com.openclassrooms.realestatemanager.views.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.*
import com.openclassrooms.realestatemanager.utils.*
import kotlinx.android.synthetic.main.fragment_add_housing.view.*
import kotlinx.android.synthetic.main.progress_bar.view.*

class AddHousingFragment : BaseEditHousingFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        super.onCreateView(inflater, container, savedInstanceState)
        this.isLoadingEdit = false
        this.mView.progress_bar_layout.visibility = View.GONE
        this.configureSpinners()
        this.mView.add_housing_fragment_final_button.setOnClickListener{
            this.addFinal()
            this.findNavController().navigate(R.id.listFragment)
        }
        return mView
    }


    /**
     * Link with the ViewModel to add a CompleteHousing in RoomDatabase
     */
    private fun addFinal()
    {
        this.checkAddress()
        housing.dateEntry = Utils.getTodayDateGood()

        context?.let {
            housing.lastUpdate = Utils.getTodayDateGood()
            this.mViewModel.createGlobalHousing(housing, address, photoList, estateAgentList, it, mApiKey, isInternetAvailable)
        }

    }


    override fun onClickDeleteEstateAgent(position: Int) {

        val builder = AlertDialog.Builder(context)
        builder.setMessage(resources.getString(R.string.sure_delete))
                .setPositiveButton(getString(R.string.yes), DialogInterface.OnClickListener { _, _ ->
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

        UtilsKotlin.displayPhoto(isInternetAvailable, photoToEdit, mView, mView.add_housing_fragment_photo_image, requireContext())

        photoToEdit.description?.let { this.mView.add_housing_fragment_image_description_editTxt.setText(it) }

        this.mView.add_housing_fragment_photo_image.isEnabled = false
        var description = STRING_EMPTY

        this.mView.add_housing_fragment_image_description_editTxt.doAfterTextChanged { description = it.toString() }

        this.mView.add_housing_fragment_photo_button.setOnClickListener {
            val photo = Photo(photoToEdit.uri, description, housingReference, photoToEdit.url_firebase)
            photoList[position] = photo
            mAdapterPhotoAddRcv.updateList(photoList)

            //Clear photo and description
            this.mView.add_housing_fragment_photo_image.isEnabled = true
            this.mView.add_housing_fragment_photo_image.setImageResource(R.drawable.ic_baseline_add_photo_camera_48)
            description = STRING_EMPTY
            this.mView.add_housing_fragment_image_description_editTxt.text.clear()

        }
    }

    override fun onClickDeletePhoto(position: Int) {

        val builder = AlertDialog.Builder(context)
        builder.setMessage(resources.getString(R.string.sure_delete))
                .setPositiveButton(getString(R.string.yes), DialogInterface.OnClickListener { _, _ ->
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