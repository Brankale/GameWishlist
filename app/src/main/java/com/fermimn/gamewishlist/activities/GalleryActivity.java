package com.fermimn.gamewishlist.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.adapters.GalleryAdapter;

import java.util.ArrayList;

public class GalleryActivity extends FragmentActivity {

    @SuppressWarnings("unused")
    private static final String TAG = GalleryActivity.class.getSimpleName();

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        // get Intent content
        Intent caller = getIntent();
        int position = caller.getIntExtra("position", 0);
        ArrayList<String> images = caller.getStringArrayListExtra("images");

        // Instantiate a ViewPager and a PagerAdapter
        mViewPager = findViewById(R.id.view_pager);
        PagerAdapter pagerAdapter = new GalleryAdapter(this, images);
        mViewPager.setAdapter(pagerAdapter);

        if (savedInstanceState == null) {
            mViewPager.setCurrentItem(position);
        } else {
            mViewPager.setCurrentItem( savedInstanceState.getInt("position") );
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("position", mViewPager.getCurrentItem());
        super.onSaveInstanceState(outState);
    }

}
