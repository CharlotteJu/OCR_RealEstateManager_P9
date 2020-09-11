package com.openclassrooms.realestatemanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.openclassrooms.realestatemanager.utils.STRING_EMPTY

@Entity (tableName = "housing_poi",
        primaryKeys = ["housing_reference", "poi_type"],
        foreignKeys = [ForeignKey(onDelete = ForeignKey.CASCADE, entity = Housing::class,
                                    parentColumns = ["reference"],
                                    childColumns = ["housing_reference"]),

                        ForeignKey(onDelete = ForeignKey.CASCADE, entity = Poi::class,
                                        parentColumns = ["type"],
                                        childColumns = ["poi_type"])])

data class HousingPoi constructor (@ColumnInfo (name = "housing_reference", index = true) var housingReference : String= STRING_EMPTY,
                                   @ColumnInfo (name = "poi_type", index = true) var poiType : String= STRING_EMPTY)
