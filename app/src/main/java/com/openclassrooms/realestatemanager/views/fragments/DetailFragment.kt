package com.openclassrooms.realestatemanager.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.CompleteHousing
import com.openclassrooms.realestatemanager.utils.*
import com.openclassrooms.realestatemanager.viewModels.DetailViewModel
import com.openclassrooms.realestatemanager.views.adapters.ListEstateAgentAdapter
import com.openclassrooms.realestatemanager.views.adapters.ListPhotoDetailAdapter
import com.openclassrooms.realestatemanager.views.adapters.ListPoiAdapter
import kotlinx.android.synthetic.main.fragment_detail.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : BaseFragment() {


    private lateinit var ref : String// = DetailFragmentArgs.fromBundle(this.requireArguments()).reference
    private val mViewModel : DetailViewModel  by viewModel()
    private lateinit var housing : CompleteHousing
    private lateinit var mView : View
    private lateinit var notSpecify : String // = getString(R.string.not_specify)
    private lateinit var currency: String


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        if (arguments!= null)
        {
            ref = requireArguments().getString(BUNDLE_REFERENCE).toString()
        }
        notSpecify = getString(R.string.not_specify)
        currency = getCurrencyFromSharedPreferences()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        mView = inflater.inflate(R.layout.fragment_detail, container, false)
        this.getDataFromLiveData()

        mView.detail_fragment_edit_button.setOnClickListener {
            val bundle  = Bundle()
            bundle.putString(BUNDLE_REFERENCE, housing.housing.ref)
            findNavController().navigate(R.id.editHousingFragment, bundle) //TODO : Trouver comment choisir la classe chargée
        }
        return mView
    }

    private fun getDataFromLiveData()
    {
        mViewModel.getCompleteHousing(ref).observe(viewLifecycleOwner, Observer {
                housing = it
                showDesign()
        })
    }

    private fun showDesign()
    {
        this.showTypeAndPrice()
        this.showStateAndDate()
        this.showInfoInsideHouse()
        this.showAddress()
        this.showDescription()
        this.showPoi()
        this.showEstateAgent()
        this.showPhoto()
    }

    private fun showTypeAndPrice()
    {
        this.mView.detail_fragment_type_txt.text = housing.housing.type

        val stringPrice = Utils.getPriceString(currency, housing.housing.price)
        this.mView.detail_fragment_price_txt.text = stringPrice
    }

    private fun showInfoInsideHouse()
    {
        this.mView.detail_fragment_area_txt.text = housing.housing.area.toString()

        if (housing.housing.rooms != null) { this.mView.detail_fragment_number_rooms_txt.text = housing.housing.rooms.toString() }
        else { this.mView.detail_fragment_number_rooms_txt.text = notSpecify }

        if (housing.housing.bedrooms != null) { this.mView.detail_fragment_bedrooms_txt.text = housing.housing.bedrooms.toString() }
        else { this.mView.detail_fragment_bedrooms_txt.text = notSpecify }

        if (housing.housing.bathrooms != null) { this.mView.detail_fragment_bathrooms_txt.text = housing.housing.bathrooms.toString() }
        else { this.mView.detail_fragment_bathrooms_txt.text = notSpecify }
    }

    private fun showStateAndDate()
    {
        this.mView.detail_fragment_state_txt.text = housing.housing.state
        this.mView.detail_fragment_date_entry_txt.text = housing.housing.dateEntry

        if (housing.housing.dateSale != null)
        { this.mView.detail_fragment_date_sale_txt.text = housing.housing.dateSale }
        else {
            this.mView.detail_fragment_date_sale_txt.visibility = View.INVISIBLE
            this.mView.detail_fragment_sale_txt.visibility = View.INVISIBLE
        }

    }

    private fun showAddress()
    {
        var addressValid = false
        if (housing.address != null)
        {
            this.mView.detail_fragment_address_txt.text = housing.address.toString()
            val location = UtilsKotlin.getGeocoderAddress(housing.address.toString(), requireContext()) // TODO : Plante en mode avion : Caused by: java.io.IOException: grpc failed

            if (location != null && location != ERROR_GEOCODER_ADDRESS)
            {
                addressValid = true
                val url = buildUrlStaticMap(location)

                if (Utils.isInternetAvailableGood(context))
                {
                    Glide.with(mView)
                            .load(url)
                            .apply(RequestOptions.centerCropTransform())
                            .into(mView.detail_fragment_address_map_image)
                }
            }
        }

        if (!addressValid)
        {
            this.mView.detail_fragment_address_txt.visibility = View.GONE
            this.mView.detail_fragment_address_map_image.visibility = View.GONE
            this.mView.detail_fragment_separation_2_address_desc.visibility = View.GONE
        }

    }

    private fun showDescription()
    {
        if (housing.housing.description != null)
        {
            this.mView.detail_fragment_description_txt.text = housing.housing.description
        }
        else
        {
            this.mView.detail_fragment_description_title_txt.visibility = View.GONE
            this.mView.detail_fragment_description_txt.visibility = View.GONE
            this.mView.detail_fragment_separation_3_desc_poi.visibility = View.GONE
        }
    }

    private fun showPoi()
    {
        if (!housing.poiList.isNullOrEmpty())
        {
            val poiList = housing.poiList!!.toList()
            val adapter = ListPoiAdapter(poiList)
            this.mView.detail_fragment_rcv_poi.adapter = adapter
            this.mView.detail_fragment_rcv_poi.layoutManager = LinearLayoutManager(context)
        }
        else
        {
            this.mView.detail_fragment_poi_title_txt.visibility = View.GONE
            this.mView.detail_fragment_rcv_poi.visibility = View.GONE
            this.mView.detail_fragment_separation_4_poi_estate.visibility = View.GONE
        }
    }

    private fun showEstateAgent()
    {

        this.mView.detail_fragment_estate_agent_title_txt.visibility = View.VISIBLE
        this.mView.detail_fragment_rcv_estate_agent.visibility = View.VISIBLE
        if (!housing.estateAgentList.isNullOrEmpty())
        {
            val estateList = housing.estateAgentList!!.toList()
            val adapter = ListEstateAgentAdapter(estateList, null)
            this.mView.detail_fragment_rcv_estate_agent.adapter = adapter
            this.mView.detail_fragment_rcv_estate_agent.layoutManager = LinearLayoutManager(context)
        }
        else
        {
            this.mView.detail_fragment_estate_agent_title_txt.visibility = View.GONE
            this.mView.detail_fragment_rcv_estate_agent.visibility = View.GONE
        }
    }

    private fun showPhoto()
    {
        if (!housing.photoList.isNullOrEmpty())
        {
            this.mView.detail_fragment_no_photo.visibility = View.GONE
            val photoList = housing.photoList!!.toList()
            val adapter = ListPhotoDetailAdapter(photoList)
            this.mView.detail_fragment_rcv_photo.adapter = adapter
            this.mView.detail_fragment_rcv_photo.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        else
        {
            this.mView.detail_fragment_rcv_photo.visibility = View.GONE
        }
    }

    private fun buildUrlStaticMap(location : String) : String
    {
        val key = getString(R.string.google_api_key)

        return "https://maps.googleapis.com/maps/api/staticmap?&center=$location&zoom=$ZOOM_STATIC_MAP&size=$SIZE_STATIC_MAP&markers=color:red|$location&key=$key"

    }

}
