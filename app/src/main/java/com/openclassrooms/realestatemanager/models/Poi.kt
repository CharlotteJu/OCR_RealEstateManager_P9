package com.openclassrooms.realestatemanager.models

import android.content.ContentValues
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.openclassrooms.realestatemanager.utils.STRING_EMPTY

@Entity (tableName = "poi")
data class Poi (@PrimaryKey @ColumnInfo(name = "type") var type : String= STRING_EMPTY,
                @ColumnInfo(name = "ic") var ic : String = STRING_EMPTY)
{
    companion object {
        fun fromContentValues(contentValues: ContentValues) : Poi
        {
            val poi = Poi()
            if (contentValues.containsKey("type")) poi.type = contentValues.getAsString("type")
            if (contentValues.containsKey("ic")) poi.ic = contentValues.getAsString("ic")

            return poi
        }
    }


}