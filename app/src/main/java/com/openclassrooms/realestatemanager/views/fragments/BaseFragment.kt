package com.openclassrooms.realestatemanager.views.fragments

import android.content.Context
import androidx.fragment.app.Fragment
import com.openclassrooms.realestatemanager.utils.CURRENCY_SHARED_PREFERENCES
import com.openclassrooms.realestatemanager.utils.CURRENCY_TAG
import com.openclassrooms.realestatemanager.utils.DOLLAR

abstract class BaseFragment : Fragment()
{
    protected fun getCurrencyFromSharedPreferences() : String
    {
        val sharedPreferences = requireContext().getSharedPreferences(CURRENCY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        return sharedPreferences.getString(CURRENCY_TAG, DOLLAR).toString()
    }
}