package com.openclassrooms.realestatemanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.openclassrooms.realestatemanager.utils.STRING_EMPTY

@Entity (tableName = "estate_agent")
data class EstateAgent constructor(@PrimaryKey @ColumnInfo(name = "last_name") var lastName : String = STRING_EMPTY,
                                   @ColumnInfo(name = "first_name") var firstName : String? = null,
                                   @ColumnInfo (name = "email") var email: String? = null,
                                   @ColumnInfo (name = "phone_number") var phoneNumber : String? = null,
                                   @ColumnInfo (name = "last_update_estate") var lastUpdateEstate : Long = 0)
{
    override fun equals(other: Any?): Boolean {
        return if (other is EstateAgent) (other.lastName == this.lastName)
        else false
    }

    override fun hashCode(): Int {
        return lastName.hashCode()
    }

}