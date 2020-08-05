package com.openclassrooms.realestatemanager.views.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.android.synthetic.main.fragment_map.view.*



/**
 * A simple [Fragment] subclass.
 * Use the [MapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MapFragment : Fragment(), OnMapReadyCallback {

    //private lateinit var mFusedLocationClient : FusedLocationProviderClient
    private lateinit var mMap : GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_map, container, false)
        val mapFragment = FragmentManager.findFragment<SupportMapFragment>(view)
        mapFragment.getMapAsync (this)

        return view
    }

    override fun onMapReady(p0: GoogleMap?) {

        p0?.apply {
            val sydney = LatLng(-33.852, 151.211)
            addMarker(
                    MarkerOptions()
                            .position(sydney)
                            .title("Marker in Sydney")
            )
        }
    }


    /*private fun fetchLocation()
    {
        if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION != PackageManager.PERMISSION_GRANTED))
        {
            arrayOf (android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
        else
        {

        }
    }*/


}