package com.fermimn.gamewishlist.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.fragments.SearchGamesFragment;
import com.fermimn.gamewishlist.utils.Connectivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // add the search bar
        if (savedInstanceState == null) {
            SearchGamesFragment searchBar = new SearchGamesFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.container, searchBar, "search_bar");
            transaction.commit();
        }
    }

}
