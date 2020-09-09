package com.openclassrooms.realestatemanager.koin

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class RealEstateApplication : Application()
{
    override fun onCreate() {
        super.onCreate()
        startKoin {
            //To launch Koin
            androidContext(this@RealEstateApplication)
            modules(appModule)
        }

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        //To can use Multidex < API 21
        MultiDex.install(this)
    }
}