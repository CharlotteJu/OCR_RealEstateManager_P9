package com.openclassrooms.realestatemanager.views.fragments

import android.content.Context
import androidx.fragment.app.Fragment
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.utils.*

abstract class BaseFragment : Fragment()
{
    protected fun getCurrencyFromSharedPreferences() : String
    {
        val sharedPreferences = requireContext().getSharedPreferences(CURRENCY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        return sharedPreferences.getString(CURRENCY_TAG, DOLLAR).toString()
    }

    protected fun getIsTabletFromSharedPreferences() : Boolean
    {
        return resources.getBoolean(R.bool.isTablet)
    }

}