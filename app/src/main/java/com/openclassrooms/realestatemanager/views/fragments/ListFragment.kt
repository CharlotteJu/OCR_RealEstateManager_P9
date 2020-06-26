package com.openclassrooms.realestatemanager.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.fragment_list.view.*

class ListFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        val fabMap : FloatingActionButton = view.list_fragment_map_fab
        val fabDetail : FloatingActionButton = view.list_fragment_detail_fab

        fabMap.setOnClickListener {
            findNavController().navigate(R.id.mapFragment)
        }

        fabDetail.setOnClickListener {
            findNavController().navigate(R.id.detailFragment)
        }

        return view
    }


}