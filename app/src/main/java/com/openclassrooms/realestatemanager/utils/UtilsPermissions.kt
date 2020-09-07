package com.openclassrooms.realestatemanager.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

class UtilsPermissions
{
    companion object
    {

        @RequiresApi(Build.VERSION_CODES.M)
        fun checkLocationPermission(activity: Activity)
        {
            if (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED)
            {
                val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
                activity.requestPermissions(permissions, LOCATION_PERMISSION_CODE)
            }
        }

        @RequiresApi(Build.VERSION_CODES.M)
        private fun checkCameraPermission(activity: Activity)
        {
            if (activity.checkSelfPermission(Manifest.permission.CAMERA)  != PackageManager.PERMISSION_GRANTED)
            {
                val permissions = arrayOf(Manifest.permission.CAMERA)
                activity.requestPermissions(permissions, CAMERA_PERMISSION_CODE)
            }
        }


        @RequiresApi(Build.VERSION_CODES.M)
        private fun checkReadPermission(activity: Activity)
        {

           if (activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                activity.requestPermissions(permissions, READ_EXTERNAL_STORAGE_PERMISSION_CODE)
            }
        }

        @RequiresApi(Build.VERSION_CODES.M)
        private fun checkWritePermission(activity: Activity)
        {
            if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                activity.requestPermissions(permissions, WRITE_EXTERNAL_STORAGE_PERMISSION_CODE)
            }
        }


        @RequiresApi(Build.VERSION_CODES.M)
        fun checkPhotosPermission(activity: Activity)
        {
            if ((activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    && (activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    && (activity.checkSelfPermission(Manifest.permission.CAMERA)  != PackageManager.PERMISSION_GRANTED))
            {
                val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                activity.requestPermissions(permissions, PHOTOS_PERMISSION_CODE)
            }
            else
            {
                this.checkReadPermission(activity)
                this.checkCameraPermission(activity)
                this.checkWritePermission(activity)
            }
        }

    }

}