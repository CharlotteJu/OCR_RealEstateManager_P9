package com.openclassrooms.realestatemanager.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import static com.openclassrooms.realestatemanager.utils.ConstKt.DOLLAR;


/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */
    /*public static int convertDollarToEuro(int dollars){
        return (int) Math.round(dollars * 0.812);
    }*/

    /**
     * Conversion d'un prix dans le type Double (Dollars vers Euros)
     * @param dollars
     * @return
     */
    public static Double convertDollarToEuroDouble(double dollars)
    {
        return (double) Math.round(dollars * 0.812);
    }

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param euros
     * @return
     */
    /*public static int convertEuroToDollar(int euros){
        return (int) Math.round(euros / 0.812);
    }*/

    /**
     *  Conversion d'un prix dans le type Double (Euros vers Dollars)
     * @param euros
     * @return
     */
    public static Double convertEuroToDollarDouble(double euros)
    {
        return (double) Math.round(euros / 0.812);
    }

    public static String getPriceString (String currency, double price)
    {
        if (currency.equals(DOLLAR))
        {
            return "" + price + "$";
        }
        else
        {
            double euroPrice = convertDollarToEuroDouble(price);
            return "" + euroPrice + "€";
        }
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    /*public static String getTodayDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(new Date());
    }*/

    public static Long getTodayDateGood()
    {
        return Calendar.getInstance().getTime().getTime();
    }

    public static String getDateFormat(Date date)
    {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        return dateFormat.format(date);
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return
     */
    /*public static Boolean isInternetAvailable(Context context)
    {
        WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        return wifi.isWifiEnabled();
    }*/

    public static Boolean isInternetAvailableGood(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnectedOrConnecting();
        } else {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        }
    }

}
