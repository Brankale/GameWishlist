package com.fermimn.gamewishlist.activities;

import android.os.Bundle;
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

    private SettingsManager mSettings;
    private boolean mChanges = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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
                if (mChanges) {
                    mSettings.commit();
                    Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show();
                }
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
