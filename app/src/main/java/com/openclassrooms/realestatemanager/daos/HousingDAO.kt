package com.openclassrooms.realestatemanager.daos

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.openclassrooms.realestatemanager.models.*

/**
 * Interface DAO for [Housing] with [RoomDatabase]
 */
@Dao
interface HousingDAO {
    @Query("SELECT * FROM housing")
    fun getAllHousing(): LiveData<List<Housing>>

    @Query("SELECT * FROM housing WHERE reference =:reference")
    fun getHousing(reference: String): LiveData<Housing>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createHousing(housing: Housing): Long

    @Update
    suspend fun updateHousing(housing: Housing): Int

    @Delete
    suspend fun deleteHousing(housing: Housing)

    @Transaction
    @Query("SELECT * FROM housing WHERE reference = :reference")
    fun getCompleteHousing(reference: String): LiveData<CompleteHousing>


    /*@Transaction
    @Query("""
        SELECT * FROM housing, housing_poi, housing_estate_agent, address, photo 
        WHERE 
        housing.type = :type   
        AND housing.price BETWEEN :priceLower AND :priceHigher 
        AND housing.area BETWEEN :areaLower AND :areaHigher 
        AND housing.rooms BETWEEN :roomLower AND :roomHigher 
        AND housing.bedrooms BETWEEN :bedRoomLower AND :bedRoomHigher
        AND housing.bathrooms BETWEEN :bathRoomLower AND :bathRoomHigher
        AND housing.state = :state
        AND housing.dateEntry /*TODO : Voir pour date*/
        AND housing.dateSale /*TODO : Voir pour date*/
        AND housing_poi.poi_type = :typePoi
        AND address.city LIKE lower(:city)
        AND address.country = :country
        AND housing_estate_agent.estate_agent_name = :estateAgent /*TODO : Voir pour listPhoto*/
        """)
    fun getListCompleteHousingFilter(type : String? = null,
                                     priceLower : Double? = null,
                                     priceHigher : Double? = null,
                                     areaLower : Double? = null,
                                     areaHigher : Double? = null,
                                     roomLower : Int? = null,
                                     roomHigher : Int? = null,
                                     bedRoomLower : Int? = null,
                                     bedRoomHigher : Int? = null,
                                     bathRoomLower : Int? = null,
                                     bathRoomHigher : Int? = null,
                                     state : String? = null,
                                     dateEntry : String? = null,
                                     dateSale : String? = null,
                                     city : String? = null,
                                     country : String? = null,
                                     typePoi : String? = null,
                                     numberPhotos : Int? = null,
                                     estateAgent : String? = null): LiveData<List<CompleteHousing>>


    @Transaction
    @Query("""
        SELECT * FROM housing
        WHERE
        type = :type
        AND price BETWEEN :priceLower AND :priceHigher
        AND area BETWEEN :areaLower AND :areaHigher
        AND rooms BETWEEN :roomLower AND :roomHigher
        AND bedrooms BETWEEN :bedRoomLower AND :bedRoomHigher
        AND bathrooms BETWEEN :bathRoomLower AND :bathRoomHigher
        AND state = :state
        AND dateEntry
        AND dateSale
        """)
    fun getListHousingFilter(type : String? = null,
                             priceLower : Double? = null,
                             priceHigher : Double? = null,
                             areaLower : Double? = null,
                             areaHigher : Double? = null,
                             roomLower : Int? = null,
                             roomHigher : Int? = null,
                             bedRoomLower : Int? = null,
                             bedRoomHigher : Int? = null,
                             bathRoomLower : Int? = null,
                             bathRoomHigher : Int? = null,
                             state : String? = null,
                             dateEntry : String? = null,
                             dateSale : String? = null): LiveData<List<CompleteHousing>>

    @Query("""
        SELECT * FROM address
        WHERE
        city LIKE lower(:city)
        AND country = :country
        """)
    fun getListAddressFilter(city : String? = null, country : String? = null): LiveData<List<Address>>

    @Query("""
        SELECT * FROM housing_poi
        WHERE
        poi_type = :typePoi
        """)
    fun getListPoiFilter(typePoi : String? = null) : LiveData<List<HousingPoi>>

    @Query("""
        SELECT * FROM housing, photo
        WHERE
        uri = housing_reference
        """)
    fun getListPhotoFilter(numberPhotos: Int?): LiveData<List<Photo>>

    @Query("""
        SELECT * FROM housing_estate_agent
        WHERE
        estate_agent_name = :estateAgent
        """)
    fun getListEstateAgentFilter(estateAgent : String? = null) : LiveData<List<HousingEstateAgent>>*/



    //CURSOR

    @Transaction
    @Query("SELECT * FROM housing")
    fun getAllCompleteHousing(): LiveData<List<CompleteHousing>>

    @Query("SELECT * FROM housing")
    fun getAllHousingWithCursor() : Cursor

    @Query("SELECT * FROM housing WHERE reference =:reference")
    fun getHousingWithCursor(reference: String) : Cursor

    @Query("DELETE FROM housing WHERE reference =:reference")
    suspend fun deleteHousingWithCursor (reference: String)

}

