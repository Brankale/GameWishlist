package com.fermimn.gamewishlist.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Connectivity {

    private static Connectivity mInstance;

    public static Connectivity getInstance() {
        if (mInstance == null) {
            mInstance = new Connectivity();
        }
        return mInstance;
    }

    private Connectivity() {
    }

    // TODO: this method should be rewritten to satisfy APIs level 29 security levels
    // TODO: if you rewrite the method, remove also ACCESS_NETWORK_STATE permission in the manifest
    // TODO: it doesn't work
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // NetworkInfo was deprecated in API level 29 due to security reasons
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}
