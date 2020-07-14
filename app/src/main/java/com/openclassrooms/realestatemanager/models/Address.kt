package com.openclassrooms.realestatemanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

//TODO : Delete CASCADE
@Entity (tableName = "address",
        foreignKeys = [ForeignKey (onDelete = CASCADE, entity = Housing::class,
                                    parentColumns = ["reference"],
                                    childColumns = ["housing_reference"])])
data class Address constructor (@PrimaryKey @ColumnInfo(name = "id") val id : String,
               @ColumnInfo (name = "street") var street : String,
               @ColumnInfo (name = "zip_code") var zipCode : Int? = null,
               @ColumnInfo (name = "city") var city : String,
               @ColumnInfo (name = "country") var country : String,
               @ColumnInfo (name = "housing_reference", index = true) val housingReference : String)

{
    override fun toString(): String {
        return "$street, $city, $country "
    }
}