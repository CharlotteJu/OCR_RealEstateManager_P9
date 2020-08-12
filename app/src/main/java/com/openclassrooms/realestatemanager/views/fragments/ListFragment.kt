package com.openclassrooms.realestatemanager.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.CompleteHousing
import com.openclassrooms.realestatemanager.utils.BUNDLE_REFERENCE
import com.openclassrooms.realestatemanager.viewModels.DetailViewModel
import com.openclassrooms.realestatemanager.views.adapters.ListHousingAdapter
import com.openclassrooms.realestatemanager.views.adapters.OnClickDelete
import com.openclassrooms.realestatemanager.views.adapters.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_list.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListFragment : BaseFragment(), OnItemClickListener, OnClickDelete {

    private lateinit var mView : View
    private lateinit var mAdapter : ListHousingAdapter
    private val mViewModel : DetailViewModel by viewModel()
    private var mListHousing : MutableList<CompleteHousing> = arrayListOf()
    private lateinit var currency: String


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        this.configRecyclerView()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        this.mView = inflater.inflate(R.layout.fragment_list, container, false)
        this.mViewModel.getAllCompleteHousing().observe(this.viewLifecycleOwner, Observer {
            this.mAdapter.updateList(it)
            mListHousing = it as MutableList<CompleteHousing>
        })
        this.configRecyclerView()
        this.mView.list_fragment_map_fab.setOnClickListener {
            findNavController().navigate(R.id.mapFragment)
        }

        return mView
    }

    private fun configRecyclerView()
    {
        this.currency = getCurrencyFromSharedPreferences()
        this.mAdapter = ListHousingAdapter(mListHousing, this, this, this.currency)
        this.mView.list_fragment_rcv.adapter = mAdapter
        this.mView.list_fragment_rcv.layoutManager = LinearLayoutManager(context)
    }

    override fun onItemClick(position : Int)
    {
        //val bundle = DetailFragmentArgs(this.m_listHousing[position].housing.ref).toBundle()

        val bundle  = Bundle()
        bundle.putString(BUNDLE_REFERENCE, this.mListHousing[position].housing.ref)
        findNavController().navigate(R.id.detailFragment, bundle)
    }

    override fun onClickDelete(position: Int)
    {
        val completeHousing = this.mListHousing[position]
        this.mViewModel.deleteGlobal(completeHousing)

        /*this.mViewModel.deleteHousing(completeHousing.housing)

        if (completeHousing.address != null) this.mViewModel.deleteAddress(completeHousing.address!!)

        if (completeHousing.estateAgentList != null || completeHousing.estateAgentList!!.isNotEmpty())
        {
            for (i in completeHousing.estateAgentList!!)
            {
                this.mViewModel.deleteHousingEstateAgent(i)
            }
        }

        if (completeHousing.photoList != null || completeHousing.photoList!!.isNotEmpty())
        {
            for (i in completeHousing.photoList!!)
            {
                this.mViewModel.deletePhoto(i)
            }
        }

        if (completeHousing.poiList != null || completeHousing.poiList!!.isNotEmpty())
        {
            for (i in completeHousing.poiList!!)
            {
                this.mViewModel.deleteHousingPoi(i)
            }
        }*/
    }


}


