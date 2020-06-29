package com.openclassrooms.realestatemanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity (tableName = "housing_estate_agent",
        primaryKeys = ["housing_reference", "estate_agent_name"],
        foreignKeys = [ForeignKey(entity = Housing::class,
                                    parentColumns = ["reference"],
                                    childColumns = ["housing_reference"]),
                        ForeignKey(entity = EstateAgent::class,
                                    parentColumns = ["last_name"],
                                    childColumns = ["estate_agent_name"])])

data class HousingEstateAgent constructor(@ColumnInfo (name = "housing_reference", index = true) val housingReference : String,
                          @ColumnInfo (name = "estate_agent_name", index = true) val estateAgentName : String,
                          @ColumnInfo (name = "function") var function : String)

{}