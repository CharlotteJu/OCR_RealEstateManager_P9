package com.openclassrooms.realestatemanager.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.CompleteHousing
import com.openclassrooms.realestatemanager.models.EstateAgent
import com.openclassrooms.realestatemanager.utils.*
import com.openclassrooms.realestatemanager.viewModels.ListHousingViewModel
import com.openclassrooms.realestatemanager.views.activities.MainActivity
import com.openclassrooms.realestatemanager.views.adapters.ListHousingAdapter
import com.openclassrooms.realestatemanager.views.adapters.OnClickDelete
import com.openclassrooms.realestatemanager.views.adapters.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.android.synthetic.main.progress_bar.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListFragment : BaseFragment(), OnItemClickListener, OnClickDelete {

    private lateinit var mView : View
    private lateinit var mAdapter : ListHousingAdapter
    private val mViewModel : ListHousingViewModel by viewModel()
    private var mListHousing : MutableList<CompleteHousing> = arrayListOf()
    private var mListEstateAgent : MutableList<EstateAgent> = arrayListOf()
    private lateinit var currency: String
    private var isInternetAvailable : Boolean = false

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
        this.isInternetAvailable = Utils.isInternetAvailableGood(context)
        this.mViewModel.getAllCompleteHousing().observe(this.viewLifecycleOwner, Observer {
            this.mAdapter.updateList(it)
            mListHousing = it as MutableList<CompleteHousing>
            if (isInternetAvailable) this.syncCompleteHousingWithFirestore()
        })

        this.mViewModel.getAllEstateAgent().observe(this.viewLifecycleOwner, Observer {
            mListEstateAgent = it as MutableList<EstateAgent>
            if (isInternetAvailable)
                this.syncEstateAgentWithFirestore()
        })

        this.configRecyclerView()
        this.mView.list_fragment_map_fab.setOnClickListener {
            if (Utils.isInternetAvailableGood(context)) findNavController().navigate(R.id.mapFragment)
            else Toast.makeText(context, getString(R.string.toast_internet_no_available), Toast.LENGTH_LONG).show()
        }

        return mView
    }

    private fun configRecyclerView()
    {
        this.currency = getCurrencyFromSharedPreferences()
        this.mAdapter = ListHousingAdapter(mListHousing, this, this, this.currency, this.isInternetAvailable, requireContext())
        this.mView.list_fragment_rcv.adapter = mAdapter
        this.mView.list_fragment_rcv.layoutManager = LinearLayoutManager(context)
    }

    private fun syncCompleteHousingWithFirestore()
    {
        context?.let {
            this.mViewModel.syncCompleteHousingWithFirebase(mListHousing, it)
        }
    }

    private fun syncEstateAgentWithFirestore()
    {
        context?.let {
            this.mViewModel.syncEstateAgentWithFirebase(mListEstateAgent)
        }
    }

    override fun onItemClick(position : Int)
    {
       if (!this.getIsTabletFromSharedPreferences())
       {
           val bundle  = Bundle()
           bundle.putString(BUNDLE_REFERENCE, this.mListHousing[position].housing.ref)
           findNavController().navigate(R.id.detailFragment, bundle)
       }
        else
       {
           val detailFragment = (activity as MainActivity).getDetailFragment()
           detailFragment?.updateRef(this.mListHousing[position].housing.ref, requireContext())
       }
    }

    override fun onClickDeleteHousing(position: Int)
    {
        val completeHousing = this.mListHousing[position]
        this.mListHousing.removeAt(position)
        this.mViewModel.deleteGlobal(completeHousing)
        this.mAdapter.updateList(mListHousing)

    }

}


