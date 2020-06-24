package com.openclassrooms.realestatemanager

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.openclassrooms.realestatemanager.daos.*
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.models.*
import com.openclassrooms.realestatemanager.utils.LiveDataTestUtil
import junit.framework.TestCase.*
import org.junit.runner.RunWith
import java.io.IOException
import java.lang.Exception
import org.junit.*
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class AppDatabaseTest
{

    private lateinit var database : AppDatabase
    private lateinit var housingDAO: HousingDAO
    private lateinit var addressDAO: AddressDAO
    private lateinit var estateAgentDAO: EstateAgentDAO
    private lateinit var photoDAO: PhotoDAO
    private lateinit var poiDAO: PoiDAO
    private lateinit var housingEstateAgentDAO: HousingEstateAgentDAO
    private lateinit var housingPoiDAO: HousingPoiDAO

    private val reference = "REF"
    private val name = "DUPONT"
    private val typePoi = "TYPEPOI"

    private var housing = Housing(reference,
            "TYPE",
            100000.00,
            100.00,
            10,
            5,
            3,
            "STATE",
            description = "DESCRIPTION",
            dateEntry = "20200624",
            dateSale = null)

    private var address = Address(1, "38 rue de Bellevue", city = "Boulogne-Billancourt", country = "FR", housingReference = reference, zipCode = null)
    private var estateAgent = EstateAgent(name, "Jean", null, null)
    private var photo = Photo("URI", null, reference)
    private var poi = Poi(typePoi, "IC")
    private var housingEstateAgent = HousingEstateAgent(reference, name, null)
    private var housingPoi = HousingPoi(reference, typePoi, 2)

    @get:Rule var rule  = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun initDatabase()
    {
        val context = ApplicationProvider.getApplicationContext<Context>()
        this.database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        housingDAO = database.housingDao()
        addressDAO = database.addressDao()
        estateAgentDAO = database.estateAgentDao()
        photoDAO = database.photoDao()
        poiDAO = database.poiDao()
        housingEstateAgentDAO = database.housingEstateAgentDao()
        housingPoiDAO = database.housingPoiDao()

    }

    @After
    @Throws(IOException::class)
    fun closeDatabase()
    {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun test1_createHousingAndGetIt()
    {
        //ADD variable housing in database
        housingDAO.createHousing(housing)

        //GET
        val housingTest = LiveDataTestUtil.getValue(this.housingDAO.getHousing(reference))
        val listHousing : List<Housing>? = LiveDataTestUtil.getValue(this.housingDAO.getAllHousing())

        //COMPARE
        assertEquals(listHousing?.size,1)
        assertNotNull(housingTest)
        assertEquals(housingTest!!,housing)

        //UPDATE
        housing.area = 200.0
        housingDAO.updateHousing(housing)
        val housingUpdate = LiveDataTestUtil.getValue(this.housingDAO.getHousing(reference))
        assertNotSame(housingUpdate, housingTest)
        assertEquals(housingUpdate, housing)

    }

    @Test
    @Throws(Exception::class)
    fun test2_createAddressAndGetIt()
    {
        //ADD housing to have the link with the FK
        housingDAO.createHousing(housing)
        val listHousing : List<Housing>? = LiveDataTestUtil.getValue(this.housingDAO.getAllHousing())
        assertEquals(listHousing?.size, 1)

        //ADD variable address in  database
        addressDAO.createAddress(address)

        //GET
        val addressFromHousingTest = LiveDataTestUtil.getValue(this.addressDAO.getAddressFromHousing(reference))
        val addressFromIdTest = LiveDataTestUtil.getValue(this.addressDAO.getAddressFromId(address.id))
        val listAddress = LiveDataTestUtil.getValue(this.addressDAO.getAllAddress())

        //COMPARE
        assertEquals(listAddress?.size,1)
        assertNotNull(addressFromHousingTest)
        assertEquals(addressFromHousingTest!!,address)
        assertEquals(addressFromIdTest!!, (address))

        //UPDATE
        address.street = "Rue de Silly"
        addressDAO.updateAddress(address)
        val addressUpdate = LiveDataTestUtil.getValue(this.addressDAO.getAddressFromId(address.id))
        assertNotSame(addressUpdate,addressFromIdTest)
        assertEquals(addressUpdate, address)
    }

    @Test
    @Throws(Exception::class)
    fun test3_createEstateAgentAndGetIt()
    {
        //ADD variable estateAgent in  database
        estateAgentDAO.createEstateAgent(estateAgent)

        //GET
        val estateAgentTest = LiveDataTestUtil.getValue(this.estateAgentDAO.getEstateAgent(name))
        val listEstateAgent = LiveDataTestUtil.getValue(this.estateAgentDAO.getAllEstateAgent())

        //COMPARE
        assertEquals(listEstateAgent?.size,1)
        assertNotNull(estateAgentTest)
        assertEquals(estateAgentTest!!, estateAgent)

        //UPDATE
        estateAgent.email = "EMAIL"
        estateAgentDAO.updateEstateAgent(estateAgent)
        val estateAgentUpdate = LiveDataTestUtil.getValue(this.estateAgentDAO.getEstateAgent(name))
        assertNotSame(estateAgentUpdate,estateAgentTest)
        assertEquals(estateAgentUpdate, estateAgent)
    }

    @Test
    @Throws(Exception::class)
    fun test4_createPhotoAndGetIt()
    {
        //ADD housing to have the link with the FK
        housingDAO.createHousing(housing)
        val listHousing : List<Housing>? = LiveDataTestUtil.getValue(this.housingDAO.getAllHousing())
        assertEquals(listHousing?.size,  1)

        //ADD variable photo in  database
        photoDAO.createPhoto(photo)

        //GET
        val photoTestFromHousing = LiveDataTestUtil.getValue(this.photoDAO.getPhotoFromHousing(reference))
        val photoTestFromUri = LiveDataTestUtil.getValue(this.photoDAO.getPhotoFromUri(photo.uri))
        val listPhoto = LiveDataTestUtil.getValue(this.photoDAO.getAllPhoto())

        //COMPARE
        assertEquals(listPhoto?.size,1)
        assertNotNull(photoTestFromHousing)
        assertEquals(photoTestFromHousing!!, photo)
        assertEquals(photoTestFromUri!!, photo)

        //UPDATE
        photo.description = "DESCRIPTION"
        photoDAO.updatePhoto(photo)
        val photoUpdate = LiveDataTestUtil.getValue(this.photoDAO.getPhotoFromUri(photo.uri))
        assertNotSame(photoUpdate, photoTestFromUri)
        assertEquals(photoUpdate, photo)
    }

    @Test
    @Throws(Exception::class)
    fun test5_createPoiAndGetIt()
    {
        //ADD variable poi in  database
        poiDAO.createPoi(poi)

        //GET
        val poiTest = LiveDataTestUtil.getValue(this.poiDAO.getPoi(typePoi))
        val listPoi = LiveDataTestUtil.getValue(this.poiDAO.getAllPoi())

        //COMPARE
        assertEquals(listPoi?.size, 1)
        assertNotNull(poiTest)
        assertEquals(poiTest!!, poi)

        //UPDATE
        poi.ic = "ICONE"
        poiDAO.updatePoi(poi)
        val poiUpdate = LiveDataTestUtil.getValue(this.poiDAO.getPoi(typePoi))
        assertNotSame(poiUpdate, poiTest)
        assertEquals(poiUpdate, poi)

    }

    @Test
    @Throws(Exception::class)
    fun test6_createHousingEstateAgentAndGetIt()
    {
        //ADD housing and estateAgent to have the link with the FK
        housingDAO.createHousing(housing)
        estateAgentDAO.createEstateAgent(estateAgent)
        val listHousing : List<Housing>? = LiveDataTestUtil.getValue(this.housingDAO.getAllHousing())
        assertEquals(listHousing?.size, 1)
        val listEstateAgent = LiveDataTestUtil.getValue(this.estateAgentDAO.getAllEstateAgent())
        assertEquals(listEstateAgent?.size,  1)

        //ADD variable housingEstateAgent in  database
        housingEstateAgentDAO.createHousingEstateAgent(housingEstateAgent)

        //GET
        val housingEstateAgentTest = LiveDataTestUtil.getValue(this.housingEstateAgentDAO.getHousingEstateAgent(reference, name))
        val housingEstateAgentFromHousingTest = LiveDataTestUtil.getValue(this.housingEstateAgentDAO.getHousingEstateAgentFromHousing(reference))
        val housingEstateAgentFromEstateAgentTest = LiveDataTestUtil.getValue(this.housingEstateAgentDAO.getHousingEstateAgentFromAgent(name))
        val listHousingEstateAgent = LiveDataTestUtil.getValue(this.housingEstateAgentDAO.getAllHousingEstateAgent())

        //COMPARE
        assertEquals(listHousingEstateAgent?.size, 1)
        assertNotNull(housingEstateAgentTest)
        assertEquals(housingEstateAgentTest!!, (housingEstateAgent))
        assertEquals(housingEstateAgentFromHousingTest!!, (housingEstateAgent))
        assertEquals(housingEstateAgentFromEstateAgentTest!!, (housingEstateAgent))

        //UPDATE
        housingEstateAgent.function = "VENDEUR"
        housingEstateAgentDAO.updateHousingEstateAgent(housingEstateAgent)
        val housingEstateAgentUpdate = LiveDataTestUtil.getValue(this.housingEstateAgentDAO.getHousingEstateAgentFromHousing(reference))
        assertNotSame(housingEstateAgentUpdate, housingEstateAgentFromHousingTest)
        assertEquals(housingEstateAgentUpdate, housingEstateAgent)
    }

    @Test
    @Throws(Exception::class)
    fun test7_createHousingPoiAndGetIt()
    {
        //ADD housing and poi to have the link with the FK
        housingDAO.createHousing(housing)
        poiDAO.createPoi(poi)
        val listHousing : List<Housing>? = LiveDataTestUtil.getValue(this.housingDAO.getAllHousing())
        assertEquals(listHousing?.size, 1)
        val listPoi = LiveDataTestUtil.getValue(this.poiDAO.getAllPoi())
        assertEquals(listPoi?.size, 1)

        //ADD variable housingEstateAgent in  database
        housingPoiDAO.createHousingPoi(housingPoi)

        //GET
        val housingPoiTest = LiveDataTestUtil.getValue(this.housingPoiDAO.getHousingPoi(reference, typePoi))
        val housingPoiFromHousingTest = LiveDataTestUtil.getValue(this.housingPoiDAO.getHousingPoiFromHousing(reference))
        val housingPoiFromPoiTest = LiveDataTestUtil.getValue(this.housingPoiDAO.getHousingPoiFromPoi(typePoi))
        val listHousingPoi = LiveDataTestUtil.getValue(this.housingPoiDAO.getAllHousingPoi())

        //COMPARE
        assertEquals(listHousingPoi?.size, 1)
        assertNotNull(housingPoiTest)
        assertEquals(housingPoiTest!!, housingPoi)
        assertEquals(housingPoiFromHousingTest!!, housingPoi)
        assertEquals(housingPoiFromPoiTest!!, housingPoi)

        //UPDATE
        housingPoi.numberOfPoi = 10
        housingPoiDAO.updateHousingPoi(housingPoi)
        val housingPoiUpdate = LiveDataTestUtil.getValue(this.housingPoiDAO.getHousingPoiFromPoi(typePoi))
        assertNotSame(housingPoiUpdate, housingPoiFromPoiTest)
        assertEquals(housingPoiUpdate, housingPoi)
    }


}