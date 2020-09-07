package com.openclassrooms.realestatemanager.models

import org.junit.Assert.*
import org.junit.Test

class HousingTest
{

    @Test
    fun changeHousingCopy()
    {
        val housingToCompare = Housing("REF", "TYPE", 0.0, 0.0)
        val housingToChange = housingToCompare.copy()

        housingToChange.rooms = 3

        assertNotEquals(housingToCompare.rooms, housingToChange.rooms)
        assertEquals(housingToCompare, housingToChange)
    }

    @Test
    fun nullIsNotEquals()
    {
        val housingToCompare = Housing("REF", "TYPE", 0.0, 0.0)
        val housingNull : Housing? = null

        assertFalse(housingToCompare == housingNull)
    }
}