package com.openclassrooms.realestatemanager.models

import android.content.ContentValues
import android.provider.ContactsContract
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.openclassrooms.realestatemanager.utils.STRING_EMPTY

@Entity (tableName = "estate_agent")
data class EstateAgent constructor(@PrimaryKey @ColumnInfo(name = "last_name") var lastName : String = STRING_EMPTY,
                                   @ColumnInfo(name = "first_name") var firstName : String? = null,
                                   @ColumnInfo (name = "email") var email: String? = null,
                                   @ColumnInfo (name = "phone_number") var phoneNumber : String? = null)
{

    companion object {
        fun fromContentValues(contentValues: ContentValues) : EstateAgent
        {
            val estateAgent = EstateAgent()
            if (contentValues.containsKey("last_name")) estateAgent.lastName = contentValues.getAsString("last_name")
            if (contentValues.containsKey("first_name")) estateAgent.firstName = contentValues.getAsString("first_name")
            if (contentValues.containsKey("email")) estateAgent.email = contentValues.getAsString("email")
            if (contentValues.containsKey("phone_number")) estateAgent.phoneNumber = contentValues.getAsString("phone_number")

            return estateAgent
        }
    }
}