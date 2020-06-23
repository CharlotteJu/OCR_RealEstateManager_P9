package com.openclassrooms.realestatemanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity (tableName = "photo",
        foreignKeys = [ForeignKey(entity = Housing::class,
                                    parentColumns = ["reference"],
                                    childColumns = ["housing_reference"])])

class Photo (@PrimaryKey @ColumnInfo(name = "url") var uri : String,
             @ColumnInfo (name = "description") var description : String?,
             @ColumnInfo (name = "housing_reference") val housingReference : String)
{}