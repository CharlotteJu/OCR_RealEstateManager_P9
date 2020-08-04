package com.openclassrooms.realestatemanager.views.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.utils.CURRENCY_SHARED_PREFERENCES
import com.openclassrooms.realestatemanager.utils.CURRENCY_TAG
import com.openclassrooms.realestatemanager.utils.DOLLAR
import com.openclassrooms.realestatemanager.utils.EURO
import com.openclassrooms.realestatemanager.views.activities.MainActivity
import kotlinx.android.synthetic.main.fragment_settings.view.*


/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {

    private lateinit var currency : String
    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_settings, container, false)
        this.getSharedPreferences()
        this.getRadioButton()
        return mView
    }

    private fun getRadioButton()
    {
        this.mView.settings_fragment_radio_group.setOnCheckedChangeListener {
            group, checkedId ->
            when (checkedId) {
            R.id.settings_fragment_dollar_radio_button -> currency = DOLLAR
            R.id.settings_fragment_euro_radio_button -> currency = EURO
        }
            this.updateSharedPreferences(currency) }

    }

    private fun getSharedPreferences()
    {
        val sharedPreferences = requireContext().getSharedPreferences(CURRENCY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        currency = sharedPreferences.getString(CURRENCY_TAG, DOLLAR).toString()
        if (currency== DOLLAR) this.mView.settings_fragment_dollar_radio_button.isChecked = true
        else this.mView.settings_fragment_euro_radio_button.isChecked = true

    }

    private fun updateSharedPreferences(currency : String)
    {
        val sharedPreferences = requireContext().getSharedPreferences(CURRENCY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val editor = sharedPreferences!!.edit()
        editor.putString(CURRENCY_TAG, currency).apply()
    }




}