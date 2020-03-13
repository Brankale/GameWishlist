package com.fermimn.gamewishlist.adapters;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.fermimn.gamewishlist.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GalleryAdapter extends PagerAdapter {

    @SuppressWarnings("unused")
    private static final String TAG = GalleryAdapter.class.getSimpleName();

    private final Activity mActivity;
    private final List<String> mGallery;

    public GalleryAdapter(Activity activity, List<String> gallery) {
        mActivity = activity;
        mGallery = gallery;
    }

    @Override
    public @NonNull Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = mActivity.getLayoutInflater();
        View view = inflater.inflate(R.layout.partial_gallery_image, container, false);
        ImageView imageView = view.findViewById(R.id.gallery_image);
        Picasso.get().load( mGallery.get(position) ).into(imageView);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return mGallery.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

}
