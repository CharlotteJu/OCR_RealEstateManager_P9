package com.openclassrooms.realestatemanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.openclassrooms.realestatemanager.utils.STRING_EMPTY

@Entity (tableName = "poi")
data class Poi (@PrimaryKey @ColumnInfo(name = "type") var type : String= STRING_EMPTY,
                @ColumnInfo(name = "ic") var ic : String = STRING_EMPTY)
