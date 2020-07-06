package com.openclassrooms.realestatemanager.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.HousingEstateAgent
import kotlinx.android.synthetic.main.item_estate_agent.view.*

class ListEstateAgentAdapter(private var listEstateAgent : List<HousingEstateAgent>) : RecyclerView.Adapter<ListEstateAgentAdapter.ListEstateAgentViewModel>()
{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListEstateAgentViewModel
    {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_estate_agent, parent, false)
        return ListEstateAgentViewModel(view)
    }

    override fun onBindViewHolder(holder: ListEstateAgentViewModel, position: Int)
    {
        val estateAgent = this.listEstateAgent[position]
        holder.configureDesign(estateAgent)
    }

    override fun getItemCount(): Int = this.listEstateAgent.size


    class ListEstateAgentViewModel(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun configureDesign(estateAgent : HousingEstateAgent)
        {
            estateAgent.estateAgentName.let { itemView.item_estate_agent_name.text = it }
            estateAgent.function.let { itemView.item_estate_agent_function.text = it }
        }
    }
}