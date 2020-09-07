package com.openclassrooms.realestatemanager.koin

import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.repositories.*
import com.openclassrooms.realestatemanager.viewModels.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module (override = true) {

    single{AppDatabase.getDatabase(get())}

    single{get<AppDatabase>().housingDao()}
    single{get<AppDatabase>().addressDao()}
    single{get<AppDatabase>().estateAgentDao()}
    single{get<AppDatabase>().photoDao()}
    single{get<AppDatabase>().poiDao()}
    single{get<AppDatabase>().housingEstateAgentDao()}
    single{get<AppDatabase>().housingPoiDao()}

    single<HousingRepository>{HousingRepository(get())}
    single<AddressRepository>{ AddressRepository(get()) }
    single<EstateAgentRepository>{ EstateAgentRepository(get()) }
    single<HousingEstateAgentRepository>{ HousingEstateAgentRepository(get()) }
    single<HousingPoiRepository>{ HousingPoiRepository(get()) }
    single<PhotoRepository>{ PhotoRepository(get()) }
    single<PoiRepository>{PoiRepository(get())}
    single<PlacesPoiRepository>{PlacesPoiRepository()}

    viewModel{AddEstateTypeViewModel(get(), get())}
    viewModel{AddUpdateHousingViewModel(get(), get(), get(), get(), get(), get(), get(), get())}
    viewModel{DetailViewModel(get())}
    viewModel{ListHousingViewModel(get(), get(), get(), get(), get(), get())}
    viewModel { FilterViewModel (get(), get()) }
    factory { ViewModelFactory(get(), get(), get(), get(), get(), get(), get(), get()) }
}