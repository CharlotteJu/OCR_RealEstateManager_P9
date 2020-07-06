package com.openclassrooms.realestatemanager.models

import androidx.room.*
import java.util.Date
import java.sql.Timestamp

@Entity (tableName = "housing")
data class Housing constructor (@PrimaryKey @ColumnInfo(name = "reference") val ref : String,
                                @ColumnInfo (name = "type") var type : String,
                                @ColumnInfo (name = "price") var price : Double,
                                @ColumnInfo (name = "area") var area : Double? = null,
                                @ColumnInfo (name = "rooms") var rooms : Int? = null,
                                @ColumnInfo (name = "bedrooms") var bedrooms : Int?= null,
                                @ColumnInfo (name = "bathrooms") var bathrooms : Int?= null,
                                @ColumnInfo (name = "state") var state : String,
                                @ColumnInfo (name = "dateEntry") var dateEntry : String,
                                @ColumnInfo (name = "dateSale")  var dateSale : String? = null,
                                @ColumnInfo (name = "description") var description : String? = null)

                                /*@Ignore var poiList : List<HousingPoi>? = null,
                                @Ignore var  estateAgentList: List<HousingEstateAgent>? = null,
                                @Ignore var  photoList : List<Photo>?= null,
                                @Ignore var address: Address? = null*/

{
    /*@Ignore var poiList : List<HousingPoi>? = null
    @Ignore var  estateAgentList: List<HousingEstateAgent>? = null
    @Ignore var  photoList : List<Photo>?= null
    @Ignore var address: Address? = null*/

    /*constructor(ref : String,
        type : String,
        price : Double,
        area : Double? = null,
        rooms : Int? = null,
        bedrooms : Int?= null,
        bathrooms : Int?= null,
        state : String,
        dateEntry : String,
        dateSale : String? = null,
        description : String? = null,

        poiList : List<HousingPoi>? = null,
        estateAgentList: List<HousingEstateAgent>? = null,
        photoList : List<Photo>?= null,
        address: Address? = null) : this("","",0.0,0.0,0,0,0,"","","","")*/


    /*private var address : Address? = null
    private var estateAgentList : List<HousingEstateAgent>? = null
    private var photoList : List<Photo>? = null
    private var poiList : List<HousingPoi>? = null

    fun getAddress(): Address?  = this.address
    fun getEstateAgentList() : List<HousingEstateAgent>? = this.estateAgentList
    fun getPhotoList() :  List<Photo>? = this.photoList
    fun getPoiList() : List<HousingPoi>? = this.poiList

    fun setAddress(address: Address)
    {
        this.address = address
    }

    fun setEstateAgentList(estateAgentList : List<HousingEstateAgent>)
    {
        this.estateAgentList = estateAgentList
    }

    fun setPhotoList(photoList : List<Photo>)
    {
        this.photoList = photoList
    }

    fun setPoi(poiList : List<HousingPoi>)
    {
        this.poiList = poiList
    }*/





}