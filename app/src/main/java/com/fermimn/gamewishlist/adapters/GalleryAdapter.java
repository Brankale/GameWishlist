package com.fermimn.gamewishlist.adapters;

import android.net.Uri;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.fermimn.gamewishlist.fragments.GalleryImageFragment;

import java.util.List;

public class GalleryAdapter extends FragmentStatePagerAdapter {

    List<Uri> mImages;

    public GalleryAdapter(FragmentManager fm, List<Uri> images) {
        super(fm);
        mImages = images;
    }

    @Override
    public Fragment getItem(int position) {
        return new GalleryImageFragment(mImages.get(position));
    }

    @Override
    public int getCount() {
        return mImages.size();
    }
}
