package com.openclassrooms.realestatemanager.database

import android.content.ContentValues
import androidx.room.OnConflictStrategy
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.realestatemanager.utils.IC_POI
import com.openclassrooms.realestatemanager.utils.TYPE_POI

/**
 * Fake Populate Database to test application
 */
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
            val listOfPoi : MutableList<ContentValues> = ArrayList()

            val restaurant = ContentValues()
            restaurant.put(TYPE_POI, "restaurant")
            restaurant.put(IC_POI, "ic_baseline_restaurant_24")
            listOfPoi.add(restaurant)
            val store = ContentValues()
            store.put(TYPE_POI, "store")
            store.put(IC_POI, "ic_baseline_store_24")
            listOfPoi.add(store)
            val subway = ContentValues()
            subway.put(TYPE_POI, "subway")
            subway.put(IC_POI, "ic_baseline_subway_24")
            listOfPoi.add(subway)
            val park = ContentValues()
            park.put(TYPE_POI, "park")
            park.put(IC_POI, "ic_baseline_park_24")
            listOfPoi.add(park)
            val school = ContentValues()
            school.put(TYPE_POI, "school")
            school.put(IC_POI, "ic_baseline_school_24")
            listOfPoi.add(school)
            val museum = ContentValues()
            museum.put(TYPE_POI, "museum")
            museum.put(IC_POI, "ic_baseline_museum_24")
            listOfPoi.add(museum)
            val doctor = ContentValues()
            doctor.put(TYPE_POI, "doctor")
            doctor.put(IC_POI, "ic_baseline_doctor_24")
            listOfPoi.add(doctor)
            val bank = ContentValues()
            bank.put(TYPE_POI, "bank")
            bank.put(IC_POI, "ic_baseline_bank_24")
            listOfPoi.add(bank)
            val airport = ContentValues()
            airport.put(TYPE_POI, "airport")
            airport.put(IC_POI, "ic_baseline_airport_24")
            listOfPoi.add(airport)
            val bar = ContentValues()
            bar.put(TYPE_POI, "bar")
            bar.put(IC_POI, "ic_baseline_bar_24")
            listOfPoi.add(bar)
            val hospital = ContentValues()
            hospital.put(TYPE_POI, "hospital")
            hospital.put(IC_POI, "ic_baseline_hospital_24")
            listOfPoi.add(hospital)
            val gym = ContentValues()
            gym.put(TYPE_POI, "gym")
            gym.put(IC_POI,"ic_baseline_gym_24")
            listOfPoi.add(gym)
            val spa = ContentValues()
            spa.put(TYPE_POI, "spa")
            spa.put(IC_POI, "ic_baseline_spa_24")
            listOfPoi.add(spa)
            val trainStation = ContentValues()
            trainStation.put(TYPE_POI, "train_station")
            trainStation.put(IC_POI, "ic_baseline_train_24")
            listOfPoi.add(trainStation)

            return listOfPoi
        }

        private fun addHousing() : ContentValues
        {
            val housing = ContentValues()
            housing.put("reference", reference)
            housing.put("type", "House")
            housing.put("price", 1000000.0)
            housing.put("area", 123.4)
            housing.put("state", "On sale")
            housing.put("dateEntry", "02/07/2020")
            housing.put("onFirestore", false)

            return housing
        }

        private fun addAddress() : ContentValues
        {
            val address = ContentValues()
            address.put("id", "idAddress")
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

            return housingEstateAgent
        }

        private fun addHousingPoi() : List<ContentValues>
        {
            val housingPoi1 = ContentValues()
            housingPoi1.put("housing_reference", reference)
            housingPoi1.put("poi_type", "restaurant")

            val housingPoi2 = ContentValues()
            housingPoi2.put("housing_reference", reference)
            housingPoi2.put("poi_type", "store")


            val housingPoi3 = ContentValues()
            housingPoi3.put("housing_reference", reference)
            housingPoi3.put("poi_type", "subway")


            return listOf(housingPoi1, housingPoi2, housingPoi3)
        }
    }
}