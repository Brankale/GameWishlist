package com.fermimn.gamewishlist.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.fermimn.gamewishlist.R;
import com.squareup.picasso.Picasso;

/**
 * Created by reale on 13/07/2016.
 */
public class ViewPagerAdapter extends PagerAdapter {
    Activity activity;
    String[] images;
    LayoutInflater inflater;

    public ViewPagerAdapter(Activity activity, String[] images) {
        this.activity = activity;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        inflater = (LayoutInflater)activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View itemView = inflater.inflate(R.layout.gallery_item,container,false);
//
//        ImageView image;
//        image = (ImageView)itemView.findViewById(R.id.imageView);
//        DisplayMetrics dis = new DisplayMetrics();
//        activity.getWindowManager().getDefaultDisplay().getMetrics(dis);
//        int height = dis.heightPixels;
//        int width = dis.widthPixels;
//        image.setMinimumHeight(height);
//        image.setMinimumWidth(width);
//
//        try{
//            Picasso.get()
//                    .load(images[position])
//                    .into(image);
//        }
//        catch (Exception ex){
//
//        }
//
//        container.addView(itemView);
//        return itemView;
        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View)object);
    }
}

