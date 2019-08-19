package com.fermimn.gamewishlist;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.fermimn.gamewishlist.utils.SettingsManager;

class GameWishlistApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // set light or dark theme
        SettingsManager settings = SettingsManager.getInstance(this);
        AppCompatDelegate.setDefaultNightMode( settings.getDarkMode() );
    }

}
