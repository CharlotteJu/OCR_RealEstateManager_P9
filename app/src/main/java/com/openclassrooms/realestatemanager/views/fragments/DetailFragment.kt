package com.openclassrooms.realestatemanager.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager


import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.CompleteHousing
import com.openclassrooms.realestatemanager.viewModels.DetailViewModel
import com.openclassrooms.realestatemanager.views.adapters.ListEstateAgentAdapter
import com.openclassrooms.realestatemanager.views.adapters.ListPhotoAdapter
import com.openclassrooms.realestatemanager.views.adapters.ListPoiAdapter
import kotlinx.android.synthetic.main.fragment_add_housing.view.*
import kotlinx.android.synthetic.main.fragment_detail.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment() {

    lateinit var refString : String //= arguments?.getString("reference")

    lateinit var ref : String// = DetailFragmentArgs.fromBundle(this.requireArguments()).reference

    private val m_ViewModel : DetailViewModel  by viewModel()
    private lateinit var housing : CompleteHousing
    private lateinit var m_View : View




    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        if (arguments!= null)
        {
            ref = requireArguments().getString("reference").toString()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        m_View = inflater.inflate(R.layout.fragment_detail, container, false)
        this.getDataFromLiveData()
        return m_View
    }

    private fun getDataFromLiveData()
    {
        val debug = ref
        m_ViewModel.getHousing(ref).observe(viewLifecycleOwner, Observer {
                housing = it
                showDesign()
        })
    }

    private fun showDesign()
    {
        this.showTypeAndPrice()
        this.showStateDateAndRef()
        this.showAddress()
        this.showDescription()
        this.showPoi()
        //this.showEstateAgent()
        //this.showPhoto()
    }

    private fun showTypeAndPrice()
    {
        this.m_View.detail_fragment_type_txt.text = housing.housing.type
        this.m_View.detail_fragment_price_txt.text = housing.housing.price.toString() //TODO : Rajouter la device
    }

    private fun showInfoInsideHouse()
    {
        this.m_View.detail_fragment_area_txt.text = housing.housing.area.toString()

        if (housing.housing.rooms != null) { this.m_View.detail_fragment_number_rooms_txt.text = housing.housing.rooms.toString() }
        else { this.m_View.detail_fragment_number_rooms_txt.visibility = View.INVISIBLE }

        if (housing.housing.bedrooms != null) { this.m_View.detail_fragment_bedrooms_txt.text = housing.housing.bedrooms.toString() }
        else { this.m_View.detail_fragment_bedrooms_txt.visibility = View.INVISIBLE }

        if (housing.housing.bathrooms != null) { this.m_View.detail_fragment_bathrooms_txt.text = housing.housing.bathrooms.toString() }
        else { this.m_View.detail_fragment_bathrooms_txt.visibility = View.INVISIBLE }
    }

    private fun showStateDateAndRef()
    {
        this.m_View.detail_fragment_state_txt.text = housing.housing.state
        this.m_View.detail_fragment_date_entry_txt.text = housing.housing.dateEntry

        if (housing.housing.dateSale != null) { this.m_View.detail_fragment_date_sale_txt.text = housing.housing.dateSale }
        else { this.m_View.detail_fragment_date_sale_txt.visibility = View.INVISIBLE }

        this.m_View.detail_fragment_reference_txt.text = housing.housing.ref
    }

    private fun showAddress()
    {
        if (housing.address != null)
        {
            this.m_View.detail_fragment_address_txt.text = housing.address.toString()
            //TODO : Afficher sur la map
        }
        else
        {
            this.m_View.detail_fragment_address_txt.visibility = View.GONE
            this.m_View.detail_fragment_address_map_image.visibility = View.GONE
        }
    }

    private fun showDescription()
    {
        if (housing.housing.description != null)
        {
            this.m_View.detail_fragment_description_txt.text = housing.housing.description
        }
        else
        {
            this.m_View.detail_fragment_description_title_txt.visibility = View.GONE
            this.m_View.detail_fragment_description_txt.visibility = View.GONE
        }
    }

    private fun showPoi()
    {
        if (housing.poiList != null)
        {
            val poiList = housing.poiList!!.toList()
            val adapter = ListPoiAdapter(poiList)
            this.m_View.detail_fragment_rcv_poi.adapter = adapter
            this.m_View.detail_fragment_rcv_poi.layoutManager = LinearLayoutManager(context)
        }
        else
        {
            this.m_View.detail_fragment_poi_title_txt.visibility = View.GONE
            this.m_View.detail_fragment_rcv_poi.visibility = View.GONE
        }
    }

    private fun showEstateAgent()
    {
        if (housing.estateAgentList != null)
        {
            val estateList = housing.estateAgentList!!.toList()
            val adapter = ListEstateAgentAdapter(estateList)
            this.m_View.add_housing_fragment_estate_agent_rcv.adapter = adapter
            this.m_View.add_housing_fragment_estate_agent_rcv.layoutManager = LinearLayoutManager(context)
        }
        else
        {
            this.m_View.detail_fragment_estate_agent_title_txt.visibility = View.GONE
            this.m_View.detail_fragment_rcv_estate_agent.visibility = View.GONE
        }
    }

    private fun showPhoto()
    {
        if (housing.photoList != null)
        {
            val photoList = housing.photoList!!.toList()
            val adapter = ListPhotoAdapter(photoList)
            this.m_View.detail_fragment_rcv_photo.adapter = adapter
            this.m_View.detail_fragment_rcv_photo.layoutManager = LinearLayoutManager(context)
        }
        else
        {
            this.m_View.detail_fragment_rcv_photo.visibility = View.GONE
        }
    }

}
