package com.openclassrooms.realestatemanager.providers

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import androidx.room.Room
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.openclassrooms.realestatemanager.database.AppDatabase
import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.Before

import org.junit.Assert.*
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class MyContentProviderTest {

    private lateinit var contentResolver : ContentResolver
    private val userId = 1L
    private lateinit var appDatabase: AppDatabase

    @Before
    fun setUp() = runBlocking {

        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().context, AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()

        contentResolver = InstrumentationRegistry.getInstrumentation().context.contentResolver

        val context = ApplicationProvider.getApplicationContext<Context>()
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
    }

    @Test
    fun a_getHousingWhenNoHousingInserted()
    {
        val cursor = contentResolver.query(ContentUris.withAppendedId(MyContentProvider.uri, userId), null, null, null, null)
        assertNotNull(cursor)
        assertEquals(cursor!!.count, 0)
        cursor.close()
    }

    @Test
    fun b_insertHousingAndGetList() = runBlocking{
        val uri = contentResolver.insert(MyContentProvider.uri, insertHousing())
        val cursor = contentResolver.query(ContentUris.withAppendedId(MyContentProvider.uri, userId), null, null, null, null)
        assertNotNull(cursor)
        assertEquals(cursor!!.count, 1)
        assertTrue(cursor.moveToFirst())
        assertEquals(cursor.getString(cursor.getColumnIndexOrThrow("type")), "House")
        cursor.close()
    }

    @Test //TODO : Ne marche pas - Ne veut pas accéder aux getColumnIndex
    fun c_updateHousingAndCheckIt()= runBlocking{

        contentResolver.update(MyContentProvider.uri, updateHousing(), null, null)
        val cursor = contentResolver.query(ContentUris.withAppendedId(MyContentProvider.uri, userId), null, null, null, null)
        assertNotNull(cursor)
        assertEquals(cursor!!.count, 1)
        assertEquals(cursor.getString(cursor.getColumnIndexOrThrow("type")), "Loft")
        deleteHousingFromBDD(cursor.getString(cursor.getColumnIndexOrThrow("reference")))
        cursor.close()
    }

    private fun insertHousing() : ContentValues
    {
        val housing = ContentValues()

        housing.put("reference", "REFERENCE")
        housing.put("type", "House")
        housing.put("price", 1000000.0)
        housing.put("area", 123.4)
        housing.put("state", "On sale")
        housing.put("dateEntry", "02/07/2020")

        return housing
    }

    private fun updateHousing() : ContentValues
    {
        val housing = ContentValues()

        housing.put("reference", "REFERENCE")
        housing.put("type", "Loft")
        housing.put("price", 1000000.0)
        housing.put("area", 123.4)
        housing.put("state", "On sale")
        housing.put("dateEntry", "02/07/2020")

        return housing
    }

    private fun deleteHousingFromBDD(reference : String)
    {
        appDatabase.query(SimpleSQLiteQuery("DELETE FROM housing WHERE reference =$reference"))
    }
}