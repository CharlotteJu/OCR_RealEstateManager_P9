package com.openclassrooms.realestatemanager.views.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.android.synthetic.main.fragment_map.view.*

/**
 * A simple [Fragment] subclass.
 * Use the [MapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MapFragment : Fragment() {

    //private lateinit var mFusedLocationClient : FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_map, container, false)

        val fabList : FloatingActionButton = view.map_fragment_list_fab
        val fabDetail : FloatingActionButton = view.map_fragment_detail_fab

        fabList.setOnClickListener {
            findNavController().navigate(R.id.listFragment)
        }

        fabDetail.setOnClickListener {
            findNavController().navigate(R.id.detailFragment)
        }

        return view
    }

    /*private fun fetchLocation()
    {
        if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION != PackageManager.PERMISSION_GRANTED))
        {
            ActivityCompat.requestPermissions(activity, Manifest)
        }
        else
        {

        }
    }*/


}