package com.fermimn.gamewishlist.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.fragments.SearchGamesFragment;

public class MainActivity extends AppCompatActivity {

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
