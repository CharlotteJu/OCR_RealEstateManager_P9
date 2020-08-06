package com.openclassrooms.realestatemanager.views.fragments

import android.Manifest
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.CompleteHousing
import com.openclassrooms.realestatemanager.utils.DOLLAR
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.viewModels.DetailViewModel

import org.koin.androidx.viewmodel.ext.android.viewModel

const val LOCATION_PERMISSION_REQUEST_CODE = 101

/**
 * A simple [Fragment] subclass.
 * Use the [MapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MapFragment : BaseFragment(), OnMapReadyCallback, LocationListener {

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap
    private var mCurrentLocation: Location? = null
    private val mViewModel : DetailViewModel by viewModel()
    private var mListHousing : MutableList<CompleteHousing> = arrayListOf()
    private lateinit var currency : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.mFusedLocationClient = requireActivity().let { LocationServices.getFusedLocationProviderClient(it) }
        this.currency = getCurrencyFromSharedPreferences()



    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_map, container, false)
        val mapFragment = FragmentManager.findFragment<SupportMapFragment>(view)
        this.fetchLocation()
        this.mViewModel.getAllCompleteHousing().observe(this.viewLifecycleOwner, Observer {
            this.mListHousing = it as MutableList<CompleteHousing>
            this.createMarkers()
        })
        mapFragment.getMapAsync(this)

        return view
    }

    override fun onMapReady(p0: GoogleMap?) {

        if (p0 != null) mMap = p0

        if (mCurrentLocation != null)
        {
            mMap.apply {
                val marker = LatLng(mCurrentLocation!!.latitude, mCurrentLocation!!.longitude)
                addMarker(MarkerOptions().position(marker))
                moveCamera(CameraUpdateFactory.newLatLng(marker))
            }
        }
    }

    private fun createMarkers()
    {
        for (housing in mListHousing)
        {
            if (housing.address != null)
            {
                val geocoder : Geocoder = Geocoder(context)
                val listGeocoder  = geocoder.getFromLocationName(housing.address.toString(), 1)
                val lat  = listGeocoder[0].latitude
                val lng = listGeocoder[0].longitude
                val latLng = LatLng(lat, lng)
                val price : String?

                if (currency == DOLLAR)
                {
                    housing.housing.price.let { price = "$it$" }
                }
                else
                {
                    housing.housing.price.let {
                        val euroPrice = Utils.convertDollarToEuroDouble(it)
                        price = "$euroPrice€"
                    }
                }

                val tempMarker : MarkerOptions = MarkerOptions().position(latLng).title("${housing.housing.type} - $price")
                val finalMarker = mMap.addMarker(tempMarker)
                finalMarker.tag = housing.housing.ref
                this.mMap.setOnInfoWindowClickListener {
                    val bundle = Bundle ()
                    bundle.putString(BUNDLE_REFERENCE, it.tag.toString())
                    findNavController().navigate(R.id.detailFragment, bundle)
                }



            }
        }
    }


    private fun fetchLocation() {
        if (context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) } != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)

        } else {
            val task = mFusedLocationClient.lastLocation
            task.addOnSuccessListener {
                if (it != null) {
                    mCurrentLocation = it
                    mMap.apply {
                        val marker = LatLng(mCurrentLocation!!.latitude, mCurrentLocation!!.longitude)
                        addMarker(MarkerOptions().position(marker))
                        moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 15f))
                    }
                } else {

                    val locationManager : LocationManager = activity?.getSystemService(LOCATION_SERVICE) as LocationManager

                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) != null)
                    {
                        //TODO : Pourquoi ça ne peut pas être null ? Si permission pas accordée
                        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                        this.fetchLocation()
                    }
                    else
                    {
                       locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this )
                    }
                }

            }
        }


    }

    override fun onLocationChanged(location: Location?) {
        mCurrentLocation = location // TODO : Comme déclarer un objet LocationListener sinon ?
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    override fun onProviderEnabled(provider: String?) {}

    override fun onProviderDisabled(provider: String?) {}


}