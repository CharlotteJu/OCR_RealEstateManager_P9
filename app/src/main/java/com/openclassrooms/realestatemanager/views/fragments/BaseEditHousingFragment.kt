package com.openclassrooms.realestatemanager.views.fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.net.toUri
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.*
import com.openclassrooms.realestatemanager.utils.*
import com.openclassrooms.realestatemanager.viewModels.AddUpdateHousingViewModel
import com.openclassrooms.realestatemanager.views.adapters.*
import kotlinx.android.synthetic.main.dialog_photo.view.*
import kotlinx.android.synthetic.main.fragment_add_housing.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

abstract class BaseEditHousingFragment : BaseFragment(), OnItemClickEdit
{
    protected lateinit var housing : Housing
    protected lateinit var housingReference : String
    private lateinit var currency : String
    protected lateinit var mAdapterEstateAgentRcv : ListEstateAgentAdapter
    protected lateinit var mAdapterPhotoAddRcv : ListPhotoAddAdapter
    protected var address : Address? = null
    protected var estateAgentList : MutableList<HousingEstateAgent> = ArrayList()
    private var photoUri : Uri? = null

    protected var photoList : MutableList<Photo> = ArrayList()

    protected val mViewModel : AddUpdateHousingViewModel by viewModel()
    protected lateinit var mView : View
    protected lateinit var mApiKey : String
    private lateinit var placesClient : PlacesClient

    protected var isInternetAvailable = false

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        if (arguments!= null)
        {
            housingReference = requireArguments().getString(BUNDLE_REFERENCE).toString()
            housing = Housing(ref = housingReference)
        }
        this.currency = getCurrencyFromSharedPreferences()
        this.mApiKey = resources.getString(R.string.google_api_key_project)
        this.mAdapterEstateAgentRcv = ListEstateAgentAdapter(estateAgentList, this)
        this.mAdapterPhotoAddRcv = ListPhotoAddAdapter(photoList, this)

        Places.initialize(requireContext(), mApiKey)
        this.placesClient = Places.createClient(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        mView = inflater.inflate(R.layout.fragment_add_housing, container, false)

        this.getEstateAgentList()
        this.getAllInfo()
        this.displayEstateAgentRcv()
        this.displayPhotoRcv()

        this.mView.add_housing_fragment_final_button.visibility = View.INVISIBLE
        this.mView.add_housing_fragment_final_button.isEnabled = false

        return mView
    }

    private fun getEstateAgentList()
    {
        this.mViewModel.getEstateAgentList().observe(this.viewLifecycleOwner, Observer { list ->

            val nameList = ArrayList<String>()

            if (list.isNotEmpty() && list[0].lastName != SPINNER_SELECT )
            {
                nameList.add(SPINNER_SELECT)
            }

            for (i in list)
            {
                nameList.add(i.lastName)
            }

            val adapter = context?.let {
                ArrayAdapter(it, android.R.layout.simple_spinner_item, nameList)
                        .also {charSequence -> charSequence.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        }
            }
            this.mView.add_housing_fragment_estate_agent_name_spinner.adapter = adapter
        })
    }


    private fun getAllInfo()
    {
        this.getPrice()
        this.getTypeAndState()
        this.getInfoInsideHouse()
        this.getAddress()
        this.getDescription()
        this.getPhotos()
        this.getEstateAgents()
        this.getDateOfToday()
    }

    private fun getPrice()
    {
        this.mView.add_housing_fragment_price_editTxt.doAfterTextChanged {
            if (it.toString().isNotEmpty())
            {
                if (currency== DOLLAR) this.housing.price = it.toString().toDouble()
                else
                {
                    val price = it.toString().toDouble()
                    val euroToDollarPrice = Utils.convertEuroToDollarDouble(price)
                    this.housing.price = euroToDollarPrice
                }
            }
            else this.housing.price = DOUBLE_00
            this.enableFinalButton()
        }
    }

    private fun getInfoInsideHouse()
    {
        this.mView.add_housing_fragment_area_editTxt.doAfterTextChanged {
            if(it.toString().isNotEmpty())
            {
                housing.area = it.toString().toDouble()

            }
            else housing.area = DOUBLE_00
            this.enableFinalButton()
        }

        this.mView.add_housing_fragment_number_rooms_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                parent?.let {
                    val item : String = parent.getItemAtPosition(position).toString()
                    if (item != SPINNER_SELECT) housing.rooms = item.toInt()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        this.mView.add_housing_fragment_number_bedrooms_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                parent?.let {
                    val item : String = parent.getItemAtPosition(position).toString()
                    if (item != SPINNER_SELECT) housing.bedrooms = item.toInt()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        this.mView.add_housing_fragment_number_bathrooms_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                parent?.let {
                    val item : String = parent.getItemAtPosition(position).toString()
                    if (item != SPINNER_SELECT) housing.bathrooms = item.toInt()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun getTypeAndState()
    {
        this.mView.add_housing_fragment_type_spinner.onItemSelectedListener =  object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                parent?.let {
                    val item : String = parent.getItemAtPosition(position).toString()
                    if (item != SPINNER_SELECT) {
                        housing.type = item
                        enableFinalButton()
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        this.mView.add_housing_fragment_state_spinner.onItemSelectedListener =  object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                parent?.let {
                    val item : String = parent.getItemAtPosition(position).toString()
                    if (item != SPINNER_SELECT)
                    {
                        housing.state = item
                        enableFinalButton()
                        if (item == getString(R.string.sold_out))
                        {
                            getDatePickerDialog()
                        }
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun getDescription()
    {
        this.mView.add_housing_fragment_description_editTxt.doAfterTextChanged { housing.description = it.toString() }
    }

    private fun getAddress()
    {

        val uuidAddress = UUID.randomUUID().toString()

        address = Address(uuidAddress, street = STRING_EMPTY, city = STRING_EMPTY, country = STRING_EMPTY, housingReference = this.housingReference)

        this.mView.add_housing_fragment_address_editTxt.doAfterTextChanged { address!!.street = it.toString() }
        this.mView.add_housing_fragment_zipCode_editTxt.doAfterTextChanged{
            if (it.toString().isNotEmpty()) address!!.zipCode = it.toString().toInt()
            else address!!.zipCode = null
        }
        this.mView.add_housing_fragment_city_editTxt.doAfterTextChanged { address!!.city = it.toString() }

        this.mView.add_housing_fragment_country_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                parent?.let {
                    val item : String = parent.getItemAtPosition(position).toString()
                    if (item != SPINNER_SELECT) address!!.country = item
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    protected fun checkAddress()
    {
        if (address!!.street == STRING_EMPTY || address!!.city == STRING_EMPTY || address!!.country == STRING_EMPTY)
        {
            if (address!!.street != STRING_EMPTY || address!!.city != STRING_EMPTY || address!!.country != STRING_EMPTY)
            {
                Toast.makeText(context, getString(R.string.toast_invalid_address), Toast.LENGTH_LONG).show()
            }
            address = null
        }
    }

    private fun getEstateAgents()
    {
        var housingEstateAgent: HousingEstateAgent? = null

        this.mView.add_housing_fragment_estate_agent_button.isEnabled = false
        this.mView.add_housing_fragment_estate_agent_name_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                parent?.let {
                    val item : String = parent.getItemAtPosition(position).toString()
                    if (item != SPINNER_SELECT)
                    {
                        housingEstateAgent = HousingEstateAgent(housingReference, item)
                        mView.add_housing_fragment_estate_agent_button.isEnabled = true
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        this.mView.add_housing_fragment_estate_agent_button.setOnClickListener {
            housingEstateAgent?.let {
                estateAgentList.add(it)
                mAdapterEstateAgentRcv.updateList(estateAgentList)
                mView.add_housing_fragment_estate_agent_name_spinner.setSelection(0)
            }
        }
    }


    private fun getPhotos()
    {
        var description : String? = null
        this.mView.add_housing_fragment_photo_image.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                UtilsPermissions.checkCameraPermission(requireActivity())
                UtilsPermissions.checkReadPermission(requireActivity())
                UtilsPermissions.checkWritePermission(requireActivity())
                getAlertDialogPhoto()
            }
            else getAlertDialogPhoto()
        }

        this.mView.add_housing_fragment_image_description_editTxt.doAfterTextChanged { description = it.toString() }

        this.mView.add_housing_fragment_photo_button.setOnClickListener{
            if (photoUri != null && photoUri != STRING_EMPTY.toUri())
            {
                val photo = Photo(photoUri!!.toString(), description, this.housing.ref)
                //val photoCopy = photo.copy()
                photoList.add(photo)
                mAdapterPhotoAddRcv.updateList(photoList)

                //Clear photo and description
                this.mView.add_housing_fragment_photo_image.setImageResource(R.drawable.ic_baseline_add_48)

                 photoUri = STRING_EMPTY.toUri()
                 description = STRING_EMPTY
                this.mView.add_housing_fragment_image_description_editTxt.text.clear()
            }
            else
            {
                Toast.makeText(requireContext(), getString(R.string.toast_no_photo), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun pickImageFromGallery()
    {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        this.startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE)

    }

    private fun openCamera()
    {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the camera")
        photoUri = requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE)
    }

    private fun displayEstateAgentRcv()
    {
        this.mView.add_housing_fragment_estate_agent_rcv.adapter = mAdapterEstateAgentRcv
        this.mView.add_housing_fragment_estate_agent_rcv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun displayPhotoRcv()
    {
        this.mView.add_housing_fragment_photo_rcv.adapter = mAdapterPhotoAddRcv
        this.mView.add_housing_fragment_photo_rcv.layoutManager = LinearLayoutManager(context)
    }

    /**
     * Just for ADD
     */
    private fun enableFinalButton()
    {
        if (housing.type != STRING_EMPTY && housing.price != DOUBLE_00 && housing.area != DOUBLE_00 && housing.state!= STRING_EMPTY )
        {
            this.mView.add_housing_fragment_final_button.visibility = View.VISIBLE
            this.mView.add_housing_fragment_final_button.isEnabled = true
        }
    }

    /**
     * Just for ADD
     */
    private fun getDateOfToday()
    {
        housing.dateEntry = Utils.getTodayDateGood()
    }

    protected fun configureSpinnerAdapter(res : Int) : ArrayAdapter<CharSequence>?
    {
        return context?.let { ArrayAdapter.createFromResource(it, res, android.R.layout.simple_spinner_item).
        also {charSequence -> charSequence.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)}}
    }

    private fun getDatePickerDialog()
    {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->

            val dayString = if (dayOfMonth < 10) "0$dayOfMonth"
            else dayOfMonth.toString()

            val month1 = month+1
            val monthString = if (month1 < 10) "0$month1"
            else month1.toString()

            val dateString = "$dayString/$monthString/$year"

            housing.dateSale = UtilsKotlin.convertStringToLongDate(dateString)
        }, year, month, dayOfMonth)
        datePickerDialog.show()
    }

    private fun getAlertDialogPhoto()
    {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_photo, null)
        dialogBuilder.setView(dialogLayout).create()
        val alertDialog : AlertDialog = dialogBuilder.show()

        dialogLayout.dialog_photo_gallery_button.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                UtilsPermissions.checkReadPermission(requireActivity())
            }
            this.pickImageFromGallery()
            alertDialog.dismiss()
        }
        dialogLayout.dialog_photo_camera_button.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                UtilsPermissions.checkCameraPermission(requireActivity())
                UtilsPermissions.checkWritePermission(requireActivity())
            }
            this.openCamera()
            alertDialog.dismiss()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                photoUri = data?.data
                context?.let {
                    Glide.with(it)
                            .load(photoUri)
                            .apply(RequestOptions.centerCropTransform())
                            .into(this.mView.add_housing_fragment_photo_image)
                }
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                context?.let {
                    Glide.with(it)
                            .load(photoUri)
                            .apply(RequestOptions.centerCropTransform())
                            .into(this.mView.add_housing_fragment_photo_image)
                }
            }
        }
    }
}