package com.openclassrooms.realestatemanager.koin

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RealEstateApplication : Application()
{
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@RealEstateApplication)
            modules(appModule)
        }
    }
}