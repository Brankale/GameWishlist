package com.fermimn.gamewishlist.fragments;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
import com.fermimn.gamewishlist.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
    }

}