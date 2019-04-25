package com.playover.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class InternetBroadcastReceiver extends BroadcastReceiver {

    //receive network status changes
    @Override
    public void onReceive(Context context, Intent intent) {
        if (isNetworkAvailable(context)) {
            return;
        }
    }

    //check if network is available
    public static boolean isNetworkAvailable(Context context) {
        try {
            int[] dataTypes = {ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_WIFI};
            ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int typeNetwork : dataTypes) {
                if (conManager != null) {
                    NetworkInfo networkInfo = conManager.getNetworkInfo(typeNetwork);
                    if (networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("broadcast error: ", ex.getMessage());
            return false;
        }
        return false;
    }
}
