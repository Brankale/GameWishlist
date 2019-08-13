package com.fermimn.gamewishlist.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.data_types.GamePreview;
import com.fermimn.gamewishlist.data_types.GamePreviewList;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {

    private GamePreviewList mGamePreviewList;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView mCoverView;
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

        // TODO: what parameters constructor wants
        public ViewHolder(View view) {
            super(view);
        }

    }

    public TestAdapter(Context context, GamePreviewList gamePreviewList) {
        mGamePreviewList = gamePreviewList;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.partial_game_preview, parent, false);

        ViewHolder holder = new ViewHolder(view);

        holder.mCoverView = view.findViewById(R.id.cover);
        holder.mTitleView = view.findViewById(R.id.title);
        holder.mPlatformView = view.findViewById(R.id.platform);
        holder.mPublisherView = view.findViewById(R.id.publisher);

        holder.mNewPriceView = view.findViewById(R.id.new_price);
        holder.mUsedPriceView = view.findViewById(R.id.used_price);
        holder.mDigitalPriceView = view.findViewById(R.id.digital_price);
        holder.mPreorderPriceView = view.findViewById(R.id.preorder_price);

        holder.mCategoryNewView = view.findViewById(R.id.category_new);
        holder.mCategoryUsedView = view.findViewById(R.id.category_used);
        holder.mCategoryDigitalView = view.findViewById(R.id.category_digital);
        holder.mCategoryPreorderView = view.findViewById(R.id.category_preorder);

        holder.mOlderNewPricesView = view.findViewById(R.id.older_new_prices);
        holder.mOlderUsedPricesView = view.findViewById(R.id.older_used_prices);
        holder.mOlderDigitalPricesView = view.findViewById(R.id.older_digital_prices);
        holder.mOlderPreorderPricesView = view.findViewById(R.id.older_preorder_prices);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        GamePreview gamePreview = mGamePreviewList.get(position);

        holder.mTitleView.setText( gamePreview.getTitle() );
        holder.mPlatformView.setText( gamePreview.getPlatform() );
        holder.mPublisherView.setText( gamePreview.getPublisher() );

        DecimalFormat df = new DecimalFormat("#.00");

        if (gamePreview.hasNewPrice()) {
            holder.mCategoryNewView.setVisibility(View.VISIBLE);
            holder.mNewPriceView.setVisibility(View.VISIBLE);
            holder.mNewPriceView.setText( df.format( gamePreview.getNewPrice() ) + "€" );

            if (gamePreview.hasOlderNewPrices()) {
                holder.mOlderNewPricesView.setVisibility(View.VISIBLE);
                String price = df.format( gamePreview.getOlderNewPrices().get(0) ) + "€";
                holder.mOlderNewPricesView.setText(price);
                holder.mOlderNewPricesView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.mOlderNewPricesView.setVisibility(View.GONE);
            }

        } else {
            holder.mCategoryNewView.setVisibility(View.GONE);
        }

        if (gamePreview.hasUsedPrice()) {
            holder.mCategoryUsedView.setVisibility(View.VISIBLE);
            holder.mUsedPriceView.setVisibility(View.VISIBLE);
            holder.mUsedPriceView.setText( df.format( gamePreview.getUsedPrice() ) + "€" );

            if (gamePreview.hasOlderUsedPrices()) {
                holder.mOlderUsedPricesView.setVisibility(View.VISIBLE);
                String price = df.format( gamePreview.getOlderUsedPrices().get(0) ) + "€";
                holder.mOlderUsedPricesView.setText(price);
                holder.mOlderUsedPricesView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.mOlderUsedPricesView.setVisibility(View.GONE);
            }

        } else {
            holder.mCategoryUsedView.setVisibility(View.GONE);
        }

        if (gamePreview.hasDigitalPrice()) {
            holder.mCategoryDigitalView.setVisibility(View.VISIBLE);
            holder.mDigitalPriceView.setVisibility(View.VISIBLE);
            holder.mDigitalPriceView.setText( df.format( gamePreview.getDigitalPrice() ) + "€" );

            if (gamePreview.hasOlderDigitalPrices()) {
                holder.mOlderDigitalPricesView.setVisibility(View.VISIBLE);
                String price = df.format( gamePreview.getOlderDigitalPrices().get(0) ) + "€";
                holder.mOlderDigitalPricesView.setText(price);
                holder.mOlderDigitalPricesView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.mOlderDigitalPricesView.setVisibility(View.GONE);
            }

        } else {
            holder.mCategoryDigitalView.setVisibility(View.GONE);
        }

        if (gamePreview.hasPreorderPrice()) {
            holder.mCategoryPreorderView.setVisibility(View.VISIBLE);
            holder.mPreorderPriceView.setVisibility(View.VISIBLE);
            holder.mPreorderPriceView.setText( df.format( gamePreview.getPreorderPrice() ) + "€" );

            if (gamePreview.hasOlderPreorderPrices()) {
                holder.mOlderPreorderPricesView.setVisibility(View.VISIBLE);
                String price = df.format( gamePreview.getOlderPreorderPrices().get(0) ) + "€";
                holder.mOlderPreorderPricesView.setText(price);
                holder.mOlderPreorderPricesView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.mOlderPreorderPricesView.setVisibility(View.GONE);
            }

        } else {
            holder.mCategoryPreorderView.setVisibility(View.GONE);
        }

        // TODO: I'm not sure but I think that if Picasso try to download an image
        //       but in the mean time you do another research, Picasso lose the
        //       reference to the ImageView and the app crash.
        //       Try to put this thing in a try-catch, add a Log and then reproduce the problem
        //       with a low speed network
        Picasso
                .get()
                .load( gamePreview.getCover() )
                .into(holder.mCoverView);

//        Glide.with(mContext)
//            .load( gamePreview.getCover() )
//            .transition(DrawableTransitionOptions.withCrossFade())
//            .into(holder.mCoverView);

    }

    @Override
    public int getItemCount() {
        return mGamePreviewList.size();
    }

}
