package com.openclassrooms.realestatemanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity (tableName = "housing_poi",
        foreignKeys = [ForeignKey(entity = Housing::class,
                                    parentColumns = ["reference"],
                                    childColumns = ["housing_reference"]),

                        ForeignKey(entity = Poi::class,
                                        parentColumns = ["type"],
                                        childColumns = ["poi_type"])])

class HousingPoi (@PrimaryKey @ColumnInfo (name = "housing_reference") val housingReference : String,
                  @PrimaryKey @ColumnInfo (name = "poi_type") val poiType : String,
                  @ColumnInfo (name = "number_of_poi") var numberOfPoi : Int)

{}