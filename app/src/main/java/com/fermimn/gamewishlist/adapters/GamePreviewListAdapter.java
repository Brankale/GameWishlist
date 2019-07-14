package com.fermimn.gamewishlist.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.data_types.GamePreview;
import com.fermimn.gamewishlist.data_types.GamePreviewList;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class GamePreviewListAdapter extends ArrayAdapter<GamePreview> {

    private static final String TAG = GamePreviewListAdapter.class.getSimpleName();

    /**
     * This class is used to improve performance
     * It avoids useless findViewById that makes the app laggy
     * See "ViewHolder pattern" to learn more
     */
    private static class ViewHolder {

        public TextView mTitleView;
        public TextView mPlatformView;
        public TextView mPublisherView;

        public TextView mNewPriceView;
        public TextView mUsedPriceView;
        public TextView mDigitalPriceView;
        public TextView mPreorderPriceView;

        public TextView mOlderNewPricesView;
        public TextView mOlderUsedPricesView;
        public TextView mOlderDigitalPricesView;
        public TextView mOlderPreorderPricesView;

        public LinearLayout mCategoryNewView;
        public LinearLayout mCategoryUsedView;
        public LinearLayout mCategoryDigitalView;
        public LinearLayout mCategoryPreorderView;

        public ImageView mCoverView;
    }

    /**
     * Creates a GamePreviewListAdapter object
     * @param context application context
     * @param gamePreviewList the ArrayList of GamePreview
     */
    public GamePreviewListAdapter(Context context, GamePreviewList gamePreviewList) {
        super(context, R.layout.partial_game_preview, gamePreviewList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // For a fluid ListView
        if (convertView == null) {

            // inflate just when needed
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.partial_game_preview, null);

            // get Views using ViewHolder pattern
            ViewHolder viewHolder = new ViewHolder();

            viewHolder.mTitleView = convertView.findViewById(R.id.title);
            viewHolder.mPlatformView = convertView.findViewById(R.id.platform);
            viewHolder.mPublisherView = convertView.findViewById(R.id.publisher);

            viewHolder.mNewPriceView = convertView.findViewById(R.id.new_price);
            viewHolder.mUsedPriceView = convertView.findViewById(R.id.used_price);
            viewHolder.mDigitalPriceView = convertView.findViewById(R.id.digital_price);
            viewHolder.mPreorderPriceView = convertView.findViewById(R.id.preorder_price);

            viewHolder.mCategoryNewView = convertView.findViewById(R.id.category_new);
            viewHolder.mCategoryUsedView = convertView.findViewById(R.id.category_used);
            viewHolder.mCategoryDigitalView = convertView.findViewById(R.id.category_digital);
            viewHolder.mCategoryPreorderView = convertView.findViewById(R.id.category_preorder);

            viewHolder.mOlderNewPricesView = convertView.findViewById(R.id.older_new_prices);
            viewHolder.mOlderUsedPricesView = convertView.findViewById(R.id.older_used_prices);
            viewHolder.mOlderDigitalPricesView = convertView.findViewById(R.id.older_digital_prices);
            viewHolder.mOlderPreorderPricesView = convertView.findViewById(R.id.older_preorder_prices);

            viewHolder.mCoverView = convertView.findViewById(R.id.cover);

            convertView.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        // sets Text
        GamePreview gamePreview = getItem(position);

        viewHolder.mTitleView.setText( gamePreview.getTitle() );
        viewHolder.mPlatformView.setText( gamePreview.getPlatform() );
        viewHolder.mPublisherView.setText( gamePreview.getPublisher() );

        DecimalFormat df = new DecimalFormat("#.00");

        if (gamePreview.hasNewPrice()) {
            viewHolder.mCategoryNewView.setVisibility(View.VISIBLE);
            viewHolder.mNewPriceView.setVisibility(View.VISIBLE);
            viewHolder.mNewPriceView.setText( df.format( gamePreview.getNewPrice() ) + "€" );

            if (gamePreview.hasOlderNewPrices()) {
                viewHolder.mOlderNewPricesView.setVisibility(View.VISIBLE);
                String price = df.format( gamePreview.getOlderNewPrices().get(0) ) + "€";
                viewHolder.mOlderNewPricesView.setText(price);
                viewHolder.mOlderNewPricesView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                viewHolder.mOlderNewPricesView.setVisibility(View.GONE);
            }

        } else {
            viewHolder.mCategoryNewView.setVisibility(View.GONE);
        }

        if (gamePreview.hasUsedPrice()) {
            viewHolder.mCategoryUsedView.setVisibility(View.VISIBLE);
            viewHolder.mUsedPriceView.setVisibility(View.VISIBLE);
            viewHolder.mUsedPriceView.setText( df.format( gamePreview.getUsedPrice() ) + "€" );

            if (gamePreview.hasOlderUsedPrices()) {
                viewHolder.mOlderUsedPricesView.setVisibility(View.VISIBLE);
                String price = df.format( gamePreview.getOlderUsedPrices().get(0) ) + "€";
                viewHolder.mOlderUsedPricesView.setText(price);
                viewHolder.mOlderUsedPricesView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                viewHolder.mOlderUsedPricesView.setVisibility(View.GONE);
            }

        } else {
            viewHolder.mCategoryUsedView.setVisibility(View.GONE);
        }

        if (gamePreview.hasDigitalPrice()) {
            viewHolder.mCategoryDigitalView.setVisibility(View.VISIBLE);
            viewHolder.mDigitalPriceView.setVisibility(View.VISIBLE);
            viewHolder.mDigitalPriceView.setText( df.format( gamePreview.getDigitalPrice() ) + "€" );

            if (gamePreview.hasOlderDigitalPrices()) {
                viewHolder.mOlderDigitalPricesView.setVisibility(View.VISIBLE);
                String price = df.format( gamePreview.getOlderDigitalPrices().get(0) ) + "€";
                viewHolder.mOlderDigitalPricesView.setText(price);
                viewHolder.mOlderDigitalPricesView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                viewHolder.mOlderDigitalPricesView.setVisibility(View.GONE);
            }

        } else {
            viewHolder.mCategoryDigitalView.setVisibility(View.GONE);
        }

        if (gamePreview.hasPreorderPrice()) {
            viewHolder.mCategoryPreorderView.setVisibility(View.VISIBLE);
            viewHolder.mPreorderPriceView.setVisibility(View.VISIBLE);
            viewHolder.mPreorderPriceView.setText( df.format( gamePreview.getPreorderPrice() ) + "€" );

            if (gamePreview.hasOlderPreorderPrices()) {
                viewHolder.mOlderPreorderPricesView.setVisibility(View.VISIBLE);
                String price = df.format( gamePreview.getOlderPreorderPrices().get(0) ) + "€";
                viewHolder.mOlderPreorderPricesView.setText(price);
                viewHolder.mOlderPreorderPricesView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                viewHolder.mOlderPreorderPricesView.setVisibility(View.GONE);
            }

        } else {
            viewHolder.mCategoryPreorderView.setVisibility(View.GONE);
        }

        Picasso.get().load( gamePreview.getCover() )
                .placeholder(R.drawable.ic_image_not_available)
                .into(viewHolder.mCoverView);

        return convertView;
    }

}
