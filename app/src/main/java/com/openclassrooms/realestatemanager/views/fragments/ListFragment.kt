package com.openclassrooms.realestatemanager.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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
import com.openclassrooms.realestatemanager.views.adapters.ListHousingAdapter
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_list.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListFragment : Fragment() {

    private lateinit var m_Adapter : ListHousingAdapter
    //private val m_ViewModel : DetailViewModel by viewModel()
    private lateinit var m_ViewModel : DetailViewModel
    private var m_listHousing : MutableList<CompleteHousing> = arrayListOf()
    private lateinit var recyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        val fabMap : FloatingActionButton = view.list_fragment_map_fab
        val fabDetail : FloatingActionButton = view.list_fragment_detail_fab

        this.testVM()

        this.recyclerView = view.list_fragment_rcv

        this.configRecyclerView()

        m_ViewModel.getGlobalHousingList().observe(this.viewLifecycleOwner, Observer { this.m_Adapter.updateList(it)})


        fabMap.setOnClickListener {
            findNavController().navigate(R.id.mapFragment)
        }

        fabDetail.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToDetailFragment("0")
            findNavController().navigate(action)
        }

        return view
    }



    private fun configRecyclerView()
    {
        this.m_Adapter = ListHousingAdapter(m_listHousing)
        this.recyclerView.adapter = m_Adapter
        this.recyclerView.layoutManager = LinearLayoutManager(context)
    }


    /*private val viewModelFactory : ViewModelFactory by inject() //TODO-Q : Plus de Factory du coup ?
    private val test = viewModelFactory.create(DetailViewModel::class.java)*/

   private fun testVM()
    {

        var vmf = context?.let { Injection.configViewModelFactory(it) }
        //viewModel = ViewModelProvider(this, vmf).get(DetailViewModel::class.java)
        this.m_ViewModel = ViewModelProviders.of(this, vmf).get(DetailViewModel::class.java)
    }


}


