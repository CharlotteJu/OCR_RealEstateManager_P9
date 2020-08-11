package com.openclassrooms.realestatemanager.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.*
import com.openclassrooms.realestatemanager.utils.*
import com.openclassrooms.realestatemanager.viewModels.AddUpdateHousingViewModel
import com.openclassrooms.realestatemanager.views.adapters.ListEstateAgentAdapter
import com.openclassrooms.realestatemanager.views.adapters.ListPhotoAdapter
import kotlinx.android.synthetic.main.fragment_add_housing.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList


class AddHousingFragment : BaseFragment() {

    private lateinit var housing : Housing
    private lateinit var mView : View
    private lateinit var housingReference : String
    private lateinit var currency : String
    private var address : Address? = null
    private var estateAgentList : MutableList<HousingEstateAgent> = ArrayList()
    private lateinit var mAdapterEstateAgentRcv : ListEstateAgentAdapter
    private var photoList : List<Photo> = ArrayList()
    private val mViewModel : AddUpdateHousingViewModel by viewModel()
    private lateinit var mApiKey : String
    private lateinit var placesClient : PlacesClient // TODO : Vraiment besoin de ça ?


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments!= null)
        {
            housingReference = requireArguments().getString("reference").toString()
            housing = Housing(ref = housingReference)
        }
        this.currency = getCurrencyFromSharedPreferences()
        this.mApiKey = resources.getString(R.string.google_api_key)
        this.mAdapterEstateAgentRcv = ListEstateAgentAdapter(estateAgentList)
        Places.initialize(requireContext(), mApiKey)
        this.placesClient = Places.createClient(requireContext())


        //housingReference = UUID.randomUUID().toString()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        mView = inflater.inflate(R.layout.fragment_add_housing, container, false)

        this.getEstateAgentList()
        this.configureSpinners()
        this.getAllInfo()
        this.displayEstateAgentRcv()

        this.mView.add_housing_fragment_final_button.visibility = View.INVISIBLE
        this.mView.add_housing_fragment_final_button.isEnabled = false
        this.mView.add_housing_fragment_final_button.setOnClickListener {
            this.addFinal()
            this.findNavController().navigate(R.id.listFragment)
        }

        return mView
    }

    private fun addFinal()
    {
        this.checkAddress()
        context?.let { this.mViewModel.createGlobalHousing(housing, address, photoList, estateAgentList, it, mApiKey) }

        //TODO : Si connecté à internet --> Push sur Firebase
    }



    private fun getEstateAgentList()
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

    private fun getInfoInsideHouse()
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
                    if (item != SPINNER_SELECT) {
                        housing.state = item
                        enableFinalButton()
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

    private fun checkAddress()
    {
        if (address!!.street == STRING_EMPTY && address!!.city == STRING_EMPTY && address!!.country == STRING_EMPTY)
        {
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
            }
            //TODO : Afficher RCV et remettre spinner à 0
        }
    }


    private fun getPhotos()
    {

    }

    private fun displayEstateAgentRcv()
    {

        this.mView.add_housing_fragment_estate_agent_rcv.adapter = mAdapterEstateAgentRcv
        this.mView.add_housing_fragment_estate_agent_rcv.layoutManager = LinearLayoutManager(context)
    }

    private fun displayPhotoRcv()
    {
        val adapter = ListPhotoAdapter(photoList)
        this.mView.add_housing_fragment_photo_rcv.adapter = adapter
        this.mView.add_housing_fragment_photo_rcv.layoutManager = LinearLayoutManager(context)
    }

    private fun enableFinalButton()
    {
        if (housing.type != STRING_EMPTY && housing.price != DOUBLE_00 && housing.area != DOUBLE_00 && housing.state!= STRING_EMPTY )
        {
            this.mView.add_housing_fragment_final_button.visibility = View.VISIBLE
            this.mView.add_housing_fragment_final_button.isEnabled = true
        }
    }

    private fun getDateOfToday()
    {
        val date = Calendar.getInstance().time
        val stringDate = Utils.getTodayDateGood(date)
        housing.dateEntry = stringDate
    }

    private fun configureSpinnerAdapter(res : Int) : ArrayAdapter<CharSequence>?
    {
        return context?.let { ArrayAdapter.createFromResource(it, res, android.R.layout.simple_spinner_item).
                        also {charSequence -> charSequence.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)}}
    }


}