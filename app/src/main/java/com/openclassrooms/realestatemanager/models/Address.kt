package com.openclassrooms.realestatemanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity (tableName = "address",
        foreignKeys = [ForeignKey (entity = Housing::class,
                                    parentColumns = ["reference"],
                                    childColumns = ["housing_reference"])])
data class Address @JvmOverloads constructor (@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id : Int,
               @ColumnInfo (name = "street") var street : String,
               @ColumnInfo (name = "zip_code") var zipCode : Int? = null,
               @ColumnInfo (name = "city") var city : String,
               @ColumnInfo (name = "country") var country : String,
               @ColumnInfo (name = "housing_reference", index = true) val housingReference : String)

{}