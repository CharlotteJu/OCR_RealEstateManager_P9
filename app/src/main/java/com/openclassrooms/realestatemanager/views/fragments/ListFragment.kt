package com.openclassrooms.realestatemanager.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.enums.EnumPoi
import com.openclassrooms.realestatemanager.enums.EnumState
import com.openclassrooms.realestatemanager.models.CompleteHousing
import com.openclassrooms.realestatemanager.models.Housing
import com.openclassrooms.realestatemanager.models.Poi
import com.openclassrooms.realestatemanager.viewModels.DetailViewModel
import com.openclassrooms.realestatemanager.viewModels.Injection
import com.openclassrooms.realestatemanager.viewModels.ViewModelFactory
import com.openclassrooms.realestatemanager.views.adapters.ListHousingAdapter
import com.openclassrooms.realestatemanager.views.adapters.onItemClickListener
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_list.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListFragment : Fragment(), onItemClickListener {

    private lateinit var m_View : View
    private lateinit var m_Adapter : ListHousingAdapter
    private val m_ViewModel : DetailViewModel by viewModel()
    private var m_listHousing : MutableList<CompleteHousing> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        this.m_View = inflater.inflate(R.layout.fragment_list, container, false)
        this.m_ViewModel.getGlobalHousingList().observe(this.viewLifecycleOwner, Observer {
            this.m_Adapter.updateList(it)
            m_listHousing = it as MutableList<CompleteHousing>
        })
        this.configRecyclerView()
        this.m_View.list_fragment_map_fab.setOnClickListener {
            findNavController().navigate(R.id.mapFragment)
        }

        this.m_View.list_fragment_detail_fab.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToDetailFragment(it.toString())
            findNavController().navigate(action)
        }

        return m_View
    }

    private fun configRecyclerView()
    {
        this.m_Adapter = ListHousingAdapter(m_listHousing, this)
        this.m_View.list_fragment_rcv.adapter = m_Adapter
        this.m_View.list_fragment_rcv.layoutManager = LinearLayoutManager(context)
    }

    override fun onItemClick(position : Int)
    {
        /*//val action = ListFragmentDirections.actionListFragmentToDetailFragment(this.view?.tag.toString())
        //findNavController().navigate(action)*/
        //val bundle = DetailFragmentArgs(this.m_listHousing[position].housing.ref).toBundle()
        //val bundle = bundleOf("reference" to view.tag)

        val bundle : Bundle = Bundle()
        bundle.putString("reference", this.m_listHousing[position].housing.ref)
        findNavController().navigate(R.id.detailFragment, bundle)
    }


}


