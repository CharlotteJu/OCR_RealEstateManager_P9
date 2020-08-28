package com.openclassrooms.realestatemanager.models

import android.content.ContentValues
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.openclassrooms.realestatemanager.utils.STRING_EMPTY

@Entity (tableName = "photo",
        foreignKeys = [ForeignKey(onDelete = ForeignKey.CASCADE, entity = Housing::class,
                                    parentColumns = ["reference"],
                                    childColumns = ["housing_reference"])])

data class Photo constructor(@PrimaryKey @ColumnInfo(name = "uri") var uri : String= STRING_EMPTY,
                             @ColumnInfo (name = "description") var description : String? = null,
                             @ColumnInfo (name = "housing_reference", index = true) var housingReference : String= STRING_EMPTY,
                             @ColumnInfo (name = "url_firebase") var url_firebase : String? = null)
{



    override fun equals(other: Any?): Boolean {
        if (other is Photo)
        {
            return (other.uri == this.uri)
        }
        else return false
    }

    override fun hashCode(): Int {
        return super.hashCode() //TODO : Vraiment besoin ?
    }

    companion object {

        fun fromContentValues(contentValues: ContentValues) : Photo
        {
            val photo = Photo()
            if (contentValues.containsKey("uri")) photo.uri = contentValues.getAsString("uri")
            if (contentValues.containsKey("description")) photo.description = contentValues.getAsString("description")
            if (contentValues.containsKey("housing_reference")) photo.description = contentValues.getAsString("housing_reference")

            return photo
        }
    }
}