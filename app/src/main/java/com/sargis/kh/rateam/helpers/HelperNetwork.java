package com.sargis.kh.rateam.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.sargis.kh.rateam.App;

public class HelperNetwork {

    public static boolean isNetworkActive() {
        ConnectivityManager connMgr = (ConnectivityManager) App.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}
