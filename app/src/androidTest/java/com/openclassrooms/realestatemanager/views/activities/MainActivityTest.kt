package com.openclassrooms.realestatemanager.views.activities

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.openclassrooms.realestatemanager.R
import org.hamcrest.Matchers

@RunWith(AndroidJUnit4::class)
class MainActivityTest
{
    @get: Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun toolbar_click_DisplayFragmentFilter()
    {
        onView(withContentDescription("Plus d'options")).perform(click())
        onView(withText(R.string.filter)).perform(click())
        onView(withId(R.id.fragment_filter_search_fab)).check(matches(isDisplayed()))
    }

    @Test
    fun homeButton_click_DisplayMenuDrawer()
    {
        onView(withContentDescription("Ouvrir le panneau de navigation")).perform(click())
        onView(withText(R.string.manage_estate_agent)).check(matches(isDisplayed()))

    }

    @Test
    fun menuDrawer_clickSettings_DisplayFragmentSettings()
    {
        onView(withContentDescription("Ouvrir le panneau de navigation")).perform(click())
        onView(withText(R.string.settings)).perform(click())
        onView(withText(R.string.notifications)).check(matches(isDisplayed()))

    }

    @Test
    fun menuDrawer_clickHome_DisplayFragmentList()
    {
        //Display Settings Fragment
        onView(withContentDescription("Ouvrir le panneau de navigation")).perform(click())
        onView(withText(R.string.settings)).perform(click())
        onView(withText(R.string.notifications)).check(matches(isDisplayed()))

        //Display List Fragment
        onView(withContentDescription("Ouvrir le panneau de navigation")).perform(click())
        onView(withText(R.string.home)).perform(click())
        onView(withId(R.id.list_fragment_map_fab)).check(matches(isDisplayed()))
    }

    @Test
    fun menuDrawer_clickAddHousing_DisplayFragmentAddHousing()
    {
        onView(withContentDescription("Ouvrir le panneau de navigation")).perform(click())
        onView(withText(R.string.add_housing)).perform(click())
        onView(withId(R.id.add_housing_fragment_type_spinner)).check(matches(isDisplayed()))
    }

    @Test
    fun menuDrawer_clickAddEstateAgent_DisplayFragmentAddEstateAgent()
    {
        onView(withContentDescription("Ouvrir le panneau de navigation")).perform(click())
        onView(withText(R.string.manage_estate_agent)).perform(click())
        onView(withId(R.id.add_estate_agent_add_fab)).check(matches(isDisplayed()))
    }



}