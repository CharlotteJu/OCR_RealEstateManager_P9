package com.openclassrooms.realestatemanager.models

import androidx.room.*
import com.openclassrooms.realestatemanager.utils.DOUBLE_00
import com.openclassrooms.realestatemanager.utils.STRING_EMPTY
import java.util.Date
import java.sql.Timestamp

@Entity (tableName = "housing")
data class Housing constructor (@PrimaryKey @ColumnInfo(name = "reference") val ref : String = STRING_EMPTY,
                                @ColumnInfo (name = "type") var type : String = STRING_EMPTY,
                                @ColumnInfo (name = "price") var price : Double = DOUBLE_00,
                                @ColumnInfo (name = "area") var area : Double = DOUBLE_00,
                                @ColumnInfo (name = "rooms") var rooms : Int? = null,
                                @ColumnInfo (name = "bedrooms") var bedrooms : Int?= null,
                                @ColumnInfo (name = "bathrooms") var bathrooms : Int?= null,
                                @ColumnInfo (name = "state") var state : String = STRING_EMPTY,
                                @ColumnInfo (name = "dateEntry") var dateEntry : String = STRING_EMPTY,
                                @ColumnInfo (name = "dateSale")  var dateSale : String? = null,
                                @ColumnInfo (name = "description") var description : String? = null)

{}