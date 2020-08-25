package com.openclassrooms.realestatemanager.views.adapters


import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.models.EstateAgent
import android.view.View
import android.view.ViewGroup
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.HousingEstateAgent
import com.openclassrooms.realestatemanager.views.fragments.AddEstateAgentFragment
import kotlinx.android.synthetic.main.item_estate_agent.view.*

class ListEstateAgentAddAdapter(private var listEstateAgent : List<EstateAgent>, private val onItemClickEdit: OnItemClickEdit, private val onClickEditEstateAgent: OnClickEditEstateAgent)
    : RecyclerView.Adapter<ListEstateAgentAddAdapter.ListEstateAgentAddViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListEstateAgentAddViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_estate_agent, parent, false)
        return ListEstateAgentAddViewHolder(view, this.onItemClickEdit, this.onClickEditEstateAgent)
    }

    override fun onBindViewHolder(holder: ListEstateAgentAddViewHolder, position: Int) {
        val estateAgent = this.listEstateAgent[position]
        holder.configureDesign(estateAgent)
    }

    override fun getItemCount(): Int = this.listEstateAgent.size

    fun updateList(listEstateAgent: List<EstateAgent>)
    {
        this.listEstateAgent = listEstateAgent
        notifyDataSetChanged()
    }

    class ListEstateAgentAddViewHolder(itemView : View, private val onItemClickEdit: OnItemClickEdit, private val onClickEditEstateAgent: OnClickEditEstateAgent) : RecyclerView.ViewHolder(itemView)
    {
        fun configureDesign(estateAgent : EstateAgent)
        {
            estateAgent.lastName.let { itemView.item_estate_agent_name.text = it }

            itemView.item_estate_agent_delete_button.setOnClickListener { onItemClickEdit.onClickDeleteEstateAgent(adapterPosition) }

            itemView.item_estate_agent_edit_button.setOnClickListener { onClickEditEstateAgent.onClickEditEstateAgent(adapterPosition)}

        }
    }


    interface OnClickEditEstateAgent
    {
        fun onClickEditEstateAgent(position : Int)
    }
}