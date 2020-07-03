package com.openclassrooms.realestatemanager.database

import android.content.ContentValues
import androidx.room.OnConflictStrategy
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

class FakePopulateDatabase
{
    companion object {

        private val reference = "HOUSE001"
        private val name = "JUDON"

        fun prepopulateDatabase() : RoomDatabase.Callback {
            return object : RoomDatabase.Callback()
            {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    db.insert("estate_agent", OnConflictStrategy.IGNORE, addEstateAgent())
                    for (i in addPoi())
                    {
                        db.insert("poi", OnConflictStrategy.IGNORE, i)
                    }
                    db.insert("housing", OnConflictStrategy.REPLACE, addHousing())
                    db.insert("address", OnConflictStrategy.IGNORE, addAddress())
                    db.insert("photo", OnConflictStrategy.IGNORE, addPhoto())
                    db.insert("housing_estate_agent", OnConflictStrategy.IGNORE, addHousingEstateAgent())
                    for (i in addHousingPoi())
                    {
                        db.insert("housing_poi", OnConflictStrategy.IGNORE, i)
                    }
                }
            }
        }

        private fun addEstateAgent() : ContentValues
        {
            val estate = ContentValues()
            estate.put("last_name", name)
            estate.put("first_name", "Charlotte")
            estate.put("email", "email@email.fr")
            estate.put("phone_number", "0000000000")
            return estate
        }

        private fun addPoi() : List<ContentValues>
        {
            val poi1 = ContentValues()
            poi1.put("type", "restaurant")
            poi1.put("ic", "ic_baseline_restaurant_24")

            val poi2 = ContentValues()
            poi2.put("type", "store")
            poi2.put("ic", "ic_baseline_store_24")

            val poi3 = ContentValues()
            poi3.put("type", "subway")
            poi3.put("ic", "ic_baseline_subway_24")

            return listOf(poi1, poi2, poi3)
        }

        private fun addHousing() : ContentValues
        {
            val housing = ContentValues()
            housing.put("reference", reference)
            housing.put("type", "House")
            housing.put("price", 1000000.0)
            housing.put("state", "On sale")
            housing.put("dateEntry", "02/07/2020")

            return housing
        }

        private fun addAddress() : ContentValues
        {
            val address = ContentValues()
            address.put("id", 0)
            address.put("street", "38 Rue de Bellevue")
            address.put("city", "Boulogne-Billancourt")
            address.put("country", "FR")
            address.put("housing_reference", reference)

            return address
        }

        private fun addPhoto() : ContentValues
        {
            val photo = ContentValues()
            photo.put("uri", "uri")
            photo.put("housing_reference", reference)

            return photo
        }

        private fun addHousingEstateAgent() : ContentValues
        {
            val housingEstateAgent = ContentValues()
            housingEstateAgent.put("housing_reference", reference)
            housingEstateAgent.put("estate_agent_name", name)
            housingEstateAgent.put("function", "Seller")

            return housingEstateAgent
        }

        private fun addHousingPoi() : List<ContentValues>
        {
            val housingPoi1 = ContentValues()
            housingPoi1.put("housing_reference", reference)
            housingPoi1.put("poi_type", "restaurant")
            housingPoi1.put("number_of_poi", 12)

            val housingPoi2 = ContentValues()
            housingPoi2.put("housing_reference", reference)
            housingPoi2.put("poi_type", "store")
            housingPoi2.put("number_of_poi", 20)

            val housingPoi3 = ContentValues()
            housingPoi3.put("housing_reference", reference)
            housingPoi3.put("poi_type", "subway")
            housingPoi3.put("number_of_poi", 3)

            return listOf(housingPoi1, housingPoi2, housingPoi3)
        }
    }
}