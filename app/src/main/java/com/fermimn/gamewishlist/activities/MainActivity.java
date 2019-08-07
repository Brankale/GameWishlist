package com.fermimn.gamewishlist.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.data_types.GamePreviewList;
import com.fermimn.gamewishlist.fragments.GamePageFragment;
import com.fermimn.gamewishlist.fragments.GamePreviewListFragment;
import com.fermimn.gamewishlist.fragments.SearchGamesFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ImageButton mImageButton;
    private SearchGamesFragment mSearchSection;
    private GamePreviewListFragment mWishlistSection;
    private GamePageFragment mGamePageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {

            // set button icon
            mImageButton = findViewById(R.id.button);
            mImageButton.setImageResource(R.drawable.ic_search_black_24dp);

            // add sections
            mSearchSection = new SearchGamesFragment();
            mWishlistSection = new GamePreviewListFragment(new GamePreviewList());

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.container, mWishlistSection, "wishlist_section");
            transaction.add(R.id.container, mSearchSection, "search_section");
            transaction.hide(mSearchSection);
            transaction.commit();
        }
    }

    /**
     * Hide wishlist and show search section and viceversa
     * @param view
     * @return
     */
    public void sectionSwitch(View view) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // if search section is hidden
        if (mSearchSection.isHidden()) {
            // show search section
            transaction.hide(mWishlistSection);
            transaction.show(mSearchSection);
            mImageButton.setImageResource(R.drawable.ic_home_black_24dp);
        } else {
            // show wishlist section
            // TODO: interrupt search
            transaction.show(mWishlistSection);
            transaction.hide(mSearchSection);
            mImageButton.setImageResource(R.drawable.ic_search_black_24dp);
        }

        transaction.commit();
    }

    public void showGamePageFragment(GamePageFragment gamePageFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, gamePageFragment, "gamePage")
                .addToBackStack(null)
                .commit();
    }

}
