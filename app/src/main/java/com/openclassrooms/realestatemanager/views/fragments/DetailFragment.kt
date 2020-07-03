package com.openclassrooms.realestatemanager.views.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController


import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.viewModels.DetailViewModel
import kotlinx.android.synthetic.main.fragment_detail.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class DetailFragment(private val reference : String) : Fragment() {

    /*init {
        findNavController(). //TODO : Voir pour récupérer des arguments
    }*/

   //private val args : ConfirmationF

    private val m_ViewModel : DetailViewModel  by viewModel()

    private lateinit var type : String



    fun newInstance(ref : String) : DetailFragment = DetailFragment(ref)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        var v : View = inflater.inflate(R.layout.fragment_detail, container, false)
        return v
    }

    private fun getDataFromLiveData()
    {
        m_ViewModel.getHousing(reference).observe(this, Observer {
            //type = it.type
            Log.d("DEBUG_APP", "price = $type")
        })
    }

}
