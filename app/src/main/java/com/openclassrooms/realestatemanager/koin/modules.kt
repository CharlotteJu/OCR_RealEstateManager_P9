package com.openclassrooms.realestatemanager.koin

import com.openclassrooms.realestatemanager.daos.HousingDAO
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.viewModels.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single{AppDatabase.getDatabase(get())}

    single{get<AppDatabase>().housingDao()}
    single{get<AppDatabase>().addressDao()}
    single{get<AppDatabase>().estateAgentDao()}
    single{get<AppDatabase>().photoDao()}
    single{get<AppDatabase>().poiDao()}
    single{get<AppDatabase>().housingEstateAgentDao()}
    single{get<AppDatabase>().housingPoiDao()}

    viewModel{AddEstateTypeViewModel(get(), get())}
    viewModel{AddUpdateHousingViewModel(get(), get(), get(), get(), get())}
    viewModel{DetailViewModel(get(), get(), get(), get(), get())}
    viewModel{ListHousingViewModel(get(), get(), get(), get(), get())}
    factory { ViewModelFactory(get(), get(), get(), get(), get(), get(), get()) }
}