package com.openclassrooms.realestatemanager.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.HousingPoi
import kotlinx.android.synthetic.main.item_poi.view.*

class ListPoiAdapter (private val poiList : List<HousingPoi>) : RecyclerView.Adapter<ListPoiAdapter.ListPoiViewModel>()
{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPoiViewModel
    {
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.item_poi, parent, false)
        return ListPoiViewModel(view, context)
    }

    override fun onBindViewHolder(holder: ListPoiViewModel, position: Int)
    {
        val poi = this.poiList[position]
        holder.configureDesign(poi)
    }

    override fun getItemCount(): Int = this.poiList.size


    class ListPoiViewModel (itemView : View, val context : Context) : RecyclerView.ViewHolder(itemView)
    {
       //TODO-Q : Pourquoi pas val sur itemView mais sur Context ?

        fun configureDesign(poi : HousingPoi)
        {
            when(poi.poiType){
                context.getString(R.string.poi_type_restaurant) -> {
                    itemView.item_poi_icone.setImageResource(R.drawable.ic_baseline_restaurant_24)
                    itemView.item_poi_description.text = context.getString(R.string.restaurants)}
                context.getString(R.string.poi_type_school)-> {
                    itemView.item_poi_icone.setImageResource(R.drawable.ic_baseline_school_24)
                    itemView.item_poi_description.text = context.getString(R.string.schools)}
                context.getString(R.string.poi_type_store) -> {
                    itemView.item_poi_icone.setImageResource(R.drawable.ic_baseline_store_24)
                    itemView.item_poi_description.text = context.getString(R.string.stores)}
                context.getString(R.string.poi_type_subway) -> {
                    itemView.item_poi_icone.setImageResource(R.drawable.ic_baseline_subway_24)
                    itemView.item_poi_description.text = context.getString(R.string.subways)}
                context.getString(R.string.poi_type_park) -> {
                    itemView.item_poi_icone.setImageResource(R.drawable.ic_baseline_park_24)
                    itemView.item_poi_description.text = context.getString(R.string.parks)}
            }
            poi.numberOfPoi.let { itemView.item_poi_number.text = it.toString() }
        }
    }
}