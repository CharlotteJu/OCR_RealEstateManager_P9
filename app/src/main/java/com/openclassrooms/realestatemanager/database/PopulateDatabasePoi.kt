package com.openclassrooms.realestatemanager.database

import android.content.ContentValues
import androidx.room.OnConflictStrategy
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.RoomDatabase
import com.openclassrooms.realestatemanager.utils.IC_POI
import com.openclassrooms.realestatemanager.utils.TYPE_POI

object PopulateDatabasePoi
{

    fun prepopulateDatabase() : RoomDatabase.Callback {
        return object : RoomDatabase.Callback()
        {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                for (i in addPoi())
                {
                    db.insert("poi", OnConflictStrategy.IGNORE, i)
                }
            }
        }
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
}