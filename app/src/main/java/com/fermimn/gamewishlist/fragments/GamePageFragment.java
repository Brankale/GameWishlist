package com.fermimn.gamewishlist.fragments;

import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.data_types.Game;
import com.fermimn.gamewishlist.data_types.Promo;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class GamePageFragment extends Fragment {

    private static final String TAG = GamePageFragment.class.getSimpleName();

    private Context mContext;

    private Game mGame;

    public GamePageFragment(Game game) {
        mGame = game;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_game_page, container, false);

        // Get
        ImageView cover = view.findViewById(R.id.cover);
        TextView title = view.findViewById(R.id.title);
        TextView publisher = view.findViewById(R.id.publisher);
        TextView platform = view.findViewById(R.id.platform);
        TextView genres = view.findViewById(R.id.genres);
        TextView releaseDate = view.findViewById(R.id.releaseDate);

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
            view.findViewById(R.id.players_container).setVisibility(View.VISIBLE);
            TextView players = view.findViewById(R.id.players);
            players.setText( mGame.getPlayers() );
        }

        if (mGame.hasOfficialSite()) {
            view.findViewById(R.id.official_site_container).setVisibility(View.VISIBLE);
            TextView officialSite = view.findViewById(R.id.officialSite);
            officialSite.setText( mGame.getOfficialSite() );
        }

        Picasso.get().load( mGame.getCover() ).into(cover);

        if (mGame.hasPegi()) {

            HorizontalScrollView pegiContainer = view.findViewById(R.id.pegi_container);
            pegiContainer.setVisibility(View.VISIBLE);

            for (String type : mGame.getPegi()) {
                switch (type) {
                    case "pegi3":
                        view.findViewById(R.id.pegi_pegi3).setVisibility(View.VISIBLE);
                        break;
                    case "pegi7":
                        view.findViewById(R.id.pegi_pegi7).setVisibility(View.VISIBLE);
                        break;
                    case "pegi12":
                        view.findViewById(R.id.pegi_pegi12).setVisibility(View.VISIBLE);
                        break;
                    case "pegi16":
                        view.findViewById(R.id.pegi_pegi16).setVisibility(View.VISIBLE);
                        break;
                    case "pegi18":
                        view.findViewById(R.id.pegi_pegi18).setVisibility(View.VISIBLE);
                        break;
                    case "bad-language":
                        view.findViewById(R.id.pegi_bad_language).setVisibility(View.VISIBLE);
                        break;
                    case "discrimination":
                        view.findViewById(R.id.pegi_discrimination).setVisibility(View.VISIBLE);
                        break;
                    case "drugs":
                        view.findViewById(R.id.pegi_drugs).setVisibility(View.VISIBLE);
                        break;
                    case "fear":
                        view.findViewById(R.id.pegi_fear).setVisibility(View.VISIBLE);
                        break;
                    case "gambling":
                        view.findViewById(R.id.pegi_gambling).setVisibility(View.VISIBLE);
                        break;
                    case "online":
                        view.findViewById(R.id.pegi_online).setVisibility(View.VISIBLE);
                        break;
                    case "sex":
                        view.findViewById(R.id.pegi_sex).setVisibility(View.VISIBLE);
                        break;
                    case "violence":
                        view.findViewById(R.id.pegi_violence).setVisibility(View.VISIBLE);
                        break;
                }
            }
        }

        setPricesSection(view, mGame);

        // Add images to gallery
        if (mGame.hasGallery()) {
            LinearLayout galleryContainer = view.findViewById(R.id.gallery_container);
            galleryContainer.setVisibility(View.VISIBLE);

            LinearLayout gallery = view.findViewById(R.id.gallery);

            for (Uri image : mGame.getGallery()) {
                Log.d(TAG, image.toString());
                ImageView imageView = new ImageView(mContext);
                imageView.setAdjustViewBounds(true);

                float scale = mContext.getResources().getDisplayMetrics().density;
                int marginRight = (int) (10 * scale + 0.5f);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                layoutParams.setMarginEnd(marginRight);
                imageView.setLayoutParams(layoutParams);

//                imageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        openGallery(v);
//                    }
//                });

                Picasso.get().load(image).into(imageView);
                gallery.addView(imageView);
            }

        }

        if (mGame.hasDescription()) {
            TextView description = view.findViewById(R.id.description);
            description.setText(mGame.getDescription());
        }

        if (mGame.isValidForPromotions()){
            LinearLayout validForPromotions = view.findViewById(R.id.valid_for_promotions);
            validForPromotions.setVisibility(View.VISIBLE);
        }

        // TODO: set URL to promo Message
        // TODO: check if message is available
        if (mGame.hasPromo()) {

            LinearLayout promoContainer = view.findViewById(R.id.promo_container);

            for (Promo promo : mGame.getPromo()) {
                View promoView = inflater.inflate(R.layout.partial_section_promo,
                        container, false);

                TextView promoHeader = promoView.findViewById(R.id.promo_header);
                TextView promoValidity = promoView.findViewById(R.id.promo_validity);
                TextView promoMessage = promoView.findViewById(R.id.promo_message);

                promoHeader.setText( promo.getHeader() );
                promoValidity.setText( promo.getValidity() );
                promoMessage.setText( promo.getMessage() );

                promoContainer.addView(promoView);
            }
        }

        return view;
    }

    private void setPricesSection(View view, Game game) {

        // Find views
        LinearLayout mCategoryNewView = view.findViewById(R.id.category_new);
        LinearLayout mCategoryUsedView = view.findViewById(R.id.category_used);
        LinearLayout mCategoryDigitalView = view.findViewById(R.id.category_digital);
        LinearLayout mCategoryPreorderView = view.findViewById(R.id.category_preorder);

        TextView mNewPriceView = view.findViewById(R.id.new_price);
        TextView mUsedPriceView = view.findViewById(R.id.used_price);
        TextView mDigitalPriceView = view.findViewById(R.id.digital_price);
        TextView mPreorderPriceView = view.findViewById(R.id.preorder_price);

        TextView mOlderNewPricesView = view.findViewById(R.id.older_new_prices);
        TextView mOlderUsedPricesView = view.findViewById(R.id.older_used_prices);
        TextView mOlderDigitalPricesView = view.findViewById(R.id.older_digital_prices);
        TextView mOlderPreorderPricesView = view.findViewById(R.id.older_preorder_prices);


        // sets Text

        // TODO: only one older price is displayed

        DecimalFormat df = new DecimalFormat("#.00");

        if (game.hasNewPrice()) {
            mCategoryNewView.setVisibility(View.VISIBLE);
            mNewPriceView.setVisibility(View.VISIBLE);
            mNewPriceView.setText( df.format( game.getNewPrice() ) + "€" );

            if (game.hasOlderNewPrices()) {
                mOlderNewPricesView.setVisibility(View.VISIBLE);
                String price = df.format( game.getOlderNewPrices().get(0) ) + "€";
                mOlderNewPricesView.setText(price);
                mOlderNewPricesView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                mOlderNewPricesView.setVisibility(View.GONE);
            }

        } else {
            mCategoryNewView.setVisibility(View.GONE);
        }

        if (game.hasUsedPrice()) {
            mCategoryUsedView.setVisibility(View.VISIBLE);
            mUsedPriceView.setVisibility(View.VISIBLE);
            mUsedPriceView.setText( df.format( game.getUsedPrice() ) + "€" );

            if (game.hasOlderUsedPrices()) {
                mOlderUsedPricesView.setVisibility(View.VISIBLE);
                String price = df.format( game.getOlderUsedPrices().get(0) ) + "€";
                mOlderUsedPricesView.setText(price);
                mOlderUsedPricesView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                mOlderUsedPricesView.setVisibility(View.GONE);
            }

        } else {
            mCategoryUsedView.setVisibility(View.GONE);
        }

        if (game.hasDigitalPrice()) {
            mCategoryDigitalView.setVisibility(View.VISIBLE);
            mDigitalPriceView.setVisibility(View.VISIBLE);
            mDigitalPriceView.setText( df.format( game.getDigitalPrice() ) + "€" );

            if (game.hasOlderDigitalPrices()) {
                mOlderDigitalPricesView.setVisibility(View.VISIBLE);
                String price = df.format( game.getOlderDigitalPrices().get(0) ) + "€";
                mOlderDigitalPricesView.setText(price);
                mOlderDigitalPricesView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                mOlderDigitalPricesView.setVisibility(View.GONE);
            }

        } else {
            mCategoryDigitalView.setVisibility(View.GONE);
        }

        if (game.hasPreorderPrice()) {
            mCategoryPreorderView.setVisibility(View.VISIBLE);
            mPreorderPriceView.setVisibility(View.VISIBLE);
            mPreorderPriceView.setText( df.format( game.getPreorderPrice() ) + "€" );

            if (game.hasOlderPreorderPrices()) {
                mOlderPreorderPricesView.setVisibility(View.VISIBLE);
                String price = df.format( game.getOlderPreorderPrices().get(0) ) + "€";
                mOlderPreorderPricesView.setText(price);
                mOlderPreorderPricesView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                mOlderPreorderPricesView.setVisibility(View.GONE);
            }

        } else {
            mCategoryPreorderView.setVisibility(View.GONE);
        }
    }

    private void openGallery(View view) {
//        File [] galleryImages = new File(DirectoryManager.getGameGalleryDirectory(gameOfThePage.getId())).listFiles();
//        String[] images = new String[galleryImages.length];
//        for(int i=0;i<galleryImages.length;i++){
//            images[i] = "file://" + galleryImages[i].getPath();
//        }
//
//        int position;
//
//
//        Intent i = new Intent(this,ActivityGallery.class);
//        i.putExtra("images",images);
//        i.putExtra("position",(int)v.getTag());
//        startActivity(i);
    }

}
