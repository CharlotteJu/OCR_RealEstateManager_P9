package com.openclassrooms.realestatemanager.models

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
        return if (other is Photo) (other.uri == this.uri)
        else false
    }

    override fun hashCode(): Int {
        return uri.hashCode()
    }

}