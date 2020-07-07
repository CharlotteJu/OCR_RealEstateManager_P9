package com.openclassrooms.realestatemanager.views.fragments

import android.os.Bundle
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

    private fun configureSpinnerAdapter(res : Int) : ArrayAdapter<CharSequence>?
    {
        return context?.let { ArrayAdapter.createFromResource(it, res, android.R.layout.simple_spinner_item).also {
            adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) } }
    }

    private fun getAllInfo()
    {
        //TODO : CASTER LES EDIT VIDES !
        this.getPrice()
        this.getInfoInsideHouse()
        this.getAddress()
        this.getDescription()
        this.getTypeAndState()
        this.getPhotos()
        this.getEstateAgents()
    }


    private fun getPrice()
    {
        if (this.m_View.add_housing_fragment_price_editTxt.text.toString() != "")
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
        housing.area = this.m_View.add_housing_fragment_area_editTxt.text.toString().toDouble()

        this.m_View.add_housing_fragment_number_rooms_spinner.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id
            -> housing.rooms = parent.getItemAtPosition(position).toString().toInt() }
        this.m_View.add_housing_fragment_number_bedrooms_spinner.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id
            -> housing.bedrooms = parent.getItemAtPosition(position).toString().toInt() }
        this.m_View.add_housing_fragment_number_bathrooms_spinner.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id
            -> housing.bathrooms = parent.getItemAtPosition(position).toString().toInt() }
    }

    private fun getTypeAndState()
    {
        this.m_View.add_housing_fragment_type_spinner.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id
            -> housing.type =  parent.getItemAtPosition(position).toString()}
        this.m_View.add_housing_fragment_state_spinner.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id
            -> housing.state = parent.getItemAtPosition(position).toString()  }
    }

    private fun getDescription()
    {
        housing.description = this.m_View.add_housing_fragment_description_editTxt.text.toString()
    }

    private fun getAddress()
    {
        val addressTxt = this.m_View.add_housing_fragment_address_editTxt.text.toString()
        val zipCodeTxt = this.m_View.add_housing_fragment_zipCode_editTxt.text.toString()
        val cityTxt = this.m_View.add_housing_fragment_city_editTxt.text.toString()
        lateinit var country : String

        this.m_View.add_housing_fragment_country_spinner.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id
            ->  country = parent.getItemAtPosition(position).toString()}


        // TODO : Voir si on rajoute le reste ou pas
        // TODO : VIEW MODEL
    }

    private fun getEstateAgents()
    {
        val estateAgentList = ArrayList<HousingEstateAgent>() //TODO-Q : Pourquoi val alors que je peux avoir à add ?
        lateinit var housingEstateAgent: HousingEstateAgent


        this.m_View.add_housing_fragment_estate_agent_button.isEnabled = false
        this.m_View.add_housing_fragment_estate_agent_function_spinner.isEnabled = false

        this.m_View.add_housing_fragment_estate_agent_name_spinner.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id
            -> val name = parent.getItemAtPosition(position).toString()
            housingEstateAgent = HousingEstateAgent(housingReference, name, null)
            this.m_View.add_housing_fragment_estate_agent_function_spinner.isEnabled = true
            this.m_View.add_housing_fragment_estate_agent_button.isEnabled = true
        }

        this.m_View.add_housing_fragment_estate_agent_function_spinner.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id
            ->  val function = parent.getItemAtPosition(position).toString()
            housingEstateAgent.function = function
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
     * Put isClickable for some spinners once the ref is created
     */
    private fun enableViews(boolean : Boolean)
    {
        this.m_View.add_housing_fragment_number_rooms_spinner.isEnabled = boolean
        this.m_View.add_housing_fragment_number_bedrooms_spinner.isEnabled = boolean
        this.m_View.add_housing_fragment_number_bathrooms_spinner.isEnabled = boolean
        this.m_View.add_housing_fragment_estate_agent_name_spinner.isEnabled = boolean
        this.m_View.add_housing_fragment_address_editTxt.isEnabled = boolean
        this.m_View.add_housing_fragment_zipCode_editTxt.isEnabled = boolean
        this.m_View.add_housing_fragment_city_editTxt.isEnabled = boolean
        this.m_View.add_housing_fragment_country_spinner.isEnabled = boolean
        this.m_View.add_housing_fragment_description_editTxt.isEnabled = boolean
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