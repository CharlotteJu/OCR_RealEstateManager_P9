package com.openclassrooms.realestatemanager.views.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.EstateAgent
import com.openclassrooms.realestatemanager.utils.STRING_EMPTY
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.viewModels.AddEstateAgentViewModel
import com.openclassrooms.realestatemanager.views.adapters.ListEstateAgentAddAdapter
import com.openclassrooms.realestatemanager.views.adapters.OnItemClickEdit
import kotlinx.android.synthetic.main.fragment_add_estate_agent.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.collections.ArrayList


class AddEstateAgentFragment : BaseFragment(), OnItemClickEdit, ListEstateAgentAddAdapter.OnClickEditEstateAgent {

    private lateinit var mView: View
    private lateinit var mAdapter: ListEstateAgentAddAdapter
    private val mViewModel : AddEstateAgentViewModel by viewModel()

    private var lastName : String? = null
    private var firstName : String? = null
    private var email : String? = null
    private var phoneNumber : String? = null
    private var listEstateAgent : MutableList<EstateAgent> = ArrayList()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        this.mView = inflater.inflate(R.layout.fragment_add_estate_agent, container, false)
        this.getEstateAgentList()
        this.getInfo()
        this.configureRecyclerView()

        this.mView.add_estate_agent_add_fab.setOnClickListener {
            this.addEstateAgent()
        }

        return this.mView
    }

    private fun getEstateAgentList()
    {
        this.mViewModel.getAllEstateAgent().observe(this.viewLifecycleOwner, androidx.lifecycle.Observer {
            listEstateAgent = it as MutableList<EstateAgent>
            mAdapter.updateList(listEstateAgent)
        })
    }

    /**
     * Link with the ViewModel to add an EstateAgent in RoomDatabase
     * Check if an EstateAgent does'nt have the same lastName because it's the PK of model
     */
    private fun addEstateAgent()
    {
        if (this.isLastNameNotNull())
        {
            if ((!listEstateAgent.isNullOrEmpty() && !listEstateAgent.contains(EstateAgent(lastName!!))) || listEstateAgent.isNullOrEmpty())
            {
                val estateAgent = EstateAgent(lastName!!, firstName, email, phoneNumber, Utils.getTodayDateGood())
                this.mViewModel.createGlobalEstateAgent(estateAgent)
                this.resetViews()
            }
            else Toast.makeText(context, getString(R.string.toast_estate_agent_same_name), Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Check if the lastName is not null because it's the PK of the model
     */
    private fun isLastNameNotNull() : Boolean
    {
        return if (lastName != null && lastName != STRING_EMPTY) true
        else
        {
            Toast.makeText(context, getString(R.string.toast_estate_agent_no_name), Toast.LENGTH_LONG).show()
            false
        }
    }


    private fun getInfo()
    {
        this.mView.add_estate_agent_last_name_edit_txt.doAfterTextChanged { this.lastName = it.toString() }
        this.mView.add_estate_agent_first_name_edit_txt.doAfterTextChanged { this.firstName = it.toString() }
        this.mView.add_estate_agent_email_edit_txt.doAfterTextChanged { this.email = it.toString() }
        this.mView.add_estate_agent_phone_number_edit_txt.doAfterTextChanged { this.phoneNumber = it.toString() }
    }


    /**
     * Reset Views when we Add or Update an EstateAgent
     */
    private fun resetViews()
    {
        this.closeKeyboard(requireContext(), mView)
        this.mView.add_estate_agent_last_name_edit_txt.setText(STRING_EMPTY)
        this.mView.add_estate_agent_first_name_edit_txt.setText(STRING_EMPTY)
        this.mView.add_estate_agent_email_edit_txt.setText(STRING_EMPTY)
        this.mView.add_estate_agent_phone_number_edit_txt.setText(STRING_EMPTY)
    }

    private fun configureRecyclerView()
    {
        this.mAdapter = ListEstateAgentAddAdapter(this.listEstateAgent, this, this)
        this.mView.add_estate_agent_rcv.adapter = mAdapter
        this.mView.add_estate_agent_rcv.layoutManager = LinearLayoutManager(context)
    }


    override fun onClickDeleteEstateAgent(position: Int) {

        val builder = AlertDialog.Builder(context)
        builder.setMessage(resources.getString(R.string.sure_delete))
                .setPositiveButton(getString(R.string.yes), DialogInterface.OnClickListener { _, _ ->
                    if (listEstateAgent.size <= 1) listEstateAgent.clear()
                    else
                    {
                        val estateAgentToDelete = this.listEstateAgent[position]
                        this.listEstateAgent.remove(estateAgentToDelete)
                    }
                    this.mAdapter.updateList(listEstateAgent)
                })
                .setNegativeButton(getString(R.string.no), DialogInterface.OnClickListener { dialog, _ -> dialog.cancel() })

        val alertDialog = builder.create()
        alertDialog.show()
    }


    override fun onClickEditEstateAgent(position: Int) {
        val estateAgentToEdit = this.listEstateAgent[position]

        this.mView.add_estate_agent_last_name_edit_txt.setText(estateAgentToEdit.lastName)
        this.mView.add_estate_agent_first_name_edit_txt.setText(estateAgentToEdit.firstName)
        this.mView.add_estate_agent_email_edit_txt.setText(estateAgentToEdit.email)
        this.mView.add_estate_agent_phone_number_edit_txt.setText(estateAgentToEdit.phoneNumber)

        this.mView.add_estate_agent_add_fab.setImageResource(R.drawable.ic_baseline_save_24)
        this.mView.add_estate_agent_add_fab.setOnClickListener {
            if (this.isLastNameNotNull())
            {
                //To check if this PK is not assigned for another Estate Agent
                val listTemp = listEstateAgent.toMutableList()
                listTemp.removeAt(position)

                if (!listTemp.contains(EstateAgent(lastName!!)))
                {
                    val estateAgent = EstateAgent(lastName!!, firstName, email, phoneNumber, Utils.getTodayDateGood())
                    this.mViewModel.updateGlobalEstateAgent(estateAgent)
                    this.resetViews()
                    this.mView.add_estate_agent_add_fab.setImageResource(R.drawable.ic_baseline_add_48)
                }
                else Toast.makeText(context, getString(R.string.toast_estate_agent_same_name), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onClickEditPhoto(position: Int) {/*No useful here*/ }

    override fun onClickDeletePhoto(position: Int) {/*No useful here*/ }


}