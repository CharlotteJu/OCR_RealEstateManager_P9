package com.openclassrooms.realestatemanager.models

import androidx.room.ColumnInfo

class CompleteHousing (val ref : String,
                       var type : String,
                       var price : Double,
                       var area : Double? = null,
                       var rooms : Int? = null,
                       var bedrooms : Int?= null,
                       var bathrooms : Int?= null,
                       var state : String,
                       var dateEntry : String,
                       var dateSale : String? = null,
                       var description : String? = null,
                       var address: Address? = null,
                       var poiList: List<HousingPoi>? = null,
                       var photoList: List<Photo>? = null,
                       var estateAgentList: List<HousingEstateAgent>)

{
}