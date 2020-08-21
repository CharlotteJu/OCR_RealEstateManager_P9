package com.openclassrooms.realestatemanager.models

import android.content.ContentValues
import androidx.room.*
import com.openclassrooms.realestatemanager.utils.DOUBLE_00
import com.openclassrooms.realestatemanager.utils.STRING_EMPTY
import java.util.Date
import java.sql.Timestamp

@Entity (tableName = "housing")
data class Housing constructor (@PrimaryKey @ColumnInfo(name = "reference") var ref : String = STRING_EMPTY,
                                @ColumnInfo (name = "type") var type : String = STRING_EMPTY,
                                @ColumnInfo (name = "price") var price : Double = DOUBLE_00,
                                @ColumnInfo (name = "area") var area : Double = DOUBLE_00,
                                @ColumnInfo (name = "rooms") var rooms : Int? = null,
                                @ColumnInfo (name = "bedrooms") var bedrooms : Int?= null,
                                @ColumnInfo (name = "bathrooms") var bathrooms : Int?= null,
                                @ColumnInfo (name = "state") var state : String = STRING_EMPTY,
                                @ColumnInfo (name = "dateEntry") var dateEntry : String = STRING_EMPTY,
                                @ColumnInfo (name = "dateSale")  var dateSale : String? = null,
                                @ColumnInfo (name = "description") var description : String? = null)

{

    companion object {

        fun fromContentValues(contentValues: ContentValues) : Housing
        {
            val housing = Housing()
            if (contentValues.containsKey("reference")) housing.ref = contentValues.getAsString("reference")
            if (contentValues.containsKey("type")) housing.type = contentValues.getAsString("type")
            if (contentValues.containsKey("price")) housing.price = contentValues.getAsDouble("price")
            if (contentValues.containsKey("area")) housing.area = contentValues.getAsDouble("area")
            if (contentValues.containsKey("rooms")) housing.rooms = contentValues.getAsInteger("rooms")
            if (contentValues.containsKey("bedrooms")) housing.bedrooms = contentValues.getAsInteger("bedrooms")
            if (contentValues.containsKey("bathrooms")) housing.bathrooms = contentValues.getAsInteger("bathrooms")
            if (contentValues.containsKey("state")) housing.state = contentValues.getAsString("state")
            if (contentValues.containsKey("dateEntry")) housing.dateEntry = contentValues.getAsString("dateEntry")
            if (contentValues.containsKey("dateSale")) housing.dateSale = contentValues.getAsString("dateSale")
            if (contentValues.containsKey("description")) housing.description = contentValues.getAsString("description")

            return housing

        }
    }
}