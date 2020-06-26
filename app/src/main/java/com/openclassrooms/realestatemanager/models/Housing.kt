package com.openclassrooms.realestatemanager.models

import androidx.room.*
import java.util.Date
import java.sql.Timestamp

@Entity (tableName = "housing")
data class Housing @JvmOverloads constructor (@PrimaryKey @ColumnInfo(name = "reference") val ref : String,
                                              @ColumnInfo (name = "type") var type : String,
                                              @ColumnInfo (name = "price") var price : Double,
                                              @ColumnInfo (name = "area") var area : Double? = null,
                                              @ColumnInfo (name = "rooms") var rooms : Int? = null,
                                              @ColumnInfo (name = "bedrooms") var bedrooms : Int?= null,
                                              @ColumnInfo (name = "bathrooms") var bathrooms : Int?= null,
                                              @ColumnInfo (name = "state") var state : String,
                                              @ColumnInfo (name = "dateEntry") var dateEntry : String,
                                              @ColumnInfo (name = "dateSale")  var dateSale : String? = null,
                                              @ColumnInfo (name = "description") var description : String? = null,
                                              @Ignore var poiList : List<HousingPoi>? = null,
                                              @Ignore var estateAgentList: List<HousingEstateAgent>? = null,
                                              @Ignore var photoList : List<Photo>?= null,
                                              @Ignore var address: Address? = null)
{}