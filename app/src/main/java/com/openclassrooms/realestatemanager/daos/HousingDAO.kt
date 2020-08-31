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


  /* @Transaction
    @Query("""
        SELECT *, count(*) as cnt FROM housing as h JOIN photo as ph ON h.reference == ph.housing_reference, housing_poi as poi, housing_estate_agent as ea, address as a
        WHERE
        h.type = :type
        AND h.price BETWEEN :priceLower AND :priceHigher
        AND h.area BETWEEN :areaLower AND :areaHigher
        AND h.rooms BETWEEN :roomLower AND :roomHigher
        AND h.bedrooms BETWEEN :bedRoomLower AND :bedRoomHigher
        AND h.bathrooms BETWEEN :bathRoomLower AND :bathRoomHigher
        AND h.state = :state
        AND dateEntry >= DateTime(:dateEntry)
        AND dateSale >= DateTime(:dateSale)
        AND poi.poi_type = :typePoi
        AND a.city LIKE lower(:city)
        AND a.country = :country
        AND ea.estate_agent_name = :estateAgent 
        AND cnt >= :numberPhotos  /*TODO : Voir pour listPhoto*/
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
                                     estateAgent : String? = null): LiveData<List<CompleteHousing>>*/



    /* @Query ("""
         SELECT housing_reference, count(*) FROM housing JOIN photo ON housing.reference == photo.housing_reference
         """)
     fun testPhoto2(numberPhotos : Int): LiveData<List<Housing>>*/

     @Query ("SELECT count(*) FROM photo WHERE housing_reference ==:housingReference")
     fun testPhoto(housingReference : String): LiveData<Int>




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

