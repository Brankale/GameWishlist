package com.fermimn.gamewishlist.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.data_types.Game;
import com.fermimn.gamewishlist.data_types.GamePreview;
import com.fermimn.gamewishlist.data_types.Promo;
import com.fermimn.gamewishlist.utils.Gamestop;
import com.fermimn.gamewishlist.utils.Util;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class GamePageActivity extends AppCompatActivity {

    @SuppressWarnings("unused")
    private static final String TAG = GamePageActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);

        String id = getIntent().getStringExtra("gameID");
        DownloadGame task = new DownloadGame(this);
        task.execute(id);
    }

    // TODO: add documentation
    private static class DownloadGame extends AsyncTask<String, Integer, Game> {

        private final WeakReference<GamePageActivity> mGamePageActivity;

        private DownloadGame(GamePageActivity gamePageActivity) {
            mGamePageActivity = new WeakReference<>(gamePageActivity);
        }

        @Override
        protected Game doInBackground(String... strings) {
            String id = strings[0];
            Gamestop gamestop = new Gamestop();
            return gamestop.downloadGame(id);
        }

        @Override
        protected void onPostExecute(Game game) {

            // init game page UI
            setGameImages(game);
            setGameData(game);
            setGameRating(game);
            setGamePrices(game);
            setGamePromo(game);

            // make game page UI visible
            GamePageActivity activity = mGamePageActivity.get();
            LinearLayout linearLayout = activity.findViewById(R.id.game_page_container);
            linearLayout.setVisibility(View.VISIBLE);
        }

        // TODO: add documentation
        private void setGameData(Game game) {

            GamePageActivity activity = mGamePageActivity.get();

            // Get views
            TextView title = activity.findViewById(R.id.title);
            TextView publisher = activity.findViewById(R.id.publisher);
            TextView platform = activity.findViewById(R.id.platform);
            TextView genres = activity.findViewById(R.id.genres);
            TextView releaseDate = activity.findViewById(R.id.releaseDate);
            TextView players = activity.findViewById(R.id.players);
            TextView officialSite = activity.findViewById(R.id.officialSite);
            TextView description = activity.findViewById(R.id.description);
            LinearLayout validForPromotions = activity.findViewById(R.id.valid_for_promotions);

            LinearLayout genresContainer = activity.findViewById(R.id.genres_container);
            LinearLayout releaseDateContainer = activity.findViewById(R.id.release_date_container);
            LinearLayout playersContainer = activity.findViewById(R.id.players_container);
            LinearLayout officialSiteContainer = activity.findViewById(R.id.official_site_container);

            // Set data
            title.setText( game.getTitle() );
            publisher.setText( game.getPublisher() );
            platform.setText( game.getPlatform() );

            if (game.hasGenres()) {
                StringBuilder stringBuilder = new StringBuilder();
                for (String genre : game.getGenres()) {
                    stringBuilder.append(genre).append("/");
                }
                stringBuilder.deleteCharAt( stringBuilder.length() - 1 );
                genres.setText(stringBuilder);
            } else {
                genresContainer.setVisibility(View.GONE);
            }

            if (game.hasReleaseDate()) {
                releaseDate.setText( game.getReleaseDate() );
            } else {
                releaseDateContainer.setVisibility(View.GONE);
            }

            if (game.hasPlayers()) {
                playersContainer.setVisibility(View.VISIBLE);
                players.setText( game.getPlayers() );
            }

            if (game.hasOfficialSite()) {
                officialSiteContainer.setVisibility(View.VISIBLE);
                officialSite.setText( game.getOfficialSite() );
            }

            if (game.isValidForPromotions()) {
                validForPromotions.setVisibility(View.VISIBLE);
            }

            // TODO: links in the description don't do anything
            if (game.hasDescription()) {
                String html = game.getDescription();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    description.setText( Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY) );
                } else {
                    description.setText( Html.fromHtml(html) );
                }
            }
        }

        // TODO: add documentation
        private void setGameRating(Game game) {

            GamePageActivity activity = mGamePageActivity.get();

            if (game.hasPegi()) {

                HorizontalScrollView pegiContainer = activity.findViewById(R.id.pegi_container);
                pegiContainer.setVisibility(View.VISIBLE);

                for (String type : game.getPegi()) {
                    switch (type) {
                        case "pegi3":
                            activity.findViewById(R.id.pegi_pegi3).setVisibility(View.VISIBLE);
                            break;
                        case "pegi7":
                            activity.findViewById(R.id.pegi_pegi7).setVisibility(View.VISIBLE);
                            break;
                        case "pegi12":
                            activity.findViewById(R.id.pegi_pegi12).setVisibility(View.VISIBLE);
                            break;
                        case "pegi16":
                            activity.findViewById(R.id.pegi_pegi16).setVisibility(View.VISIBLE);
                            break;
                        case "pegi18":
                            activity.findViewById(R.id.pegi_pegi18).setVisibility(View.VISIBLE);
                            break;
                        case "bad-language":
                            activity.findViewById(R.id.pegi_bad_language).setVisibility(View.VISIBLE);
                            break;
                        case "discrimination":
                            activity.findViewById(R.id.pegi_discrimination).setVisibility(View.VISIBLE);
                            break;
                        case "drugs":
                            activity.findViewById(R.id.pegi_drugs).setVisibility(View.VISIBLE);
                            break;
                        case "fear":
                            activity.findViewById(R.id.pegi_fear).setVisibility(View.VISIBLE);
                            break;
                        case "gambling":
                            activity.findViewById(R.id.pegi_gambling).setVisibility(View.VISIBLE);
                            break;
                        case "online":
                            activity.findViewById(R.id.pegi_online).setVisibility(View.VISIBLE);
                            break;
                        case "sex":
                            activity.findViewById(R.id.pegi_sex).setVisibility(View.VISIBLE);
                            break;
                        case "violence":
                            activity.findViewById(R.id.pegi_violence).setVisibility(View.VISIBLE);
                            break;
                    }
                }
            }

        }

        // TODO: add documentation
        private void setGameImages(Game game) {

            GamePageActivity activity = mGamePageActivity.get();

            // Get views
            ImageView cover = activity.findViewById(R.id.cover);
            LinearLayout gallery = activity.findViewById(R.id.gallery);
            LinearLayout galleryContainer = activity.findViewById(R.id.gallery_container);

            // load cover in the view
            Picasso.get().load( game.getCover() ).into(cover);

            // Add images to gallery
            if (game.hasGallery()) {

                galleryContainer.setVisibility(View.VISIBLE);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);

                List<Uri> images = game.getGallery();

                // create an array which is used as tag for images
                ArrayList<String> UriImages = new ArrayList<>();
                for (Uri uri : images) {
                    UriImages.add( uri.toString() );
                }

                // insert all the images in the gallery
                for (int i = 0; i < images.size(); ++i) {

                    // create view
                    ImageView imageView = new ImageView(activity);

                    // init view
                    imageView.setAdjustViewBounds(true);
                    imageView.setLayoutParams(layoutParams);

                    // last image in the gallery doesn't have margin
                    if (i != images.size()-1) {
                        imageView.setPadding(0, 0, (int) Util.convertDpToPx(activity, 8), 0);
                    }

                    // set tag
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("gallery", UriImages);
                    bundle.putInt("position", i);
                    imageView.setTag(bundle);

                    // set listener
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openGallery(v);
                        }
                    });

                    // load image in the view
                    Picasso.get().load( images.get(i) ).into(imageView);

                    // add view to gallery
                    gallery.addView(imageView);
                }
            }
        }

        // TODO: add documentation
        private void setGamePrices(GamePreview game) {

            GamePageActivity activity = mGamePageActivity.get();

            LayoutInflater inflater = activity.getLayoutInflater();
            ViewGroup parent = activity.findViewById(R.id.section_prices);

            // set new prices
            if (game.hasNewPrice()) {

                // create the container
                ViewGroup container = (ViewGroup) inflater
                        .inflate(R.layout.partial_price_container, parent, false);

                // add prices to the container
                setPrice(container, game.getNewPrice(), R.string.new_price);
                if (game.hasOlderNewPrices()) {
                    setOldPrices(container, game.getOlderNewPrices());
                }

                // add the container to the parent
                parent.addView(container);
            }

            // set old prices
            if (game.hasUsedPrice()) {

                // create the container
                LinearLayout container = (LinearLayout) inflater
                        .inflate(R.layout.partial_price_container, parent, false);

                // add prices to the container
                setPrice(container, game.getUsedPrice(), R.string.used_price);
                if (game.hasOlderUsedPrices()) {
                    setOldPrices(container, game.getOlderUsedPrices());
                }

                // add the container to the parent
                parent.addView(container);
            }

            // set digital prices
            if (game.hasDigitalPrice()) {

                // create the container
                LinearLayout container = (LinearLayout) inflater
                        .inflate(R.layout.partial_price_container, parent, false);

                // add prices to the container
                setPrice(container, game.getDigitalPrice(), R.string.digital_price);
                if (game.hasOlderDigitalPrices()) {
                    setOldPrices(container, game.getOlderDigitalPrices());
                }

                // add the container to the parent
                parent.addView(container);
            }

            // set preorder prices
            if (game.hasPreorderPrice()) {

                // create the container
                LinearLayout container = (LinearLayout) inflater
                        .inflate(R.layout.partial_price_container, parent, false);

                // add prices to the container
                setPrice(container, game.getPreorderPrice(), R.string.preorder_price);
                if (game.hasOlderPreorderPrices()) {
                    setOldPrices(container, game.getOlderPreorderPrices());
                }

                // add the container to the parent
                parent.addView(container);
            }
        }

        // TODO: add documentation
        private void setPrice(ViewGroup container, Double price, int priceType) {

            GamePageActivity activity = mGamePageActivity.get();
            DecimalFormat df = new DecimalFormat("#.00");

            // create view
            TextView priceView = new TextView(activity);

            // set view parameters
            priceView.append( activity.getString(priceType) );
            priceView.append( df.format(price) );
            priceView.append( activity.getString(R.string.currency) );
            priceView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            priceView.setTextColor(Color.WHITE);
            priceView.setTypeface(Typeface.DEFAULT_BOLD);

            // add view to the container
            container.addView(priceView);
        }

        // TODO: add documentation
        private void setOldPrices(ViewGroup container, List<Double> oldPrices) {

            GamePageActivity activity = mGamePageActivity.get();
            DecimalFormat df = new DecimalFormat("#.00");

            for (Double oldPrice : oldPrices) {

                // create view
                TextView oldPricesView = new TextView(activity);

                // set view parameters
                oldPricesView.append( df.format(oldPrice) );
                oldPricesView.append( activity.getString(R.string.currency) );
                oldPricesView.setPadding( (int) Util.convertDpToPx(activity, 7), 0, 0, 0);
                oldPricesView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                oldPricesView.setTextColor(Color.WHITE);
                oldPricesView.setTypeface(Typeface.DEFAULT_BOLD);
                oldPricesView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

                // add view to the container
                container.addView(oldPricesView);
            }
        }

        // TODO: add documentation
        private void setGamePromo(Game game) {

            GamePageActivity activity = mGamePageActivity.get();
            LayoutInflater inflater = activity.getLayoutInflater();

            // Get Views
            LinearLayout promoContainer = activity.findViewById(R.id.promo_container);

            // TODO: set URL to promo Message
            // TODO: check if message is available
            if (game.hasPromo()) {
                for (Promo promo : game.getPromo()) {

                    // Create a promo view
                    View promoView = inflater.inflate(R.layout.partial_section_promo,
                            promoContainer, false);

                    // init promo view
                    TextView promoHeader = promoView.findViewById(R.id.promo_header);
                    TextView promoValidity = promoView.findViewById(R.id.promo_validity);
                    TextView promoMessage = promoView.findViewById(R.id.promo_message);

                    promoHeader.setText( promo.getHeader() );
                    promoValidity.setText( promo.getValidity() );

                    // TODO: implement hasMessage() method in Promo class
                    if (promo.getMessage() != null && !promo.getMessage().isEmpty()) {
                        promoMessage.setText( promo.getMessage() );
                        promoMessage.setVisibility(View.VISIBLE);
                    }

                    // add promo to promoContainer
                    promoContainer.addView(promoView);
                }

                promoContainer.setVisibility(View.VISIBLE);
            }
        }

        // TODO: add documentation
        private void openGallery(View view) {

            GamePageActivity activity = mGamePageActivity.get();

            Bundle bundle = (Bundle) view.getTag();
            int position = bundle.getInt("position");
            ArrayList<String> gallery = bundle.getStringArrayList("gallery");

            // create and start intent
            Intent intent = new Intent(activity, GalleryActivity.class);
            intent.putStringArrayListExtra("gallery", gallery);
            intent.putExtra("position", position);
            activity.startActivity(intent);
        }

    }

}
