package com.openclassrooms.realestatemanager.models

import android.content.ContentValues
import androidx.room.*
import com.openclassrooms.realestatemanager.utils.DOUBLE_00
import com.openclassrooms.realestatemanager.utils.STRING_EMPTY

@Entity (tableName = "housing")
data class Housing constructor (@PrimaryKey @ColumnInfo(name = "reference") var ref : String = STRING_EMPTY,
                                @ColumnInfo (name = "type") var type : String = STRING_EMPTY,
                                @ColumnInfo (name = "price") var price : Double = DOUBLE_00,
                                @ColumnInfo (name = "area") var area : Double = DOUBLE_00,
                                @ColumnInfo (name = "rooms") var rooms : Int? = null,
                                @ColumnInfo (name = "bedrooms") var bedrooms : Int?= null,
                                @ColumnInfo (name = "bathrooms") var bathrooms : Int?= null,
                                @ColumnInfo (name = "state") var state : String = STRING_EMPTY,
                                @ColumnInfo (name = "dateEntry") var dateEntry : Long = 0,
                                @ColumnInfo (name = "dateSale")  var dateSale : Long? = null,
                                @ColumnInfo (name = "description") var description : String? = null,
                                @ColumnInfo (name = "lastUpdate") var lastUpdate : Long = 0)

{

    override fun equals(other: Any?): Boolean {
        return other is Housing && other.ref == this.ref
    }

    override fun hashCode(): Int {
        /*var result = ref.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + area.hashCode()
        result = 31 * result + (rooms ?: 0)
        result = 31 * result + (bedrooms ?: 0)
        result = 31 * result + (bathrooms ?: 0)
        result = 31 * result + state.hashCode()
        result = 31 * result + dateEntry.hashCode()
        result = 31 * result + (dateSale?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)*/
        return ref.hashCode()
    }

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
            if (contentValues.containsKey("dateEntry")) housing.dateEntry = contentValues.getAsLong("dateEntry")
            if (contentValues.containsKey("dateSale")) housing.dateSale = contentValues.getAsLong("dateSale")
            if (contentValues.containsKey("description")) housing.description = contentValues.getAsString("description")
            if (contentValues.containsKey("lastUpdate")) housing.lastUpdate = contentValues.getAsLong("lastUpdate")

            return housing

        }
    }
}