package com.fermimn.gamewishlist.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.utils.SettingsManager;

public class SettingsActivity extends AppCompatActivity {

    private SettingsManager mSettings;
    private boolean mChanges = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mSettings = SettingsManager.getInstance(this);

        Switch darkMode = findViewById(R.id.dark_mode);

        if (mSettings.getDarkMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            darkMode.setChecked(true);
        } else {
            darkMode.setChecked(false);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mChanges) {
            mSettings.commit();
            Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show();
        }
    }

    public void changeSettings(View view) {

        // user made changes
        mChanges = true;

        switch (view.getId()) {

            case R.id.dark_mode:
                Switch darkMode = (Switch) view;
                if (darkMode.isChecked()) {
                    mSettings.setDarkMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    mSettings.setDarkMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                break;

        }
    }

}
