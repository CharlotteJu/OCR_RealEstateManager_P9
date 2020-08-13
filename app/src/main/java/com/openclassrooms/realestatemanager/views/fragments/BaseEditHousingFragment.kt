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
import kotlinx.android.synthetic.main.dialog_photo.view.*
import kotlinx.android.synthetic.main.fragment_add_housing.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

abstract class BaseEditHousingFragment : BaseFragment()
{
    protected lateinit var housing : Housing
    protected lateinit var housingReference : String
    protected lateinit var currency : String
    protected lateinit var mAdapterEstateAgentRcv : ListEstateAgentAdapter
    protected lateinit var mAdapterPhotoAddRcv : ListPhotoAddAdapter
    protected var address : Address? = null
    protected var estateAgentList : MutableList<HousingEstateAgent> = ArrayList()
    protected var photoUri : Uri? = null

    protected var photoList : MutableList<Photo> = ArrayList()

    protected val mViewModel : AddUpdateHousingViewModel by viewModel()
    protected lateinit var mView : View
    protected lateinit var mApiKey : String
    protected lateinit var placesClient : PlacesClient

    protected lateinit var listAdapterTypeSpinner : ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        if (arguments!= null)
        {
            housingReference = requireArguments().getString("reference").toString()
            housing = Housing(ref = housingReference)
        }
        this.currency = getCurrencyFromSharedPreferences()
        this.mApiKey = resources.getString(R.string.google_api_key)
        this.mAdapterEstateAgentRcv = ListEstateAgentAdapter(estateAgentList)
        this.mAdapterPhotoAddRcv = ListPhotoAddAdapter(photoList)

        Places.initialize(requireContext(), mApiKey)
        this.placesClient = Places.createClient(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        mView = inflater.inflate(R.layout.fragment_add_housing, container, false)

        this.getEstateAgentList()
        this.configureSpinners()
        this.getAllInfo()
        this.displayEstateAgentRcv()
        this.displayPhotoRcv()

        this.mView.add_housing_fragment_final_button.visibility = View.INVISIBLE
        this.mView.add_housing_fragment_final_button.isEnabled = false



        return mView
    }

    protected fun getEstateAgentList()
    {
        this.mViewModel.getEstateAgentList().observe(this.viewLifecycleOwner, Observer { list ->

            val nameList = ArrayList<String>()

            if (list.isNotEmpty() && list[0].lastName != SPINNER_SELECT ) {
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

    protected fun configureSpinners()
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

    protected fun getAllInfo()
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

    protected fun getPrice()
    {
        this.mView.add_housing_fragment_price_editTxt.doAfterTextChanged {
            if (currency== DOLLAR)
            {
                this.housing.price = it.toString().toDouble()
            }
            else
            {
                val price = it.toString().toDouble()
                val euroToDollarPrice = Utils.convertEuroToDollarDouble(price)
                this.housing.price = euroToDollarPrice
            }
            this.enableFinalButton()
        }
    }

    protected fun getInfoInsideHouse()
    {
        this.mView.add_housing_fragment_area_editTxt.doAfterTextChanged {
            housing.area = it.toString().toDouble()
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

    protected fun getTypeAndState()
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
                            //housing.dateSale =  UtilsKotlin.getDatePickerDialog(requireContext())
                            getDatePickerDialog()
                        }
                    }

                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    protected fun getDescription()
    {
        this.mView.add_housing_fragment_description_editTxt.doAfterTextChanged { housing.description = it.toString() }
    }

    protected fun getAddress()
    {

        val uuidAddress = UUID.randomUUID().toString()

        address = Address(uuidAddress, street = STRING_EMPTY, city = STRING_EMPTY, country = STRING_EMPTY, housingReference = this.housingReference)

        //TODO-Q : Si champs vide, que faire ?

        this.mView.add_housing_fragment_address_editTxt.doAfterTextChanged { address!!.street = it.toString() }
        this.mView.add_housing_fragment_zipCode_editTxt.doAfterTextChanged { address!!.zipCode = it.toString().toInt() }
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
        if (address!!.street == STRING_EMPTY && address!!.city == STRING_EMPTY && address!!.country == STRING_EMPTY)
        {
            address = null
        }
    }

    protected fun getEstateAgents()
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



    protected fun getPhotos()
    {
        //val photo = Photo(STRING_EMPTY, STRING_EMPTY, housingReference)

        var description = STRING_EMPTY

        this.mView.add_housing_fragment_photo_image.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                UtilsPermissions.checkCameraPermission(requireActivity()) //TODO : Attention, ça plante la 1ère fois
                UtilsPermissions.checkReadPermission(requireActivity())
                UtilsPermissions.checkWritePermission(requireActivity())
                getAlertDialogPhoto()
            }
            else getAlertDialogPhoto()
        }

        this.mView.add_housing_fragment_image_description_editTxt.doAfterTextChanged { description = it.toString() }

        this.mView.add_housing_fragment_photo_button.setOnClickListener{
            if (photoUri != null)
            {
                val photo = Photo(photoUri!!.toString(), description, housingReference)
                photoList.add(photo)
                mAdapterPhotoAddRcv.updateList(photoList)

                //Clear photo and description
                this.mView.add_housing_fragment_photo_image.setImageResource(R.drawable.ic_baseline_add_48)

                /* photoUri = STRING_EMPTY
                 description = STRING_EMPTY*/ //TODO-Q : Où est-ce que je peux clear ça ?
                this.mView.add_housing_fragment_image_description_editTxt.text.clear()
            }
            else
            {
                Toast.makeText(requireContext(), getString(R.string.toast_no_photo), Toast.LENGTH_LONG).show()
            }
        }
    }

    protected fun pickImageFromGallery()
    {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        this.startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE)

    }

    protected fun openCamera()
    {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the camera")
        photoUri = requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)


        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE)

    }

    protected fun displayEstateAgentRcv()
    {
        this.mView.add_housing_fragment_estate_agent_rcv.adapter = mAdapterEstateAgentRcv
        this.mView.add_housing_fragment_estate_agent_rcv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    protected fun displayPhotoRcv()
    {
        this.mView.add_housing_fragment_photo_rcv.adapter = mAdapterPhotoAddRcv
        this.mView.add_housing_fragment_photo_rcv.layoutManager = LinearLayoutManager(context)
    }

    /**
     * Just for ADD
     */
    protected fun enableFinalButton()
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
    protected fun getDateOfToday()
    {
        val stringDate = Utils.getTodayDateGood()
        housing.dateEntry = stringDate
    }

    protected fun configureSpinnerAdapter(res : Int) : ArrayAdapter<CharSequence>?
    {
        //TODO : Quand est-ce que ça peut être null ?
        return context?.let { ArrayAdapter.createFromResource(it, res, android.R.layout.simple_spinner_item).
        also {charSequence -> charSequence.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)}}
    }

    protected fun getDatePickerDialog()
    {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

            val month1 = month+1
            val monthString = if (month1 < 10) "0$month1"
            else month1.toString()

            housing.dateSale = "$dayOfMonth/$monthString/$year"
            //housing.dateSale = Utils.getDateFormat(calendar.time) //TODO-Q : Pourquoi ça met la Date du jour ?
        }, year, month, dayOfMonth)

        datePickerDialog.show()


    }

    protected fun getAlertDialogPhoto()
    {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_photo, null)

        dialogLayout.dialog_photo_gallery_button.setOnClickListener { this.pickImageFromGallery() }
        dialogLayout.dialog_photo_camera_button.setOnClickListener { this.openCamera() }

        dialogBuilder.setView(dialogLayout).create().show()

        /*dialogBuilder
                .setCancelable(false)
                .setPositiveButton(getString(R.string.gallery)) { dialog, which ->
                    this.pickImageFromGallery() }
                .setNegativeButton(R.string.camera) { dialog, which ->
                    this.openCamera() }
        val alert = dialogBuilder.create()
        alert.show()*/

        //alert.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundResource(R.drawable.ic_baseline_add_photo_gallery_48) //TODO-Q : Comment mettre des drawable ?
        //alert.getButton(DialogInterface.BUTTON_NEGATIVE).setBackgroundResource(R.drawable.ic_baseline_add_photo_camera_48)
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