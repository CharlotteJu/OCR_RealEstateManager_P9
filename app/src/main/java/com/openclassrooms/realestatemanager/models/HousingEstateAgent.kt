package com.openclassrooms.realestatemanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity (tableName = "housing_estate_agent", foreignKeys = [ForeignKey(entity = Housing::class, parentColumns = ["reference"], childColumns = ["housing_reference"]),
                                                            ForeignKey(entity = EstateAgent::class, parentColumns = ["last_name"], childColumns = ["estate_agent_name"])])
class HousingEstateAgent (@PrimaryKey @ColumnInfo (name = "housing_reference") val housingReference : String,
                          @PrimaryKey @ColumnInfo (name = "estate_agent_name") val estateAgentName : String,
                          @ColumnInfo (name = "function") var function : String?)

{}