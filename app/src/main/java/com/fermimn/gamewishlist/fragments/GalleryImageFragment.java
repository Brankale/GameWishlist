package com.fermimn.gamewishlist.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.fermimn.gamewishlist.R;
import com.squareup.picasso.Picasso;

public class GalleryImageFragment extends Fragment {

    @SuppressWarnings("unused")
    private static final String TAG = GalleryImageFragment.class.getSimpleName();

    private Uri mImage;

    public GalleryImageFragment(Uri image) {
        mImage = image;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gallery_image, container, false);
        ImageView imageView = view.findViewById(R.id.gallery_image);
        Picasso.get().load(mImage).into(imageView);

        return view;
    }


}
