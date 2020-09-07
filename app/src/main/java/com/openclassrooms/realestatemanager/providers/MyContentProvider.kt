package com.openclassrooms.realestatemanager.providers

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.models.Housing
import kotlinx.coroutines.runBlocking
import java.lang.IllegalArgumentException

class MyContentProvider : ContentProvider() {

    companion object{
        const val authority = "com.openclassrooms.realestatemanager.providers"
        val tableName = Housing::class.java.simpleName
        val uri: Uri = Uri.parse("content://$authority/$tableName")
    }



    override fun insert(uri: Uri, values: ContentValues?): Uri? = runBlocking {

       if (context != null)
       {
           val id = values?.let { Housing.fromContentValues(it) }?.let { AppDatabase.getDatabase(context!!).housingDao().createHousing(it) }
           if (id!= 0L && id != null)
           {
               context!!.contentResolver.notifyChange(uri, null)
               return@runBlocking ContentUris.withAppendedId(uri, id)
           }
       }
       throw IllegalArgumentException("Failed to insert uri : $uri")
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        return if (context != null)
        {
            /*val index = ContentUris.parseId(uri)
            val reference = ContentUris.withAppendedId(uri, index).toString()

            val cursorHousing = AppDatabase.getDatabase(context!!).housingDao().getHousingWithCursor(reference)*/

            val cursor = AppDatabase.getDatabase(context!!).housingDao().getAllHousingWithCursor()
            cursor.setNotificationUri(context!!.contentResolver, uri)
            cursor
        }
        else null
    }

    override fun onCreate(): Boolean {
       return true
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = runBlocking {

        if (context != null) {
            val count = values?.let { Housing.fromContentValues(it) }?.let { AppDatabase.getDatabase(context!!).housingDao().updateHousing(it) }
            context!!.contentResolver.notifyChange(uri, null)
            return@runBlocking count
        }
        throw IllegalArgumentException("Failed to update uri : $uri")
    }!!

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int { return 0 } 

    override fun getType(uri: Uri): String? { return "vnd.android.cursor.item/$authority.$tableName" }
}