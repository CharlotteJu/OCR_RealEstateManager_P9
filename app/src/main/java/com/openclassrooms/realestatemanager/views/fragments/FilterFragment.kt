package com.openclassrooms.realestatemanager.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.utils.SPINNER_SELECT
import com.openclassrooms.realestatemanager.utils.STRING_EMPTY
import kotlinx.android.synthetic.main.fragment_add_housing.view.*
import kotlinx.android.synthetic.main.fragment_filter.view.*

class FilterFragment : Fragment() {

    private lateinit var mView : View
    private var type : String? = null
    private var priceLower : Double? = null
    private var priceHigher : Double? = null
    private var areaLower : Double? = null
    private var areaHigher : Double? = null
    private var roomLower : Int? = null
    private var roomHigher : Int? = null
    private var bedRoomLower : Int? = null
    private var bedRoomHigher : Int? = null
    private var bathRoomLower : Int? = null
    private var bathRoomHigher : Int? = null
    private var state : String? = null
    private var dateEntry : String? = null
    private var dateSale : String? = null
    private var city : String? = null
    private var country : String? = null
    private var typePoi : String? = null
    private var numberPhotos : Int? = null
    private var estateAgent : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        this.mView = inflater.inflate(R.layout.fragment_filter, container, false)

        return this.mView
    }

    private fun configureSpinners()
    {
        this.mView.fragment_filter_type_spinner.adapter = configureSpinnerAdapter(R.array.type_housing_spinner)
        this.mView.fragment_filter_state_spinner.adapter = configureSpinnerAdapter(R.array.state_spinner)
        this.mView.fragment_filter_country_spinner.adapter = configureSpinnerAdapter(R.array.country_spinner)
        this.mView.fragment_filter_around_poi_spinner.adapter = configureSpinnerAdapter(R.array.type_poi)
        //TODO : this.mView.fragment_filter_estate_agent_name_spinner.adapter = configureSpinnerAdapter(R.array.type_poi)
        this.mView.fragment_filter_nb_photo_spinner.adapter = configureSpinnerAdapter(R.array.number_rooms)
    }

    private fun getAllInfo()
    {
        this.getTypeAndState()
        this.getInfoInsideHouse()
        this.getAddress()
        this.getDates()
        this.getPoi()
        this.getEstateAgent()
        this.getNbPhotos()
    }

    private fun getTypeAndState()
    {
        this.mView.fragment_filter_type_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                parent?.let {
                    val item = parent.getItemAtPosition(position)
                    if (item != STRING_EMPTY) type = item.toString()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        this.mView.fragment_filter_state_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                parent?.let {
                    val item = parent.getItemAtPosition(position)
                    if (item != STRING_EMPTY) state = item.toString()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun getInfoInsideHouse()
    {
        this.mView.fragment_filter_price_slider.values = listOf(0f, 1000000000f)
        this.mView.fragment_filter_price_slider.addOnChangeListener { slider, value, fromUser ->
            priceHigher = value.toDouble()
        }
    }

    private fun getDates()
    {

    }

    private fun getAddress()
    {
        this.mView.fragment_filter_city_editTxt.doAfterTextChanged {
            if (it != null) city = it.toString()
        }

        this.mView.fragment_filter_country_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                parent?.let {
                    val item = parent.getItemAtPosition(position)
                    if (item != STRING_EMPTY) country = item.toString()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun getPoi()
    {
        this.mView.fragment_filter_around_poi_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                parent?.let {
                    val item = parent.getItemAtPosition(position)
                    if (item != STRING_EMPTY) typePoi = item.toString()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun getEstateAgent()
    {
        //TODO :
        /*this.mViewModel.getEstateAgentList().observe(this.viewLifecycleOwner, Observer { list ->

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
        })*/
        this.mView.fragment_filter_estate_agent_name_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                parent?.let {
                    val item = parent.getItemAtPosition(position)
                    if (item != STRING_EMPTY) estateAgent = item.toString()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun getNbPhotos()
    {
        this.mView.fragment_filter_nb_photo_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                parent?.let {
                    val item = parent.getItemAtPosition(position)
                    if (item != STRING_EMPTY) numberPhotos = item.toString().toInt()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun configureSpinnerAdapter(res : Int) : ArrayAdapter<CharSequence>?
    {
        return context?.let { ArrayAdapter.createFromResource(it, res, android.R.layout.simple_spinner_item).
        also {charSequence -> charSequence.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)}}
    }


}