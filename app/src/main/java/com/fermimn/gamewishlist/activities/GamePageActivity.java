package com.fermimn.gamewishlist.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.data_types.Game;
import com.fermimn.gamewishlist.data_types.Promo;
import com.fermimn.gamewishlist.utils.Gamestop;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class GamePageActivity extends AppCompatActivity {

    private Game mGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);

        String id = getIntent().getStringExtra("gameID");
        DownloadGame task = new DownloadGame();
        task.execute(id);
    }

    // TODO: add documentation
    private class DownloadGame extends AsyncTask<String, Integer, Game> {

        @Override
        protected Game doInBackground(String... strings) {
            String id = strings[0];
            Gamestop gamestop = new Gamestop();
            return gamestop.downloadGame(id);
        }

        @Override
        protected void onPostExecute(Game game) {
            showGamePage(game);
        }

    }

    // TODO: add documentation
    private void showGamePage(Game game) {

        // TODO: change when possible
        mGame = game;

        // Get
        ImageView cover = findViewById(R.id.cover);

        Picasso.get().load( mGame.getCover() ).into(cover);

        TextView title = findViewById(R.id.title);
        TextView publisher = findViewById(R.id.publisher);
        TextView platform = findViewById(R.id.platform);
        TextView genres = findViewById(R.id.genres);
        TextView releaseDate = findViewById(R.id.releaseDate);

        title.setText( mGame.getTitle() );
        publisher.setText( mGame.getPublisher() );
        platform.setText( mGame.getPlatform() );

        if (mGame.hasGenres()) {
            List<String> tmpGenres = mGame.getGenres();
            for (int i = 0; i < tmpGenres.size(); ++i) {
                if (i==0) {
                    genres.setText(tmpGenres.get(i));
                } else {
                    genres.setText(genres.getText() + "/" + tmpGenres.get(i));
                }
            }
        }

        if (mGame.hasReleaseDate()) {
            releaseDate.setText( mGame.getReleaseDate() );
        }

        if (mGame.hasPlayers()) {
            findViewById(R.id.players_container).setVisibility(View.VISIBLE);
            TextView players = findViewById(R.id.players);
            players.setText( mGame.getPlayers() );
        }

        if (mGame.hasOfficialSite()) {
            findViewById(R.id.official_site_container).setVisibility(View.VISIBLE);
            TextView officialSite = findViewById(R.id.officialSite);
            officialSite.setText( mGame.getOfficialSite() );
        }



        if (mGame.hasPegi()) {

            HorizontalScrollView pegiContainer = findViewById(R.id.pegi_container);
            pegiContainer.setVisibility(View.VISIBLE);

            for (String type : mGame.getPegi()) {
                switch (type) {
                    case "pegi3":
                        findViewById(R.id.pegi_pegi3).setVisibility(View.VISIBLE);
                        break;
                    case "pegi7":
                        findViewById(R.id.pegi_pegi7).setVisibility(View.VISIBLE);
                        break;
                    case "pegi12":
                        findViewById(R.id.pegi_pegi12).setVisibility(View.VISIBLE);
                        break;
                    case "pegi16":
                        findViewById(R.id.pegi_pegi16).setVisibility(View.VISIBLE);
                        break;
                    case "pegi18":
                        findViewById(R.id.pegi_pegi18).setVisibility(View.VISIBLE);
                        break;
                    case "bad-language":
                        findViewById(R.id.pegi_bad_language).setVisibility(View.VISIBLE);
                        break;
                    case "discrimination":
                        findViewById(R.id.pegi_discrimination).setVisibility(View.VISIBLE);
                        break;
                    case "drugs":
                        findViewById(R.id.pegi_drugs).setVisibility(View.VISIBLE);
                        break;
                    case "fear":
                        findViewById(R.id.pegi_fear).setVisibility(View.VISIBLE);
                        break;
                    case "gambling":
                        findViewById(R.id.pegi_gambling).setVisibility(View.VISIBLE);
                        break;
                    case "online":
                        findViewById(R.id.pegi_online).setVisibility(View.VISIBLE);
                        break;
                    case "sex":
                        findViewById(R.id.pegi_sex).setVisibility(View.VISIBLE);
                        break;
                    case "violence":
                        findViewById(R.id.pegi_violence).setVisibility(View.VISIBLE);
                        break;
                }
            }
        }

        setPricesSection();

        // Add images to gallery
        if (mGame.hasGallery()) {
            LinearLayout galleryContainer = findViewById(R.id.gallery_container);
            galleryContainer.setVisibility(View.VISIBLE);

            LinearLayout gallery = findViewById(R.id.gallery);

            for (Uri image : mGame.getGallery()) {
                ImageView imageView = new ImageView(this);
                imageView.setAdjustViewBounds(true);

                float scale = getResources().getDisplayMetrics().density;
                int marginRight = (int) (10 * scale + 0.5f);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                layoutParams.setMarginEnd(marginRight);
                imageView.setLayoutParams(layoutParams);

                Picasso.get().load(image).into(imageView);
                gallery.addView(imageView);
            }

            gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openGallery(v);
                }
            });

        }

        if (mGame.hasDescription()) {
            TextView description = findViewById(R.id.description);
            String html = mGame.getDescription();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                description.setText( Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY) );
            } else {
                description.setText( Html.fromHtml(html) );
            }
        }

        if (mGame.isValidForPromotions()){
            LinearLayout validForPromotions = findViewById(R.id.valid_for_promotions);
            validForPromotions.setVisibility(View.VISIBLE);
        }

        // TODO: set URL to promo Message
        // TODO: check if message is available
        if (mGame.hasPromo()) {

            LinearLayout promoContainer = findViewById(R.id.promo_container);

            for (Promo promo : mGame.getPromo()) {
                View promoView = getLayoutInflater().inflate(R.layout.partial_section_promo,
                        promoContainer, false);

                TextView promoHeader = promoView.findViewById(R.id.promo_header);
                TextView promoValidity = promoView.findViewById(R.id.promo_validity);
                TextView promoMessage = promoView.findViewById(R.id.promo_message);

                promoHeader.setText( promo.getHeader() );
                promoValidity.setText( promo.getValidity() );
                promoMessage.setText( promo.getMessage() );

                promoContainer.addView(promoView);
            }
        }

        LinearLayout linearLayout = findViewById(R.id.game_page_container);
        linearLayout.setVisibility(View.VISIBLE);

    }

    private void setPricesSection() {

        // Find views
        LinearLayout mCategoryNewView = findViewById(R.id.category_new);
        LinearLayout mCategoryUsedView = findViewById(R.id.category_used);
        LinearLayout mCategoryDigitalView = findViewById(R.id.category_digital);
        LinearLayout mCategoryPreorderView = findViewById(R.id.category_preorder);

        TextView mNewPriceView = findViewById(R.id.new_price);
        TextView mUsedPriceView = findViewById(R.id.used_price);
        TextView mDigitalPriceView = findViewById(R.id.digital_price);
        TextView mPreorderPriceView = findViewById(R.id.preorder_price);

        TextView mOlderNewPricesView = findViewById(R.id.older_new_prices);
        TextView mOlderUsedPricesView = findViewById(R.id.older_used_prices);
        TextView mOlderDigitalPricesView = findViewById(R.id.older_digital_prices);
        TextView mOlderPreorderPricesView = findViewById(R.id.older_preorder_prices);


        // sets Text

        // TODO: only one older price is displayed

        DecimalFormat df = new DecimalFormat("#.00");

        if (mGame.hasNewPrice()) {
            mCategoryNewView.setVisibility(View.VISIBLE);
            mNewPriceView.setVisibility(View.VISIBLE);
            mNewPriceView.setText( df.format( mGame.getNewPrice() ) + "€" );

            if (mGame.hasOlderNewPrices()) {
                mOlderNewPricesView.setVisibility(View.VISIBLE);
                String price = df.format( mGame.getOlderNewPrices().get(0) ) + "€";
                mOlderNewPricesView.setText(price);
                mOlderNewPricesView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                mOlderNewPricesView.setVisibility(View.GONE);
            }

        } else {
            mCategoryNewView.setVisibility(View.GONE);
        }

        if (mGame.hasUsedPrice()) {
            mCategoryUsedView.setVisibility(View.VISIBLE);
            mUsedPriceView.setVisibility(View.VISIBLE);
            mUsedPriceView.setText( df.format( mGame.getUsedPrice() ) + "€" );

            if (mGame.hasOlderUsedPrices()) {
                mOlderUsedPricesView.setVisibility(View.VISIBLE);
                String price = df.format( mGame.getOlderUsedPrices().get(0) ) + "€";
                mOlderUsedPricesView.setText(price);
                mOlderUsedPricesView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                mOlderUsedPricesView.setVisibility(View.GONE);
            }

        } else {
            mCategoryUsedView.setVisibility(View.GONE);
        }

        if (mGame.hasDigitalPrice()) {
            mCategoryDigitalView.setVisibility(View.VISIBLE);
            mDigitalPriceView.setVisibility(View.VISIBLE);
            mDigitalPriceView.setText( df.format( mGame.getDigitalPrice() ) + "€" );

            if (mGame.hasOlderDigitalPrices()) {
                mOlderDigitalPricesView.setVisibility(View.VISIBLE);
                String price = df.format( mGame.getOlderDigitalPrices().get(0) ) + "€";
                mOlderDigitalPricesView.setText(price);
                mOlderDigitalPricesView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                mOlderDigitalPricesView.setVisibility(View.GONE);
            }

        } else {
            mCategoryDigitalView.setVisibility(View.GONE);
        }

        if (mGame.hasPreorderPrice()) {
            mCategoryPreorderView.setVisibility(View.VISIBLE);
            mPreorderPriceView.setVisibility(View.VISIBLE);
            mPreorderPriceView.setText( df.format( mGame.getPreorderPrice() ) + "€" );

            if (mGame.hasOlderPreorderPrices()) {
                mOlderPreorderPricesView.setVisibility(View.VISIBLE);
                String price = df.format( mGame.getOlderPreorderPrices().get(0) ) + "€";
                mOlderPreorderPricesView.setText(price);
                mOlderPreorderPricesView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                mOlderPreorderPricesView.setVisibility(View.GONE);
            }

        } else {
            mCategoryPreorderView.setVisibility(View.GONE);
        }
    }

    public void openGallery(View view) {
        List<Uri> gallery = mGame.getGallery();
        String[] uri = new String[gallery.size()];

        for (int i = 0; i < gallery.size(); ++i) {
            uri[i] = gallery.get(i).toString();
        }

        Intent intent = new Intent(this, GalleryActivity.class);
        intent.putExtra("URIs", uri);
        startActivity(intent);
    }

}
