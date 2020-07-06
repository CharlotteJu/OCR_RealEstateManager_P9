package com.openclassrooms.realestatemanager.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.fragment_add_housing.view.*


class AddHousingFragment : Fragment() {

    private lateinit var typeSpinner : Spinner
    private lateinit var priceSpinner: Spinner
    private lateinit var roomsSpinner: Spinner
    private lateinit var bedRoomsSpinner: Spinner
    private lateinit var bathRoomsSpinner: Spinner
    private lateinit var stateSpinner: Spinner
    private lateinit var estateAgentNameSpinner : Spinner
    private lateinit var estateAgentFunctionSpinner: Spinner


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_add_housing, container, false)

        this.configureSpinners(view)
        return view
    }

    private fun configureSpinners(view: View)
    {
        typeSpinner = view.add_housing_fragment_type_spinner
        context?.let { ArrayAdapter.createFromResource(it,R.array.type_housing_spinner, android.R.layout.simple_spinner_item).also {
            adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            typeSpinner.adapter = adapter
        } }
    }




}