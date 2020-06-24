package com.openclassrooms.realestatemanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.Date
import java.sql.Timestamp

@Entity (tableName = "housing")
data class Housing (@PrimaryKey @ColumnInfo(name = "reference") val ref : String,
                    @ColumnInfo (name = "type") var type : String,
                    @ColumnInfo (name = "price") var price : Double,
                    @ColumnInfo (name = "area") var area : Double?,
                    @ColumnInfo (name = "rooms") var rooms : Int?,
                    @ColumnInfo (name = "bedrooms") var bedrooms : Int?,
                    @ColumnInfo (name = "bathrooms") var bathrooms : Int?,
                    @ColumnInfo (name = "state") var state : String,
                    @ColumnInfo (name = "dateEntry") var dateEntry : String,
                    @ColumnInfo (name = "dateSale")  var dateSale : String?,
                    @ColumnInfo (name = "description") var description : String?)
{}