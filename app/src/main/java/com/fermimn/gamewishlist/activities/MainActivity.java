package com.fermimn.gamewishlist.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.data_types.GamePreviewList;
import com.fermimn.gamewishlist.fragments.GamePreviewListFragment;
import com.fermimn.gamewishlist.fragments.SearchGamesFragment;

public class MainActivity extends AppCompatActivity {

    @SuppressWarnings("unused")
    private static final String TAG = MainActivity.class.getSimpleName();

    private ImageButton mImageButton;
    private SearchGamesFragment mSearchSection;
    private GamePreviewListFragment mWishListSection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set button icon
        mImageButton = findViewById(R.id.button);
        mImageButton.setImageResource(R.drawable.ic_search_black_24dp);

        // add sections
        mSearchSection = new SearchGamesFragment();
        mWishListSection = new GamePreviewListFragment(new GamePreviewList());

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, mWishListSection, "wishlist_section");
        transaction.add(R.id.container, mSearchSection, "search_section");
        transaction.hide(mSearchSection);
        transaction.commit();
    }

    /**
     * Hide wishlist and show search section and viceversa
     * @param view the caller view
     */
    public void sectionSwitch(View view) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // if search section is hidden
        if (mSearchSection.isHidden()) {
            // show search section
            transaction.hide(mWishListSection);
            transaction.show(mSearchSection);
            mImageButton.setImageResource(R.drawable.ic_videogame_asset_black_24dp);
        } else {
            // show wishlist section
            // TODO: interrupt search
            transaction.show(mWishListSection);
            transaction.hide(mSearchSection);
            mImageButton.setImageResource(R.drawable.ic_search_black_24dp);
        }

        transaction.commit();
    }

}
