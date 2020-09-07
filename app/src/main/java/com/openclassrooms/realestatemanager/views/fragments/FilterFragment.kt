package com.openclassrooms.realestatemanager.views.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.google.android.material.slider.RangeSlider
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.CompleteHousing
import com.openclassrooms.realestatemanager.utils.*
import com.openclassrooms.realestatemanager.viewModels.FilterViewModel
import com.openclassrooms.realestatemanager.views.activities.MainActivity
import com.openclassrooms.realestatemanager.views.adapters.ListHousingAdapter
import com.openclassrooms.realestatemanager.views.adapters.OnClickDelete
import com.openclassrooms.realestatemanager.views.adapters.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_add_housing.view.*
import kotlinx.android.synthetic.main.fragment_filter.view.*
import kotlinx.android.synthetic.main.fragment_list.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

class FilterFragment : BaseFragment(), OnItemClickListener, OnClickDelete {


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
    private var dateEntry : Long? = null
    private var dateSale : Long? = null
    private var city : String? = null
    private var country : String? = null
    private var typePoi : String? = null
    private var numberPhotos : Int? = null
    private var estateAgent : String? = null
    private var listFilter = ArrayList<CompleteHousing>()
    private lateinit var currency : String

    private val mViewModel : FilterViewModel by viewModel()
    private lateinit var mView : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.currency = this.getCurrencyFromSharedPreferences()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        this.mView = inflater.inflate(R.layout.fragment_filter, container, false)
        this.configureSpinners()
        this.getAllInfo()

        this.mView.fragment_filter_search_fab.setOnClickListener {
            this.launchSearch()
        }


        return this.mView
    }

    override fun onResume() {
        super.onResume()
        this.mView.fragment_filter_search_fab.setOnClickListener {
            this.launchSearch()
        }
    }

    private fun launchSearch()
    {
        if (currency == EURO)
        {
            if (priceLower != null) priceLower = Utils.convertDollarToEuroDouble(priceLower!!)
            if (priceHigher != null) priceHigher = Utils.convertDollarToEuroDouble(priceHigher!!)
        }

        this.mViewModel.getListFilter(type, priceLower, priceHigher, areaLower, areaHigher,
                roomLower, roomHigher, bedRoomLower, bedRoomHigher, bathRoomLower, bathRoomHigher,
                state, dateEntry, dateSale, city, country, typePoi, numberPhotos, estateAgent)
                .observe(viewLifecycleOwner, Observer {
                   listFilter = it as ArrayList<CompleteHousing>
                    configRecyclerView(it)
                })
    }

    private fun configRecyclerView(housingList : List<CompleteHousing>)
    {
        this.mView.fragment_filter_rcv.adapter = ListHousingAdapter(housingList, this, this, this.currency, Utils.isInternetAvailableGood(context), requireContext())
        this.mView.fragment_filter_rcv.layoutManager = LinearLayoutManager(context)
    }

    private fun configureSpinners()
    {
        this.mView.fragment_filter_type_spinner.adapter = configureSpinnerAdapter(R.array.type_housing_spinner)
        this.mView.fragment_filter_state_spinner.adapter = configureSpinnerAdapter(R.array.state_spinner)
        this.mView.fragment_filter_country_spinner.adapter = configureSpinnerAdapter(R.array.country_spinner)
        this.mView.fragment_filter_around_poi_spinner.adapter = configureSpinnerAdapter(R.array.type_poi_spinner)
        this.mView.fragment_filter_nb_photo_spinner.adapter = configureSpinnerAdapter(R.array.number_rooms)
    }

    private fun getAllInfo()
    {
        this.getTypeAndState()
        this.getPrice()
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
                    type = if (item != SPINNER_SELECT) item.toString()
                    else null
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        this.mView.fragment_filter_state_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                parent?.let {
                    val item = parent.getItemAtPosition(position)
                    state = if (item != SPINNER_SELECT) item.toString()
                    else null
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun getPrice()
    {
        this.mView.fragment_filter_price_min_editTxt.doAfterTextChanged {
            if (it.toString().isNotEmpty())
            {
                this.priceLower = it.toString().toDouble()
            }
            else this.priceLower = null
        }
        this.mView.fragment_filter_price_max_editTxt.doAfterTextChanged {
            if (it.toString().isNotEmpty())
            {
                this.priceHigher = it.toString().toDouble()
            }
            else this.priceHigher = null
        }

    }

    private fun getInfoInsideHouse()
    {
        if (this.mView.fragment_filter_area_slider.isPressed)
        {
            this.mView.fragment_filter_area_slider.addOnChangeListener { slider, _, _ ->
                areaLower = slider.values[0].toDouble()
                areaHigher = slider.values[1].toDouble()
            }
        }

        if (this.mView.fragment_filter_rooms_slider.isPressed)
        {
            this.mView.fragment_filter_rooms_slider.addOnChangeListener { slider, _, _ ->
                roomLower = slider.values[0].toInt()
                roomHigher = slider.values[1].toInt()
            }
        }

        if (this.mView.fragment_filter_bedrooms_slider.isPressed)
        {
            this.mView.fragment_filter_bedrooms_slider.addOnChangeListener { slider, _, _ ->
                bedRoomLower = slider.values[0].toInt()
                bedRoomHigher = slider.values[1].toInt()
            }
        }

        if ( this.mView.fragment_filter_bathrooms_slider.isPressed)
        {
            this.mView.fragment_filter_bathrooms_slider.addOnChangeListener { slider, _, _ ->
                bathRoomLower = slider.values[0].toInt()
                bathRoomHigher = slider.values[1].toInt()
            }
        }
    }

    private fun getDates()
    {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        this.mView.fragment_filter_date_entry_button.setOnClickListener {
            val datePickerDialog = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val dayString = if (dayOfMonth < 10) "0$dayOfMonth"
                else dayOfMonth.toString()
                val month1 = month+1
                val monthString = if (month1 < 10) "0$month1"
                else month1.toString()

                val dateString = "$dayString/$monthString/$year"
                dateEntry = UtilsKotlin.convertStringToLongDate(dateString)
                mView.fragment_filter_date_entry_generated_txt.text = dateString
            }, year, month, dayOfMonth)
            datePickerDialog.show()
        }

        this.mView.fragment_filter_date_sale_button.setOnClickListener {
            val datePickerDialog = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val dayString = if (dayOfMonth < 10) "0$dayOfMonth"
                else dayOfMonth.toString()
                val month1 = month+1
                val monthString = if (month1 < 10) "0$month1"
                else month1.toString()

                val dateString = "$dayString/$monthString/$year"
                dateSale= UtilsKotlin.convertStringToLongDate(dateString)
                mView.fragment_filter_date_sale_generated_txt.text = dateString
            }, year, month, dayOfMonth)
            datePickerDialog.show()
        }
    }

    private fun getAddress()
    {
        this.mView.fragment_filter_city_editTxt.doAfterTextChanged {
            if (it.toString().isNotEmpty() || it.toString() != STRING_EMPTY) city = it.toString()
            else city = null
        }

        this.mView.fragment_filter_country_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                parent?.let {
                    val item = parent.getItemAtPosition(position)
                    country = if (item != SPINNER_SELECT) item.toString()
                    else null
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
                    typePoi = if (item != SPINNER_SELECT) item.toString()
                    else null
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun getEstateAgent()
    {
        this.mViewModel.getEstateAgentList().observe(this.viewLifecycleOwner, androidx.lifecycle.Observer { list ->

            val nameList = ArrayList<String>()

            if (list.isNotEmpty() && list[0].lastName != SPINNER_SELECT ) nameList.add(SPINNER_SELECT)
            for (i in list)
            {
                nameList.add(i.lastName)
            }

            val adapter = context?.let {
                ArrayAdapter(it, android.R.layout.simple_spinner_item, nameList)
                        .also {charSequence -> charSequence.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        }
            }
            this.mView.fragment_filter_estate_agent_name_spinner.adapter = adapter
        })

        this.mView.fragment_filter_estate_agent_name_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                parent?.let {
                    val item = parent.getItemAtPosition(position)
                    estateAgent = if (item != SPINNER_SELECT)item.toString()
                    else null
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
                    numberPhotos = if (item != SPINNER_SELECT) item.toString().toInt()
                    else null
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

    override fun onItemClick(position: Int) {

        if (!this.getIsTabletFromSharedPreferences())
        {
            val bundle  = Bundle()
            bundle.putString(BUNDLE_REFERENCE, this.listFilter[position].housing.ref)
            findNavController().navigate(R.id.detailFragment, bundle)
        }
        else
        {
            val detailFragment = (activity as MainActivity).getDetailFragment()
            detailFragment?.updateRef(this.listFilter[position].housing.ref, requireContext()) //TODO : Voir pour split l'écran
        }
    }

    override fun onClickDeleteHousing(position: Int) {
        TODO("Not yet implemented")
    }


}