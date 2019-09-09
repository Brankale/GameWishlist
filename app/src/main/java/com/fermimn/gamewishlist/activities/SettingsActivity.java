package com.fermimn.gamewishlist.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.utils.SettingsManager;

public class SettingsActivity extends AppCompatActivity {

    @SuppressWarnings("unused")
    private static final String TAG = SettingsActivity.class.getSimpleName();

    private SettingsManager mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Log.d(TAG, "Activity Settings started");

        // set action bar
        Toolbar toolbar = findViewById(R.id.action_bar);
        toolbar.setTitle( getString(R.string.section_settings) );
        setSupportActionBar(toolbar);

        mSettings = SettingsManager.getInstance(this);

        Switch darkMode = findViewById(R.id.dark_mode);

        if (mSettings.getDarkMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            darkMode.setChecked(true);
        } else {
            darkMode.setChecked(false);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_apply_changes:
                showDialog();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showDialog();
    }

    private void showDialog() {
        if (mSettings.commit()) {
            Toast.makeText(this, getString(R.string.toast_settings_saved),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.toast_settings_not_saved),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    public void changeSettings(View view) {

        switch (view.getId()) {
            case R.id.dark_mode:
                Switch darkMode = (Switch) view;
                if (darkMode.isChecked()) {
                    mSettings.setDarkMode(AppCompatDelegate.MODE_NIGHT_YES);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    mSettings.setDarkMode(AppCompatDelegate.MODE_NIGHT_NO);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                break;
        }
    }

}
