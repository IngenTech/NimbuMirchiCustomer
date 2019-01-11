package com.iembsys.admin.nimbumirchi.customer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.iembsys.admin.nimbumirchi.customer.util.MyUtility;

/**
 * Created by WRMS on 23-05-2016.
 */

public class NetworkUpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        boolean isConnected = isOnline(context);
        if (isConnected){
            Log.i("NET", "connected " + isConnected);
            if (MyUtility.isOnline(context)) {
                MyUtility.getInstance().generateGCM_ID(context);
            }

        }else{
            Log.i("NET", "not connected " +isConnected);
        }
    }

    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in air plan mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }

}