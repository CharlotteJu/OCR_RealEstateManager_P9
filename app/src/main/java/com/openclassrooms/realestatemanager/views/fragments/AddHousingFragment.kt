package com.openclassrooms.realestatemanager.views.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.*
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.viewModels.AddUpdateHousingViewModel
import com.openclassrooms.realestatemanager.views.adapters.ListPhotoAdapter
import com.openclassrooms.realestatemanager.views.adapters.NothingSelectedSpinnerAdapter
import kotlinx.android.synthetic.main.fragment_add_housing.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class AddHousingFragment : Fragment() {

    private var housing = Housing()
    private lateinit var m_View : View
    private var housingReference : String? = null
    private var address : Address? = null
    private var estateAgentList : List<EstateAgent> = ArrayList()
    private var photoList : List<Photo> = ArrayList()
    private val m_ViewModel : AddUpdateHousingViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        m_View = inflater.inflate(R.layout.fragment_add_housing, container, false)

        this.configureSpinners()
        this.enableViews(false)
        this.getAllInfo()

        this.m_View.add_housing_fragment_final_button.setOnClickListener {
            this.findNavController().navigate(R.id.listFragment)
            //this.addFinal()
        }

        return m_View
    }

    private suspend fun addFinal()
    {
        if (housingReference != null)
        {
            this.m_ViewModel.createHousing(housing)

            if (address != null)
            {
                this.m_ViewModel.createAddress(address!!)
            }

            for (estate in estateAgentList)
            {
                val estateAgent = HousingEstateAgent(housingReference!!, estate.lastName)
                this.m_ViewModel.createHousingEstateAgent(estateAgent)
            }

            for (photo in photoList)
            {
                this.m_ViewModel.createPhoto(photo)
            }

            //TODO : Chercher les POI
        }

    }

    private fun createRef()
    {

        if (housing.type != "" && housing.price != 0.0 && housing.area != 0.0)
        {
            housingReference = "${housing.type} + ${housing.price} + ${housing.area}"   //TODO : Revoir Ref
            this.enableViews(true)
        }
    }


    private fun configureSpinners()
    {
        this.m_View.add_housing_fragment_type_spinner.adapter = configureSpinnerAdapter(R.array.type_housing_spinner)
        this.m_View.add_housing_fragment_state_spinner.adapter = configureSpinnerAdapter(R.array.state_spinner)
        this.m_View.add_housing_fragment_price_currency_spinner.adapter = configureSpinnerAdapter(R.array.price_currency_spinner)
        this.m_View.add_housing_fragment_number_rooms_spinner.adapter = configureSpinnerAdapter(R.array.number_rooms)
        this.m_View.add_housing_fragment_number_bedrooms_spinner.adapter = configureSpinnerAdapter(R.array.number_rooms)
        this.m_View.add_housing_fragment_number_bathrooms_spinner.adapter = configureSpinnerAdapter(R.array.number_rooms)
        //TODO : NameSpinner

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
    }


    private fun getPrice()
    {
        this.m_View.add_housing_fragment_price_currency_spinner.isEnabled = false


        this.m_View.add_housing_fragment_price_editTxt.doAfterTextChanged {
            this.housing.price = it.toString().toDouble()
            this.m_View.add_housing_fragment_price_currency_spinner.isEnabled = true
        }

        this.m_View.add_housing_fragment_price_currency_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                parent?.let{
                    if (parent.getItemAtPosition(position).toString() == "€")
                    {
                        housing.price = Utils.convertEuroToDollar(housing.price.toInt()).toDouble()
                    }
                    createRef()}

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        /*if (this.getEditTxt(this.m_View.add_housing_fragment_price_editTxt) != null)
        {
            this.housing.price = this.m_View.add_housing_fragment_price_editTxt.toString().toDouble()
            this.m_View.add_housing_fragment_price_currency_spinner.isEnabled = true
            this.m_View.add_housing_fragment_price_currency_spinner.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id
                ->  val currency = parent.getItemAtPosition(position).toString()
                this.createRef()
                if (currency == "€")
                {
                    //TODO : Faire la conversion
                    housing.price = Utils.convertEuroToDollar(housing.price.toInt()).toDouble()
                }
            }
        }*/
    }



    private fun getInfoInsideHouse()
    {
        this.m_View.add_housing_fragment_area_editTxt.doAfterTextChanged {
            housing.area = it.toString().toDouble()
            this.createRef()
        }

        this.m_View.add_housing_fragment_number_rooms_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                parent?.let { housing.rooms = parent.getItemAtPosition(position).toString().toInt() }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        this.m_View.add_housing_fragment_number_bedrooms_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                parent?.let { housing.bedrooms = parent.getItemAtPosition(position).toString().toInt() }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        this.m_View.add_housing_fragment_number_bathrooms_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                parent?.let { housing.bathrooms = parent.getItemAtPosition(position).toString().toInt() }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        /*if (this.getEditTxt(this.m_View.add_housing_fragment_area_editTxt) != null)
        {
            housing.area = this.getEditTxt(this.m_View.add_housing_fragment_area_editTxt)?.toDouble()!!
            this.createRef()
        }
        housing.rooms = getItemSpinner(this.m_View.add_housing_fragment_number_rooms_spinner)?.toInt()
        housing.bedrooms = getItemSpinner(this.m_View.add_housing_fragment_number_bedrooms_spinner)?.toInt()
        housing.bathrooms = getItemSpinner(this.m_View.add_housing_fragment_number_bathrooms_spinner)?.toInt()*/
    }

    private fun getTypeAndState()
    {
        this.m_View.add_housing_fragment_type_spinner.onItemSelectedListener =  object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                parent?.let {
                    housing.type = parent.getItemAtPosition(position).toString()
                    createRef()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        this.m_View.add_housing_fragment_state_spinner.onItemSelectedListener =  object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                parent?.let { housing.state = parent.getItemAtPosition(position).toString() }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        /*if (this.getItemSpinner(this.m_View.add_housing_fragment_type_spinner) != null)
        {
            housing.type = this.getItemSpinner(this.m_View.add_housing_fragment_type_spinner)!!
            this.createRef()
        }
        if (this.getItemSpinner(this.m_View.add_housing_fragment_state_spinner) != null)
        {
            housing.state = this.getItemSpinner(this.m_View.add_housing_fragment_state_spinner)!!
        }*/
    }

    private fun getDescription()
    {
        this.m_View.add_housing_fragment_description_editTxt.doAfterTextChanged { housing.description = it.toString() }

        //housing.description = this.getEditTxt(this.m_View.add_housing_fragment_description_editTxt)
    }

    private fun getAddress()
    {
        var addressTxt : String? = null
        var zipCodeTxt : String? = null
        var cityTxt : String? = null
        var country : String? = null

        this.m_View.add_housing_fragment_address_editTxt.doAfterTextChanged { addressTxt = it.toString() }
        this.m_View.add_housing_fragment_zipCode_editTxt.doAfterTextChanged { zipCodeTxt = it.toString() }
        this.m_View.add_housing_fragment_city_editTxt.doAfterTextChanged { cityTxt = it.toString() }

        this.m_View.add_housing_fragment_country_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                parent?.let { country = parent.getItemAtPosition(position).toString() }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun getEstateAgents()
    {
        var estateAgentList = ArrayList<HousingEstateAgent>() //TODO-Q : Pourquoi val alors que je peux avoir à add ?
        lateinit var housingEstateAgent: HousingEstateAgent


        this.m_View.add_housing_fragment_estate_agent_button.isEnabled = false
        var name : String? = null
        this.m_View.add_housing_fragment_estate_agent_name_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                parent?.let {
                    name = parent.getItemAtPosition(position).toString()
                    housingEstateAgent = HousingEstateAgent(housingReference!!, name!!)
                    m_View.add_housing_fragment_estate_agent_button.isEnabled = true
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        this.m_View.add_housing_fragment_estate_agent_button.setOnClickListener {
            estateAgentList.add(housingEstateAgent)
            //TODO-Q : Remettre à 0 housingEstateAgent ET Spinners
        }

        //TODO-Q : Syntaxe OK ?

    }


    private fun getPhotos()
    {

    }

    private fun displayPhotoRcv(photoList : List<Photo>)
    {
        val adapter = ListPhotoAdapter(photoList)
        this.m_View.add_housing_fragment_photo_rcv.adapter = adapter
        this.m_View.add_housing_fragment_photo_rcv.layoutManager = LinearLayoutManager(context)
    }



    /**
     * Put isEnabled for some spinners once the ref is created
     */
    private fun enableViews(boolean : Boolean)
    {
        this.m_View.add_housing_fragment_number_rooms_spinner.isEnabled = boolean
        this.m_View.add_housing_fragment_number_bedrooms_spinner.isEnabled = boolean
        this.m_View.add_housing_fragment_number_bathrooms_spinner.isEnabled = boolean
        this.m_View.add_housing_fragment_estate_agent_name_spinner.isEnabled = boolean
        this.m_View.add_housing_fragment_state_spinner.isEnabled = boolean
        this.m_View.add_housing_fragment_address_editTxt.isEnabled = boolean
        this.m_View.add_housing_fragment_zipCode_editTxt.isEnabled = boolean
        this.m_View.add_housing_fragment_city_editTxt.isEnabled = boolean
        this.m_View.add_housing_fragment_country_spinner.isEnabled = boolean
        this.m_View.add_housing_fragment_description_editTxt.isEnabled = boolean
        this.m_View.add_housing_fragment_image_description_editTxt.isEnabled = boolean
    }

    private fun configureSpinnerAdapter(res : Int) : ArrayAdapter<CharSequence>?
    {
        return context?.let { ArrayAdapter.createFromResource(it, res, android.R.layout.simple_spinner_item).
        also {charSequence -> charSequence.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)}}


        /*val adapter = context?.let {
            ArrayAdapter.createFromResource(it, res, android.R.layout.simple_spinner_item)}
        //TODO : Prompt n'a pas marché + Fait un nullPointer à l'entrée dans le fragment

        return NothingSelectedSpinnerAdapter(adapter, R.layout.spinner_nothing_selected, context)*/

    }

    private fun getItemSpinner(spinner: Spinner) : String?
    {
        var string : String? = null
        //TODO-Q : Object ?
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                string = parent?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        return string
    }

    private fun getEditTxt(editText: EditText) : String?
    {
        var string : String? = null

        editText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                string = s.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })

        return string
    }




    /*private fun bindViews()
    {
        this.typeSpinner = m_View.add_housing_fragment_type_spinner
        this.priceSpinner = m_View.add_housing_fragment_price_currency_spinner
        this.roomsSpinner = m_View.add_housing_fragment_number_rooms_spinner
        this.bedRoomsSpinner = m_View.add_housing_fragment_number_bedrooms_spinner
        this.bathRoomsSpinner = m_View.add_housing_fragment_number_bathrooms_spinner
        this.stateSpinner = m_View.add_housing_fragment_state_spinner
        this.estateAgentNameSpinner = m_View.add_housing_fragment_estate_agent_name_spinner
        this.estateAgentFunctionSpinner = m_View.add_housing_fragment_estate_agent_function_spinner
        this.priceEditTxt = m_View.add_housing_fragment_price_editTxt
        this.areaEditTxt = m_View.add_housing_fragment_area_editTxt
        this.addressEditTxt = m_View.add_housing_fragment_address_editTxt
        this.descriptionEditTxt = m_View.add_housing_fragment_description_editTxt
        this.estateAgentButton = m_View.add_housing_fragment_estate_agent_button
        this.photoButton = m_View.add_housing_fragment_photo_button
        this.photoImage = m_View.add_housing_fragment_photo_image
        this.photoDescriptionEditTxt = m_View.add_housing_fragment_image_description_editTxt
        this.estateAgentRcv = m_View.add_housing_fragment_estate_agent_rcv
        this.photoRcv = m_View.add_housing_fragment_photo_rcv
    }*/


}