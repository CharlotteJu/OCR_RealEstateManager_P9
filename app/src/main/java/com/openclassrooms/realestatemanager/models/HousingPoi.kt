package com.openclassrooms.realestatemanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity (tableName = "housing_poi",
        primaryKeys = ["housing_reference", "poi_type"],
        foreignKeys = [ForeignKey(entity = Housing::class,
                                    parentColumns = ["reference"],
                                    childColumns = ["housing_reference"]),

                        ForeignKey(entity = Poi::class,
                                        parentColumns = ["type"],
                                        childColumns = ["poi_type"])])

data class HousingPoi @JvmOverloads constructor (@ColumnInfo (name = "housing_reference", index = true) val housingReference : String,
                  @ColumnInfo (name = "poi_type", index = true) val poiType : String,
                  @ColumnInfo (name = "number_of_poi") var numberOfPoi : Int)

{}