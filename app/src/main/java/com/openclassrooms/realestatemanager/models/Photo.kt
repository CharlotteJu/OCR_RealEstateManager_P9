package com.openclassrooms.realestatemanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity (tableName = "photo",
        foreignKeys = [ForeignKey(entity = Housing::class,
                                    parentColumns = ["reference"],
                                    childColumns = ["housing_reference"])])

data class Photo @JvmOverloads constructor(@PrimaryKey @ColumnInfo(name = "uri") var uri : String,
             @ColumnInfo (name = "description") var description : String? = null,
             @ColumnInfo (name = "housing_reference", index = true) val housingReference : String)
{}