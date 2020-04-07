package com.fermimn.gamewishlist.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.custom_views.PriceView;
import com.fermimn.gamewishlist.models.Game;
import com.fermimn.gamewishlist.models.GamePreview;
import com.fermimn.gamewishlist.models.GamePreviews;
import com.fermimn.gamewishlist.models.Price;
import com.fermimn.gamewishlist.models.Promo;
import com.fermimn.gamewishlist.gamestop.GameStop;
import com.fermimn.gamewishlist.utils.Util;
import com.fermimn.gamewishlist.viewmodels.WishlistViewModel;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class GamePageActivity extends AppCompatActivity {

    @SuppressWarnings("unused")
    private static final String TAG = GamePageActivity.class.getSimpleName();

    private Game mGame;
    private WishlistViewModel mWishListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);

        // get game ID
        int gameId = getIntent().getIntExtra("gameID", 0);

        mWishListViewModel = new ViewModelProvider(this).get(WishlistViewModel.class);

        mWishListViewModel.isUpdating().observe(this, isUpdating -> {
            GamePreview gamePreview = isUpdating.first;
            boolean isDownloading = isUpdating.second;

            if (gamePreview == null) {
                return;
            }

            if (isDownloading) {
                Toast.makeText(this, "Downloading: " + gamePreview.getTitle(), Toast.LENGTH_SHORT).show();
            } else {
                // TODO: if the activity is closed the user can't see the message
                Toast.makeText(this, "Added: " + gamePreview.getTitle(), Toast.LENGTH_SHORT).show();
                invalidateOptionsMenu();
            }
        });

        // search the game in the wishlist
        mGame = mWishListViewModel.getGame(gameId);

        // if the game is not in the wishlist
        if (mGame == null) {
            // download the game
            DownloadGameAndUpdateUI task = new DownloadGameAndUpdateUI(this);
            task.execute(gameId);
        } else {
            // set the UI
            updateUI(mGame);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_game_page, menu);

        MenuItem actionAdd = menu.findItem(R.id.action_add_game).setVisible(false);
        MenuItem actionRemove = menu.findItem(R.id.action_remove_game).setVisible(false);

        // show add button if the game is not in the wishlist
        // show remove button if the game is already in the wishlist
        if (mGame != null) {
            setTitle( mGame.getTitle() );
            GamePreviews wishlist = mWishListViewModel.getWishlist().getValue();
            boolean result = false;
            if (wishlist != null) {
                result = wishlist.contains(mGame);
            }
            if (result) {
                actionRemove.setVisible(true);
            } else {
                actionAdd.setVisible(true);
            }
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_add_game:
                new AlertDialog.Builder(this)
                        .setTitle( getString(R.string.dialog_add_game_to_wishlist_title) )
                        .setMessage( getString(R.string.dialog_add_game_to_wishlist_text) )

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> mWishListViewModel.addGame(mGame))

                        // A null listener allows the button to dismiss the dialog and take no further action
                        .setNegativeButton(android.R.string.no, null)
                        .show();
                return true;

            case R.id.action_remove_game:
                new AlertDialog.Builder(this)
                        .setTitle( getString(R.string.dialog_remove_game_from_wishlist_title) )
                        .setMessage( getString(R.string.dialog_remove_game_from_wishlist_text) )

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            mWishListViewModel.removeGame(mGame.getId());
                            finish();
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action
                        .setNegativeButton(android.R.string.no, null)
                        .show();
                return true;

            case R.id.action_open_website:
                Intent browserIntent =
                        new Intent(Intent.ACTION_VIEW, Uri.parse( GameStop.Companion.getGamePageUrl( mGame.getId() ) ));
                startActivity(browserIntent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    private static class DownloadGameAndUpdateUI extends AsyncTask<Integer, Integer, Game> {

        private final WeakReference<GamePageActivity> mGamePageActivity;

        private DownloadGameAndUpdateUI(GamePageActivity gamePageActivity) {
            mGamePageActivity = new WeakReference<>(gamePageActivity);
        }

        @Override
         protected Game doInBackground(Integer... integers) {
            return GameStop.Companion.getGameById(integers[0]);
        }

        @Override
        protected void onPostExecute(Game game) {
            GamePageActivity activity = mGamePageActivity.get();
            if (activity != null) {
                activity.mGame = game;
                activity.updateUI(game);
            }
        }

    }

    // TODO: add documentation
    private void updateUI(Game game) {

        // set action bar
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);

        // init game page UI
        setGameImages(game);
        setGameData(game);
        initSectionPrices(game);
        setGamePromo(game);
        initSectionPegi(game);

        // make game page UI visible
        LinearLayout linearLayout = findViewById(R.id.game_page_container);
        linearLayout.setVisibility(View.VISIBLE);
    }

    // TODO: add documentation
    private void setGameData(Game game) {

        // Get views
        TextView title = findViewById(R.id.title);
        TextView publisher = findViewById(R.id.publisher);
        TextView platform = findViewById(R.id.platform);
        TextView genres = findViewById(R.id.genres);
        TextView releaseDate = findViewById(R.id.releaseDate);
        TextView players = findViewById(R.id.players);
        TextView officialSite = findViewById(R.id.officialSite);
        TextView description = findViewById(R.id.description);
        LinearLayout validForPromotions = findViewById(R.id.valid_for_promotions);
        TextView validForPromotionsText = findViewById(R.id.valid_for_promotions_text);

        FrameLayout genresContainer = findViewById(R.id.genres_container);
        FrameLayout releaseDateContainer = findViewById(R.id.release_date_container);
        FrameLayout playersContainer = findViewById(R.id.players_container);
        FrameLayout officialSiteContainer = findViewById(R.id.official_site_container);

        // Set data
        title.setText( game.getTitle() );
        publisher.setText( game.getPublisher() );
        platform.setText( game.getPlatform() );

        if (game.getGenres() != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String genre : game.getGenres()) {
                stringBuilder.append(genre).append("/");
            }
            stringBuilder.deleteCharAt( stringBuilder.length() - 1 );
            genres.setText(stringBuilder);
        } else {
            genresContainer.setVisibility(View.GONE);
        }

        if (game.getReleaseDate() != null) {
            releaseDate.setText( game.getReleaseDate() );
        } else {
            releaseDateContainer.setVisibility(View.GONE);
        }

        if (game.getPlayers() != null) {
            playersContainer.setVisibility(View.VISIBLE);
            players.setText( game.getPlayers() );
        }

        if (game.getWebsite() != null) {
            String href = game.getWebsite();
            String domain = href.split("/")[2];
            Spanned link = Html.fromHtml("<a href='" + href + "'>" + domain + "</a>");

            officialSite.setMovementMethod( LinkMovementMethod.getInstance() );
            officialSite.setText(link);
            officialSiteContainer.setVisibility(View.VISIBLE);
        }

        if (game.getValidForPromo()) {
            validForPromotions.setBackgroundColor(getResources().getColor(R.color.green));
            validForPromotionsText.setText(R.string.valid_for_promotions_true);
            validForPromotionsText.setTextColor(getResources().getColor(R.color.always_white));
        }

        // TODO: links in the description don't do anything
        if (game.getDescription() != null) {
            String html = game.getDescription();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                description.setMovementMethod( LinkMovementMethod.getInstance() );
                description.setText( Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY) );
            } else {
                description.setMovementMethod( LinkMovementMethod.getInstance() );
                description.setText( Html.fromHtml(html) );
            }
        }
    }

    // TODO: add documentation
    private void setGameImages(Game game) {

        // Get views
        ImageView cover = findViewById(R.id.cover);
        LinearLayout gallery = findViewById(R.id.gallery);
        LinearLayout galleryContainer = findViewById(R.id.gallery_container);

        // set listener to the cover
        Bundle coverBundle = new Bundle();
        coverBundle.putString("cover", game.getCover());
        cover.setTag(coverBundle);

        cover.setOnClickListener(this::openGallery);

        // load cover in the view
        Picasso.get().load(game.getCover()).into(cover);

        // Add images to gallery
        if (game.getGallery() != null && !game.getGallery().isEmpty()) {

            galleryContainer.setVisibility(View.VISIBLE);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);

            ArrayList<String> images = game.getGallery();

            // insert all the images in the gallery
            for (int i = 0; i < images.size(); ++i) {

                // create view
                ImageView imageView = new ImageView(this);

                // init view
                imageView.setAdjustViewBounds(true);
                imageView.setLayoutParams(layoutParams);

                // last image in the gallery doesn't have margin
                if (i != images.size()-1) {
                    imageView.setPadding(0, 0, (int) Util.convertDpToPx(this, 8), 0);
                }

                // set tag
                Bundle galleryBundle = new Bundle();
                galleryBundle.putStringArrayList("gallery", images);
                galleryBundle.putInt("position", i);
                imageView.setTag(galleryBundle);

                // set listener
                imageView.setOnClickListener(this::openGallery);

                // load image in the view
                Picasso.get().load( images.get(i) ).into(imageView);

                // add view to gallery
                gallery.addView(imageView);
            }
        }
    }

    private void initSectionPrices(GamePreview game) {

        ViewGroup parent = findViewById(R.id.section_prices);

        if (game.getNewPrice() != null) {
            PriceView priceView = createPriceView(
                    Price.NEW,
                    game.getNewPrice(),
                    game.getOldNewPrices(),
                    game.getNewAvailable()
            );
            parent.addView(priceView);
        }

        if (game.getUsedPrice() != null) {
            PriceView priceView = createPriceView(
                    Price.USED,
                    game.getUsedPrice(),
                    game.getOldUsedPrices(),
                    game.getUsedAvailable()
            );
            parent.addView(priceView);
        }

        if (game.getDigitalPrice() != null) {
            PriceView priceView = createPriceView(
                    Price.DIGITAL,
                    game.getDigitalPrice(),
                    game.getOldDigitalPrices(),
                    game.getDigitalAvailable()
            );
            parent.addView(priceView);
        }

        if (game.getPreorderPrice() != null) {
            PriceView priceView = createPriceView(
                    Price.PREORDER,
                    game.getPreorderPrice(),
                    game.getOldPreorderPrices(),
                    game.getPreorderAvailable()
            );
            parent.addView(priceView);
        }
    }

    @NonNull
    private PriceView createPriceView(
            int category,
            @Nullable Float price,
            @Nullable List<Float> oldPrices,
            boolean available
    ) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, (int) Util.convertDpToPx(this, 10), 0, 0);

        PriceView priceView = new PriceView(this, null);
        priceView.setLayoutParams(layoutParams);
        priceView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        priceView.bind(category, price, oldPrices, available);
        return priceView;
    }

    // TODO: add documentation
    private void setGamePromo(Game game) {

        LayoutInflater inflater = getLayoutInflater();

        // Get Views
        LinearLayout promoContainer = findViewById(R.id.promo_container);

        if (game.getPromos() != null) {
            for (Promo promo : game.getPromos()) {

                // Create a promo view
                View promoView = inflater.inflate(R.layout.partial_section_promo,
                        promoContainer, false);

                // init promo view
                TextView promoHeader = promoView.findViewById(R.id.promo_header);
                TextView promoValidity = promoView.findViewById(R.id.promo_validity);
                TextView promoMessage = promoView.findViewById(R.id.promo_message);
                LinearLayout promoMessageLayout = promoView.findViewById(R.id.promo_message_layout);

                promoHeader.setText( promo.getHeader() );
                promoValidity.setText( promo.getText() );

                if (promo.hasFindMore()) {
                    String href = promo.getFindMoreUrl();
                    String message = promo.getFindMore();
                    Spanned link = Html.fromHtml("<a href='" + href + "'>" + message + "</a>");

                    promoMessage.setMovementMethod( LinkMovementMethod.getInstance() );
                    promoMessage.setText(link);
                    promoMessageLayout.setVisibility(View.VISIBLE);
                } else {
                    promoMessageLayout.setVisibility(View.GONE);
                }

                // add promo to promoContainer
                promoContainer.addView(promoView);
            }

            promoContainer.setVisibility(View.VISIBLE);
        }
    }

    private void initSectionPegi(@NonNull Game game) {

        ViewGroup section = findViewById(R.id.section_pegi);
        List<String> pegi = game.getPegi();

        if (pegi != null) {
            for (String type : pegi) {
                switch (type) {
                    case "pegi3":
                        section.addView(getPegiImageView(R.drawable.ic_pegi_pegi3, R.string.pegi_pegi3));
                        break;
                    case "pegi7":
                        section.addView(getPegiImageView(R.drawable.ic_pegi_pegi7, R.string.pegi_pegi7));
                        break;
                    case "pegi12":
                        section.addView(getPegiImageView(R.drawable.ic_pegi_pegi12, R.string.pegi_pegi12));
                        break;
                    case "pegi16":
                        section.addView(getPegiImageView(R.drawable.ic_pegi_pegi16, R.string.pegi_pegi16));
                        break;
                    case "pegi18":
                        section.addView(getPegiImageView(R.drawable.ic_pegi_pegi18, R.string.pegi_pegi18));
                        break;
                    case "bad-language":
                        section.addView(getPegiImageView(R.drawable.ic_pegi_bad_language, R.string.pegi_bad_language));
                        break;
                    case "violence":
                        section.addView(getPegiImageView(R.drawable.ic_pegi_violence, R.string.pegi_violence));
                        break;
                    case "online":
                        section.addView(getPegiImageView(R.drawable.ic_pegi_online, R.string.pegi_online));
                        break;
                    case "sex":
                        section.addView(getPegiImageView(R.drawable.ic_pegi_sex, R.string.pegi_sex));
                        break;
                    case "fear":
                        section.addView(getPegiImageView(R.drawable.ic_pegi_fear, R.string.pegi_fear));
                        break;
                    case "drugs":
                        section.addView(getPegiImageView(R.drawable.ic_pegi_drugs, R.string.pegi_drugs));
                        break;
                    case "discrimination":
                        section.addView(getPegiImageView(R.drawable.ic_pegi_discrimination, R.string.pegi_discrimination));
                        break;
                    case "gambling":
                        section.addView( getPegiImageView(R.drawable.ic_pegi_gambling, R.string.pegi_gambling));
                        break;
                }
            }
        }
    }

    @NonNull
    private ImageView getPegiImageView(int drawableRes, int descriptionRes) {

        ImageView imageView = new ImageView(this);
        String description = getResources().getString(descriptionRes);
        imageView.setContentDescription(description);
        imageView.setAdjustViewBounds(true);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 0, (int) Util.convertDpToPx(this, 5), 0);
        imageView.setLayoutParams(layoutParams);

        Picasso.get().load(drawableRes).into(imageView);

        return imageView;
    }

    // TODO: add documentation
    private void openGallery(View view) {

        Bundle bundle = (Bundle) view.getTag();

        int position = bundle.getInt("position");
        String cover = bundle.getString("cover");
        ArrayList<String> gallery = bundle.getStringArrayList("gallery");

        // create and start intent
        Intent intent = new Intent(this, GalleryActivity.class);

        if (cover == null) {
            intent.putStringArrayListExtra("images", gallery);
            intent.putExtra("position", position);
        } else {
            ArrayList<String> coverArray = new ArrayList<>();
            coverArray.add(cover);
            intent.putStringArrayListExtra("images", coverArray);
            intent.putExtra("position", 0);
        }

        startActivity(intent);
    }

}
