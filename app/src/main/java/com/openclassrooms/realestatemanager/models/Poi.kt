package com.openclassrooms.realestatemanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "poi")
data class Poi (@PrimaryKey @ColumnInfo(name = "type") val type : String,
           @ColumnInfo(name = "ic") var ic : String)
{}