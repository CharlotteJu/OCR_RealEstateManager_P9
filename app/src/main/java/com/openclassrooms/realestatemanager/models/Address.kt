package com.openclassrooms.realestatemanager.models

import android.content.ContentValues
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.openclassrooms.realestatemanager.utils.STRING_EMPTY

@Entity (tableName = "address",
        foreignKeys = [ForeignKey (onDelete = CASCADE, entity = Housing::class,
                                    parentColumns = ["reference"],
                                    childColumns = ["housing_reference"])])
data class Address constructor (@PrimaryKey @ColumnInfo(name = "id") var id : String = STRING_EMPTY,
               @ColumnInfo (name = "street") var street : String = STRING_EMPTY,
               @ColumnInfo (name = "zip_code") var zipCode : Int? = null,
               @ColumnInfo (name = "city") var city : String = STRING_EMPTY,
               @ColumnInfo (name = "country") var country : String = STRING_EMPTY,
               @ColumnInfo (name = "housing_reference", index = true) var housingReference : String = STRING_EMPTY)

{
    override fun toString(): String {
        return "$street, $city, $country "
    }

    companion object {
        fun fromContentValues(contentValues: ContentValues) : Address
        {
            val address = Address()
            if (contentValues.containsKey("id")) address.id = contentValues.getAsString("id")
            if (contentValues.containsKey("street")) address.street = contentValues.getAsString("street")
            if (contentValues.containsKey("zip_code")) address.zipCode = contentValues.getAsInteger("zip_code")
            if (contentValues.containsKey("city")) address.city = contentValues.getAsString("city")
            if (contentValues.containsKey("country")) address.country = contentValues.getAsString("country")
            if (contentValues.containsKey("housing_reference")) address.housingReference = contentValues.getAsString("housing_reference") //TODO : Housing_reference pour plusieurs objets ?

            return address
        }
    }
}