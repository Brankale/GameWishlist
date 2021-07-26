package com.fermimn.gamewishlist.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.databinding.ActivitySettingsBinding;
import com.fermimn.gamewishlist.fragments.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.actionBar;
        toolbar.setTitle( getString(R.string.section_settings) );
        setSupportActionBar(toolbar);

        getSupportFragmentManager()
                .beginTransaction()
                .add(binding.container.getId(), new SettingsFragment())
                .commit();
    }

}
