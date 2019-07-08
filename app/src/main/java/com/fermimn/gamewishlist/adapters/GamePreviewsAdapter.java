package com.fermimn.gamewishlist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.data_types.GamePreview;
import com.fermimn.gamewishlist.data_types.GamePreviews;

import java.text.DecimalFormat;

public class GamePreviewsAdapter extends ArrayAdapter<GamePreview> {

    private static final String TAG = GamePreviewsAdapter.class.getSimpleName();

    private TextView mTitle;
    private TextView mPlatform;
    private TextView mPublisher;

    private TextView mNewPrice;
    private TextView mUsedPrice;
    private TextView mDigitalPrice;
    private TextView mPreorderPrice;

    private TextView mOlderNewPrices;
    private TextView mOlderUsedPrices;
    private TextView mOlderDigitalPrices;
    private TextView mOlderPreorderPrices;

    private LinearLayout mCategoryNew;
    private LinearLayout mCategoryUsed;
    private LinearLayout mCategoryDigital;
    private LinearLayout mCategoryPreorder;

    private ImageView image;

    public GamePreviewsAdapter(Context context, GamePreviews gamePreviews) {
        super(context, R.layout.fragment_game_preview, gamePreviews);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.fragment_game_preview, null);

        // gets TextViews
        mTitle = convertView.findViewById(R.id.title);
        mPlatform = convertView.findViewById(R.id.platform);
        mPublisher = convertView.findViewById(R.id.publisher);

        mNewPrice = convertView.findViewById(R.id.new_price);
        mUsedPrice = convertView.findViewById(R.id.used_price);
        mDigitalPrice = convertView.findViewById(R.id.digital_price);
        mPreorderPrice = convertView.findViewById(R.id.preorder_price);

        mCategoryNew = convertView.findViewById(R.id.category_new);
        mCategoryUsed = convertView.findViewById(R.id.category_used);
        mCategoryDigital = convertView.findViewById(R.id.category_digital);
        mCategoryPreorder = convertView.findViewById(R.id.category_preorder);

        mOlderNewPrices = convertView.findViewById(R.id.older_new_prices);
        mOlderUsedPrices = convertView.findViewById(R.id.older_used_prices);
        mOlderDigitalPrices = convertView.findViewById(R.id.older_digital_prices);
        mOlderPreorderPrices = convertView.findViewById(R.id.older_preorder_prices);

        image = convertView.findViewById(R.id.image);

        // sets Text
        GamePreview gamePreview = getItem(position);

        mTitle.setText( gamePreview.getTitle() );
        mPlatform.setText( gamePreview.getPlatform() );
        mPublisher.setText( gamePreview.getPublisher() );

        DecimalFormat df = new DecimalFormat("#.00");

        if (gamePreview.hasNewPrice()) {
            mCategoryNew.setVisibility(View.VISIBLE);
            mNewPrice.setVisibility(View.VISIBLE);
            mNewPrice.setText( df.format( gamePreview.getNewPrice() ) + "€" );

            if (gamePreview.hasOlderNewPrices()) {
                mOlderNewPrices.setVisibility(View.VISIBLE);
                String str = new String();
                for (Double price: gamePreview.getOlderNewPrices()) {
                    str += df.format(price) + "€ ";
                }
                mOlderNewPrices.setText(str);
            } else {
                mOlderNewPrices.setVisibility(View.GONE);
            }

        } else {
            mCategoryNew.setVisibility(View.GONE);
        }

        if (gamePreview.hasUsedPrice()) {
            mCategoryUsed.setVisibility(View.VISIBLE);
            mUsedPrice.setVisibility(View.VISIBLE);
            mUsedPrice.setText( df.format( gamePreview.getUsedPrice() ) + "€" );

            if (gamePreview.hasOlderUsedPrices()) {
                mOlderUsedPrices.setVisibility(View.VISIBLE);
                String str = new String();
                for (Double price: gamePreview.getOlderNewPrices()) {
                    str += df.format(price) + "€ ";
                }
                mOlderUsedPrices.setText(str);
            } else {
                mOlderUsedPrices.setVisibility(View.GONE);
            }

        } else {
            mCategoryUsed.setVisibility(View.GONE);
        }

        if (gamePreview.hasDigitalPrice()) {
            mCategoryDigital.setVisibility(View.VISIBLE);
            mDigitalPrice.setVisibility(View.VISIBLE);
            mDigitalPrice.setText( df.format( gamePreview.getDigitalPrice() ) + "€" );

            if (gamePreview.hasOlderDigitalPrices()) {
                mOlderDigitalPrices.setVisibility(View.VISIBLE);
                String str = new String();
                for (Double price: gamePreview.getOlderNewPrices()) {
                    str += df.format(price) + "€ ";
                }
                mOlderDigitalPrices.setText(str);
            } else {
                mOlderDigitalPrices.setVisibility(View.GONE);
            }

        } else {
            mCategoryDigital.setVisibility(View.GONE);
        }

        if (gamePreview.hasPreorderPrice()) {
            mCategoryPreorder.setVisibility(View.VISIBLE);
            mPreorderPrice.setVisibility(View.VISIBLE);
            mPreorderPrice.setText( df.format( gamePreview.getPreorderPrice() ) + "€" );

            if (gamePreview.hasOlderPreorderPrices()) {
                mOlderPreorderPrices.setVisibility(View.VISIBLE);
                String str = new String();
                for (Double price: gamePreview.getOlderNewPrices()) {
                    str += df.format(price) + "€ ";
                }
                mOlderPreorderPrices.setText(str);
            } else {
                mOlderPreorderPrices.setVisibility(View.GONE);
            }

        } else {
            mCategoryPreorder.setVisibility(View.GONE);
        }

        return convertView;
    }

}
