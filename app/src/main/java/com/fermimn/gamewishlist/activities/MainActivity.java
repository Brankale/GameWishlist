package com.fermimn.gamewishlist.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.fragments.SearchBox;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: this is just a test, you must remove it from here
        // add the searchbox
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new SearchBox( getApplicationContext() )).commit();
    }

}
