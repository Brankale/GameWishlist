package com.fermimn.gamewishlist.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.adapters.GalleryAdapter;
import com.fermimn.gamewishlist.databinding.ActivityGalleryBinding;

import java.util.ArrayList;

public class GalleryActivity extends FragmentActivity {

    @SuppressWarnings("unused")
    private static final String TAG = GalleryActivity.class.getSimpleName();

    private ActivityGalleryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGalleryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // get Intent content
        Intent caller = getIntent();
        int position = caller.getIntExtra("position", 0);
        ArrayList<String> images = caller.getStringArrayListExtra("images");

        // Instantiate a ViewPager and a PagerAdapter
        PagerAdapter pagerAdapter = new GalleryAdapter(this, images);
        binding.viewPager.setAdapter(pagerAdapter);

        if (savedInstanceState == null) {
            binding.viewPager.setCurrentItem(position);
        } else {
            binding.viewPager.setCurrentItem( savedInstanceState.getInt("position") );
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("position", binding.viewPager.getCurrentItem());
        super.onSaveInstanceState(outState);
    }

}
