package com.openclassrooms.realestatemanager.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.EstateAgent
import com.openclassrooms.realestatemanager.utils.STRING_EMPTY
import com.openclassrooms.realestatemanager.viewModels.AddEstateTypeViewModel
import com.openclassrooms.realestatemanager.views.adapters.ListEstateAgentAddAdapter
import com.openclassrooms.realestatemanager.views.adapters.OnItemClickEdit
import kotlinx.android.synthetic.main.fragment_add_estate_agent.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.collections.ArrayList


class AddEstateAgentFragment : Fragment(), OnItemClickEdit, ListEstateAgentAddAdapter.OnClickEditEstateAgent {

    private lateinit var mView: View
    private lateinit var mAdapter: ListEstateAgentAddAdapter
    private var lastName : String? = null
    private var firstName : String? = null
    private var email : String? = null
    private var phoneNumber : String? = null
    private var listEstateAgent : MutableList<EstateAgent> = ArrayList()

    private val mViewModel : AddEstateTypeViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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

    private fun addEstateAgent()
    {
        if (lastName != null)
        {
            val estateAgent = EstateAgent(lastName!!, firstName, email, phoneNumber)
            this.mViewModel.createGlobalEstateAgent(estateAgent)
        }
    }

    private fun getEstateAgentList()
    {
        this.mViewModel.getAllEstateAgent().observe(this.viewLifecycleOwner, androidx.lifecycle.Observer {
            listEstateAgent = it as MutableList<EstateAgent>
            mAdapter.updateList(listEstateAgent)
        })
    }


    private fun getInfo()
    {
        this.mView.add_estate_agent_last_name_edit_txt.doAfterTextChanged { this.lastName = it.toString() }
        this.mView.add_estate_agent_first_name_edit_txt.doAfterTextChanged { this.firstName = it.toString() }
        this.mView.add_estate_agent_email_edit_txt.doAfterTextChanged { this.email = it.toString() }
        this.mView.add_estate_agent_phone_number_edit_txt.doAfterTextChanged { this.phoneNumber = it.toString() }
    }

    private fun configureRecyclerView()
    {
        this.mAdapter = ListEstateAgentAddAdapter(this.listEstateAgent, this, this)
        this.mView.add_estate_agent_rcv.adapter = mAdapter
        this.mView.add_estate_agent_rcv.layoutManager = LinearLayoutManager(context)
    }

    fun chargeInfoToEdit()
    {

    }


    override fun onClickDeleteEstateAgent(position: Int) {
        if (listEstateAgent.size <= 1)
        {
            listEstateAgent.clear()
        }
        else
        {
            val estateAgentToDelete = this.listEstateAgent[position]
            this.listEstateAgent.remove(estateAgentToDelete)
        }
        this.mAdapter.updateList(listEstateAgent)
    }

    override fun onClickEditPhoto(position: Int) {
        //No useful here
    }

    override fun onClickDeletePhoto(position: Int) {
        //No useful here
    }

    override fun onClickEditEstateAgent(position: Int) {
        val estateAgentToEdit = this.listEstateAgent[position]

        this.mView.add_estate_agent_last_name_edit_txt.setText(estateAgentToEdit.lastName)
        this.mView.add_estate_agent_first_name_edit_txt.setText(estateAgentToEdit.firstName)
        this.mView.add_estate_agent_email_edit_txt.setText(estateAgentToEdit.email)
        this.mView.add_estate_agent_phone_number_edit_txt.setText(estateAgentToEdit.phoneNumber)

        this.mView.add_estate_agent_add_fab.setImageResource(R.drawable.ic_baseline_save_24)
        this.mView.add_estate_agent_add_fab.setOnClickListener {
            if (lastName != null)
            {
                val estateAgent = EstateAgent(lastName!!, firstName, email, phoneNumber)
                this.mViewModel.updateGlobalEstateAgent(estateAgent)
                this.mView.add_estate_agent_last_name_edit_txt.setText(STRING_EMPTY)
                this.mView.add_estate_agent_first_name_edit_txt.setText(STRING_EMPTY)
                this.mView.add_estate_agent_email_edit_txt.setText(STRING_EMPTY)
                this.mView.add_estate_agent_phone_number_edit_txt.setText(STRING_EMPTY)
                this.mView.add_estate_agent_add_fab.setImageResource(R.drawable.ic_baseline_add_48)
            }
        }
    }


}