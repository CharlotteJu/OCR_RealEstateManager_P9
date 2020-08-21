package com.openclassrooms.realestatemanager.providers

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.models.CompleteHousing
import java.lang.IllegalArgumentException

class MyContentProvider : ContentProvider() {

    private val authority = "com.openclassrooms.realestatemanager.providers"
    private val tableName = CompleteHousing::class.java.simpleName
    private val uri = Uri.parse("content://$authority/$tableName")

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        if (context != null)
        {
            val reference = ContentUris.parseId(uri).toString()//TODO-Q : Récupérer String
            val cursor = AppDatabase.getDatabase(context!!).housingDao().getCompleteHousingCursor(reference)
            cursor.setNotificationUri(context!!.contentResolver, uri)
            return cursor
        }
        else return null

       // throw object : IllegalArgumentException("FAILED TO QUERY ROW FOR URI $uri")//TODO-Q : Ne suffit pas sans else ?

    }

    override fun onCreate(): Boolean {
       return true
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }
}