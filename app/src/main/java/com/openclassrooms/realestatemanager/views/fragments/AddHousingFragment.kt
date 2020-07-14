package com.openclassrooms.realestatemanager.views.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.observe
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.*
import com.openclassrooms.realestatemanager.utils.SPINNER_SELECT
import com.openclassrooms.realestatemanager.utils.STRING_EMPTY
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.viewModels.AddUpdateHousingViewModel
import com.openclassrooms.realestatemanager.views.adapters.ListPhotoAdapter
import kotlinx.android.synthetic.main.fragment_add_housing.view.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.ext.scope
import java.util.*
import kotlin.collections.ArrayList


class AddHousingFragment : Fragment() {

    private lateinit var housing : Housing
    private lateinit var mView : View
    //private var housingReference : String? = null
    private var address : Address? = null
    private var estateAgentList : List<EstateAgent> = ArrayList()
    private var photoList : List<Photo> = ArrayList()
    private val mViewModel : AddUpdateHousingViewModel by viewModel()
    private lateinit var housingReference : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments!= null)
        {
            housingReference = requireArguments().getString("reference").toString()
            housing = Housing(ref = housingReference)
        }

        //housingReference = UUID.randomUUID().toString()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        mView = inflater.inflate(R.layout.fragment_add_housing, container, false)

        this.getEstateAgentList()
        this.configureSpinners()
        //this.enableViews(false)
        this.getAllInfo()

        this.mView.add_housing_fragment_final_button.setOnClickListener {
            this.coroutine()
            this.findNavController().navigate(R.id.listFragment)
        }

        return mView
    }

    private fun coroutine() = runBlocking {

       // mViewModel.createHousing(housing)

        val test = launch {
            mViewModel.createHousing(housing)
        }

        test.join()

        launch { addFinal() }

       /* val housingTest = mViewModel.getHousing(housingReference).observe(viewLifecycleOwner, Observer {
            val debug = it
            val test2 = launch { addFinal() }
        })*/




    }

    private suspend fun addFinal()
    {

       // mViewModel.createHousing(housing)

        if (address != null)
        {
            mViewModel.createAddress(address!!)
        }

        for (estate in estateAgentList)
        {
            val estateAgent = HousingEstateAgent(housingReference!!, estate.lastName)
            mViewModel.createHousingEstateAgent(estateAgent)
        }

        for (photo in photoList)
        {
            mViewModel.createPhoto(photo)
        }

        //TODO : Chercher les POI

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
        this.mView.add_housing_fragment_type_spinner.setSelection(2)
        this.mView.add_housing_fragment_state_spinner.adapter = configureSpinnerAdapter(R.array.state_spinner)
        this.mView.add_housing_fragment_state_spinner.prompt = getString(R.string.spinners_state)
        this.mView.add_housing_fragment_price_currency_spinner.adapter = configureSpinnerAdapter(R.array.price_currency_spinner)
        this.mView.add_housing_fragment_price_currency_spinner.prompt = getString(R.string.spinners_currency)
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
    }

    private fun getPrice()
    {
        this.mView.add_housing_fragment_price_currency_spinner.isEnabled = false

        this.mView.add_housing_fragment_price_editTxt.doAfterTextChanged {
            this.housing.price = it.toString().toDouble()
            this.mView.add_housing_fragment_price_currency_spinner.isEnabled = true
        }

        this.mView.add_housing_fragment_price_currency_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                parent?.let {
                    val item : String = parent.getItemAtPosition(position).toString()
                    if (item == "€") housing.price = Utils.convertEuroToDollar(housing.price.toInt()).toDouble()}
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun getInfoInsideHouse()
    {
        this.mView.add_housing_fragment_area_editTxt.doAfterTextChanged { housing.area = it.toString().toDouble() }

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
                    if (item != SPINNER_SELECT) housing.type = item
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
                    if (item != SPINNER_SELECT) housing.state = item
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

    private fun getEstateAgents()
    {
        val estateAgentList = ArrayList<HousingEstateAgent>()
        var housingEstateAgent: HousingEstateAgent? = null
        var name : String? = null

        this.mView.add_housing_fragment_estate_agent_button.isEnabled = false

        this.mView.add_housing_fragment_estate_agent_name_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                parent?.let {
                    val item : String = parent.getItemAtPosition(position).toString()
                    if (item != SPINNER_SELECT)
                    {
                        name = item
                        housingEstateAgent = HousingEstateAgent(housingReference, name!!)
                        mView.add_housing_fragment_estate_agent_button.isEnabled = true
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        this.mView.add_housing_fragment_estate_agent_button.setOnClickListener {
            if (housingEstateAgent != null) estateAgentList.add(housingEstateAgent!!)
        }
    }


    private fun getPhotos()
    {

    }

    private fun displayPhotoRcv(photoList : List<Photo>)
    {
        val adapter = ListPhotoAdapter(photoList)
        this.mView.add_housing_fragment_photo_rcv.adapter = adapter
        this.mView.add_housing_fragment_photo_rcv.layoutManager = LinearLayoutManager(context)
    }

    private fun enableFinalButton()
    {
        if (housing.type != "" && housing.price != 0.0 && housing.area != 0.0 && housing.state!= "" )
        {
            this.mView.add_housing_fragment_final_button.isEnabled = true // TODO-Q : Ou appeler ? --> Listener spinner
        }
    }

    private fun configureSpinnerAdapter(res : Int) : ArrayAdapter<CharSequence>?
    {
        return context?.let { ArrayAdapter.createFromResource(it, res, android.R.layout.simple_spinner_item).
                        also {charSequence -> charSequence.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)}}

        /*val adapter = context?.let {
            ArrayAdapter.createFromResource(it, res, android.R.layout.simple_spinner_item)}
            return NothingSelectedSpinnerAdapter(adapter, R.layout.spinner_nothing_selected, context)*/
    }

    /*private fun getItemSpinner(spinner: Spinner) : String?
    {
        var string : String? = null
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
    }*/

}