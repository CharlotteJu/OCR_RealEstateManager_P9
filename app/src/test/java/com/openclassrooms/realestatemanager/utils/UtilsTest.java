package com.openclassrooms.realestatemanager.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.test.mock.MockContext;


import org.junit.Test;


import java.util.Calendar;
import java.util.Date;



import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class UtilsTest
{
    @Test
    public void convertDollarToEuroDouble_Success()
    {
        assertEquals(0.00, Utils.convertDollarToEuroDouble(0), 0.01);
        assertEquals(81, Utils.convertDollarToEuroDouble(100), 0.01);
        assertEquals(-81, Utils.convertDollarToEuroDouble(-100), 0.01);
    }
    @Test
    public void convertEuroToDollarDouble_Success()
    {
        assertEquals(0, Utils.convertEuroToDollarDouble(0), 0.01);
        assertEquals(100, Utils.convertEuroToDollarDouble(81), 0.01);
        assertEquals(-100, Utils.convertEuroToDollarDouble(-81), 0.01);
    }
    @Test
    public void getPriceString_Success()
    {
        String dollar = "$";
        String euro = "€";

        assertEquals("0.0$", Utils.getPriceString(dollar, 0));
        assertEquals("0.0€", Utils.getPriceString(euro, 0));

    }
    @Test
    public void getDateFormat_Success()
    {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        assertEquals("25/08/2020", Utils.getDateFormat(date)); //Think to change with the date of today
    }
    @Test
    public void getTodayDateGood_Success()
    {
        Calendar calendar = Calendar.getInstance();

        String day;
        String month;

        if (calendar.get(Calendar.DAY_OF_MONTH) < 10) day = "0" + calendar.get(Calendar.DAY_OF_MONTH);
        else day = "" + calendar.get(Calendar.DAY_OF_MONTH);

        if (calendar.get(Calendar.MONTH)+1 < 10) month = "0" + (calendar.get(Calendar.MONTH)+1);
        else month = "" + (calendar.get(Calendar.MONTH)+1);

        assertEquals(""+ day + "/" + month + "/" + calendar.get(Calendar.YEAR), Utils.getTodayDateGood());
    }


   @Test
    public void isInternetAvailableGood_Success()
    {
        NetworkInfo networkInfo = mock(NetworkInfo.class);
        when(networkInfo.isConnected()).thenReturn(true);
        when(networkInfo.isConnectedOrConnecting()).thenReturn(true);

        NetworkCapabilities capabilities = mock(NetworkCapabilities.class);
        when(capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)).thenReturn(true);
        ConnectivityManager connectivityManager = mock(ConnectivityManager.class);
        when(connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork())).thenReturn(capabilities);

        Context context = mock(MockContext.class);
        when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);

        assertTrue(Utils.isInternetAvailableGood(context));
    }

}