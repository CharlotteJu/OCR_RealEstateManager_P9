package com.openclassrooms.realestatemanager.views.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.*
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.views.adapters.ListPhotoAdapter
import com.openclassrooms.realestatemanager.views.adapters.NothingSelectedSpinnerAdapter
import kotlinx.android.synthetic.main.fragment_add_housing.view.*


class AddHousingFragment : Fragment() {

    private var housing = Housing()
    private lateinit var m_View : View
    private lateinit var housingReference : String

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
        this.createRef() //TODO : A appeler au bon moment
        return m_View
    }

    private fun createRef()
    {
        if (housing.type != "" && housing.price != 0.0 && housing.area != 0.0)
        {
            //TODO : Create REF
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
        //TODO : CASTER LES EDIT VIDES !
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
        if (this.getEditTxt(this.m_View.add_housing_fragment_price_editTxt) != null)
        {
            this.housing.price = this.m_View.add_housing_fragment_price_editTxt.toString().toDouble()
            this.m_View.add_housing_fragment_price_currency_spinner.isEnabled = true
            this.m_View.add_housing_fragment_price_currency_spinner.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id
                ->  val currency = parent.getItemAtPosition(position).toString()
                if (currency == "€")
                {
                    //TODO : Faire la conversion
                    housing.price = Utils.convertEuroToDollar(housing.price.toInt()).toDouble()
                }
            }
        }
    }



    private fun getInfoInsideHouse()
    {
        if (this.getEditTxt(this.m_View.add_housing_fragment_area_editTxt) != null)
        {
            housing.area = this.getEditTxt(this.m_View.add_housing_fragment_area_editTxt)?.toDouble()!!
        }
        housing.rooms = getItemSpinner(this.m_View.add_housing_fragment_number_rooms_spinner)?.toInt()
        housing.bedrooms = getItemSpinner(this.m_View.add_housing_fragment_number_bedrooms_spinner)?.toInt()
        housing.bathrooms = getItemSpinner(this.m_View.add_housing_fragment_number_bathrooms_spinner)?.toInt()
    }

    private fun getTypeAndState()
    {

        if (this.getItemSpinner(this.m_View.add_housing_fragment_type_spinner) != null)
        {
            housing.type = this.getItemSpinner(this.m_View.add_housing_fragment_type_spinner)!!
        }
        if (this.getItemSpinner(this.m_View.add_housing_fragment_state_spinner) != null)
        {
            housing.state = this.getItemSpinner(this.m_View.add_housing_fragment_state_spinner)!!
        }
    }

    private fun getDescription()
    {
        housing.description = this.getEditTxt(this.m_View.add_housing_fragment_description_editTxt)
    }

    private fun getAddress()
    {
        val addressTxt = this.getEditTxt(this.m_View.add_housing_fragment_address_editTxt)
        val zipCodeTxt = this.getEditTxt(this.m_View.add_housing_fragment_zipCode_editTxt)
        val cityTxt = this.getEditTxt(this.m_View.add_housing_fragment_city_editTxt)
        var country : String? = null

        if (this.getItemSpinner(this.m_View.add_housing_fragment_country_spinner) != null)
        {
            country = this.getItemSpinner(this.m_View.add_housing_fragment_country_spinner)!!
        }

        // TODO : Voir si on rajoute le reste ou pas
        // TODO : VIEW MODEL
    }

    private fun getEstateAgents()
    {
        var estateAgentList = ArrayList<HousingEstateAgent>() //TODO-Q : Pourquoi val alors que je peux avoir à add ?
        lateinit var housingEstateAgent: HousingEstateAgent


        this.m_View.add_housing_fragment_estate_agent_button.isEnabled = false
        val name = this.getItemSpinner(this.m_View.add_housing_fragment_estate_agent_name_spinner)
        if (name != null)
        {
            housingEstateAgent = HousingEstateAgent(housingReference, name)
            this.m_View.add_housing_fragment_estate_agent_button.isEnabled = true
        }

        this.m_View.add_housing_fragment_estate_agent_button.setOnClickListener {
            estateAgentList.add(housingEstateAgent)

            //TODO-Q : Remettre à 0 housingEstateAgent ET Spinners
            //TODO : VIEW MODEL
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

    private fun configureSpinnerAdapter(res : Int) : NothingSelectedSpinnerAdapter?
    {
        /*return context?.let { ArrayAdapter.createFromResource(it, res, android.R.layout.simple_spinner_item).also {
            adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) } }*/
        val adapter = context?.let {
            ArrayAdapter.createFromResource(it, res, android.R.layout.simple_spinner_item)}

        return NothingSelectedSpinnerAdapter(adapter, R.layout.spinner_nothing_selected, context)

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