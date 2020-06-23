package com.openclassrooms.realestatemanager

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.openclassrooms.realestatemanager.daos.HousingDAO
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.models.Housing
import com.openclassrooms.realestatemanager.utils.LiveDataTestUtil
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.lang.Exception
import java.sql.Timestamp
import junit.framework.TestCase.assertTrue
import java.util.ArrayList

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest
{
    private lateinit var database : AppDatabase
    private lateinit var housingDAO: HousingDAO
    private var housing = Housing("REF",
            "TYPE",
            100000.00,
            100.00,
            10,
            5,
            3,
            "STATE",
            description = "DESCRIPTION",
            dateEntry = Timestamp(1592916745),
            dateSale = null)

    @get:Rule var rule  = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun initDatabase()
    {
        val context = ApplicationProvider.getApplicationContext<Context>()
        this.database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        housingDAO = database.housingDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase()
    {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun createHousing()
    {
        housingDAO.createHousing(housing)
        val housingTest = LiveDataTestUtil.getValue(this.housingDAO.getHousing("REF"))
        val listHousing : List<Housing>? = LiveDataTestUtil.getValue(this.housingDAO.getAllHousing())
        assertTrue(listHousing?.size == 3)
        assertTrue(housingTest != null)
    }

}