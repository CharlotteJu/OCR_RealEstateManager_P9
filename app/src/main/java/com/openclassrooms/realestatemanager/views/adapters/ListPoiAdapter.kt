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
        fun configureDesign(poi : HousingPoi)
        {
            when(poi.poiType){
                context.getString(R.string.restaurant) -> {
                    itemView.item_poi_icone.setImageResource(R.drawable.ic_baseline_restaurant_24)
                    itemView.item_poi_description.text = context.getString(R.string.restaurant).capitalize()}
                context.getString(R.string.school)-> {
                    itemView.item_poi_icone.setImageResource(R.drawable.ic_baseline_school_24)
                    itemView.item_poi_description.text = context.getString(R.string.school).capitalize()}
                context.getString(R.string.store) -> {
                    itemView.item_poi_icone.setImageResource(R.drawable.ic_baseline_store_24)
                    itemView.item_poi_description.text = context.getString(R.string.store).capitalize()}
                context.getString(R.string.subway) -> {
                    itemView.item_poi_icone.setImageResource(R.drawable.ic_baseline_subway_24)
                    itemView.item_poi_description.text = context.getString(R.string.subway).capitalize()}
                context.getString(R.string.park) -> {
                    itemView.item_poi_icone.setImageResource(R.drawable.ic_baseline_park_24)
                    itemView.item_poi_description.text = context.getString(R.string.park).capitalize()}
                context.getString(R.string.museum) -> {
                    itemView.item_poi_icone.setImageResource(R.drawable.ic_baseline_museum_24)
                    itemView.item_poi_description.text = context.getString(R.string.museum).capitalize()}
                context.getString(R.string.doctor) -> {
                    itemView.item_poi_icone.setImageResource(R.drawable.ic_baseline_doctor_24)
                    itemView.item_poi_description.text = context.getString(R.string.doctor).capitalize()}
                context.getString(R.string.bank) -> {
                    itemView.item_poi_icone.setImageResource(R.drawable.ic_baseline_bank_24)
                    itemView.item_poi_description.text = context.getString(R.string.bank).capitalize()}
                context.getString(R.string.airport) -> {
                    itemView.item_poi_icone.setImageResource(R.drawable.ic_baseline_airport_24)
                    itemView.item_poi_description.text = context.getString(R.string.airport).capitalize()}
                context.getString(R.string.bar) -> {
                    itemView.item_poi_icone.setImageResource(R.drawable.ic_baseline_bar_24)
                    itemView.item_poi_description.text = context.getString(R.string.bar).capitalize()}
                context.getString(R.string.hospital) -> {
                    itemView.item_poi_icone.setImageResource(R.drawable.ic_baseline_hospital_24)
                    itemView.item_poi_description.text = context.getString(R.string.hospital).capitalize()}
                context.getString(R.string.gym) -> {
                    itemView.item_poi_icone.setImageResource(R.drawable.ic_baseline_gym_24)
                    itemView.item_poi_description.text = context.getString(R.string.gym).capitalize()}
                context.getString(R.string.spa) -> {
                    itemView.item_poi_icone.setImageResource(R.drawable.ic_baseline_spa_24)
                    itemView.item_poi_description.text = context.getString(R.string.spa).capitalize()}
                context.getString(R.string.train_station) -> {
                    itemView.item_poi_icone.setImageResource(R.drawable.ic_baseline_train_24)
                    itemView.item_poi_description.text = context.getString(R.string.train).capitalize()}
            }

        }
    }
}