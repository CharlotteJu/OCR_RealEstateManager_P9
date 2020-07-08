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
import com.openclassrooms.realestatemanager.viewModels.ViewModelFactory
import com.openclassrooms.realestatemanager.views.adapters.ListHousingAdapter
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_list.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListFragment : Fragment() {

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
        this.m_ViewModel.getGlobalHousingList().observe(this.viewLifecycleOwner, Observer { this.m_Adapter.updateList(it)})
        this.configRecyclerView()
        this.m_View.list_fragment_map_fab.setOnClickListener {
            findNavController().navigate(R.id.mapFragment)
        }

        this.m_View.list_fragment_detail_fab.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToDetailFragment("0")
            findNavController().navigate(action)
        }

        return m_View
    }

    private fun configRecyclerView()
    {
        this.m_Adapter = ListHousingAdapter(m_listHousing)
        this.m_View.list_fragment_rcv.adapter = m_Adapter
        this.m_View.list_fragment_rcv.layoutManager = LinearLayoutManager(context)
    }


}


