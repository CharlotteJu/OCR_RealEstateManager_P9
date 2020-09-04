package com.openclassrooms.realestatemanager.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.HousingEstateAgent
import com.openclassrooms.realestatemanager.views.fragments.AddEstateAgentFragment
import kotlinx.android.synthetic.main.item_estate_agent.view.*

class ListEstateAgentAdapter(private var listEstateAgent : List<HousingEstateAgent>, private val onItemClickEdit: OnItemClickEdit?) : RecyclerView.Adapter<ListEstateAgentAdapter.ListEstateAgentViewModel>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListEstateAgentViewModel
    {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_estate_agent, parent, false)
        return ListEstateAgentViewModel(view, this.onItemClickEdit)
    }

    override fun onBindViewHolder(holder: ListEstateAgentViewModel, position: Int)
    {
        val estateAgent = this.listEstateAgent[position]
        holder.configureDesign(estateAgent)
    }

    override fun getItemCount(): Int = this.listEstateAgent.size

    fun updateList(listEstateAgent: List<HousingEstateAgent>)
    {
        this.listEstateAgent = listEstateAgent
        this.notifyDataSetChanged()
    }


    class ListEstateAgentViewModel(itemView: View, private val onItemClickEdit: OnItemClickEdit?) : RecyclerView.ViewHolder(itemView)
    {
        fun configureDesign(estateAgent : HousingEstateAgent)
        {
            estateAgent.estateAgentName.let { itemView.item_estate_agent_name.text = it }


            if (onItemClickEdit != null)
            {
                itemView.item_estate_agent_delete_button.setOnClickListener { onItemClickEdit.onClickDeleteEstateAgent(adapterPosition) }
            }
            else itemView.item_estate_agent_delete_button.visibility = View.GONE

            if (onItemClickEdit !is AddEstateAgentFragment) itemView.item_estate_agent_edit_button.visibility = View.GONE
            else itemView.item_estate_agent_edit_button.visibility = View.VISIBLE
        }


    }
}