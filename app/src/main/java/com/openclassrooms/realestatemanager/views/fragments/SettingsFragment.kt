package com.openclassrooms.realestatemanager.views.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.utils.*
import kotlinx.android.synthetic.main.fragment_settings.view.*


class SettingsFragment : Fragment() {

    private lateinit var currency : String
    private var isNotification : Boolean = true
    private lateinit var mView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_settings, container, false)
        this.getSharedPreferences()
        this.getRadioButton()
        this.getNotificationSwitchButton()
        return mView
    }

    private fun getRadioButton()
    {
        this.mView.settings_fragment_radio_group.setOnCheckedChangeListener {
            _, checkedId ->
            when (checkedId) {
            R.id.settings_fragment_dollar_radio_button -> currency = DOLLAR
            R.id.settings_fragment_euro_radio_button -> currency = EURO
        }
            this.updateSharedPreferencesCurrency(currency) }

    }

    private fun getNotificationSwitchButton()
    {
        this.mView.settings_fragment_notifications_switch.setOnCheckedChangeListener { _, isChecked ->
            isNotification = isChecked
            this.updateSharedPreferencesNotification(isNotification)
        }
    }

    private fun getSharedPreferences() {
        val sharedPreferencesCurrency = requireContext().
        getSharedPreferences(CURRENCY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        currency = sharedPreferencesCurrency.getString(CURRENCY_TAG, DOLLAR).toString()
        if (currency== DOLLAR) this.mView.settings_fragment_dollar_radio_button.isChecked = true
        else this.mView.settings_fragment_euro_radio_button.isChecked = true
        val sharedPreferencesNotification = requireContext().
        getSharedPreferences(NOTIFICATION_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        isNotification = sharedPreferencesNotification.getBoolean(NOTIFICATION_TAG, true)
        this.mView.settings_fragment_notifications_switch.isChecked = isNotification
    }

    private fun updateSharedPreferencesCurrency(currency : String)
    {
        val sharedPreferences = requireContext().getSharedPreferences(CURRENCY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(CURRENCY_TAG, currency).apply()
    }

    private fun updateSharedPreferencesNotification(isNotification : Boolean)
    {
        val sharedPreferencesNotification = requireContext().getSharedPreferences(NOTIFICATION_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val editor = sharedPreferencesNotification.edit()
        editor.putBoolean(NOTIFICATION_TAG, isNotification).apply()
    }




}