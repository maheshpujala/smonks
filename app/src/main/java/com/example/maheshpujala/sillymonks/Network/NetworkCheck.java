package com.example.maheshpujala.sillymonks.Network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by maheshpujala on 23/6/16.
 */
public class NetworkCheck {

    private static final String TAG = NetworkCheck.class.getSimpleName();



    public static boolean isInternetAvailable(Context context)
    {
        NetworkInfo info =  ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null)
        {

            Log.d(TAG,"no internet connection");
            return false;
        }
        else
        {
            if(info.isConnected())
            {
                Log.d(TAG," internet connection available...");
                return true;
            }
            else
            {
                Log.d(TAG," internet connection");
                return true;
            }

        }
    }
}