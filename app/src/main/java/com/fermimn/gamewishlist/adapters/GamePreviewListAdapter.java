package com.fermimn.gamewishlist.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.fermimn.gamewishlist.activities.GamePageActivity;
import com.fermimn.gamewishlist.data_types.GamePreview;
import com.fermimn.gamewishlist.data_types.GamePreviewList;
import com.fermimn.gamewishlist.utils.WishlistManager;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class GamePreviewListAdapter extends RecyclerView.Adapter<GamePreviewListAdapter.ViewHolder> {

    @SuppressWarnings("unused")
    private static final String TAG = GamePreviewListAdapter.class.getSimpleName();

    private final GamePreviewList mGamePreviewList;
    private final Context mContext;

    public GamePreviewListAdapter(Context context, GamePreviewList gamePreviewList) {
        mGamePreviewList = gamePreviewList;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.partial_game_preview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final GamePreview gamePreview = mGamePreviewList.get(position);

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

        Picasso.get().load( gamePreview.getCover() ).into(holder.mCoverView);

        holder.mParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GamePreview gamePreview = mGamePreviewList.get(position);
                Intent intent = new Intent(mContext, GamePageActivity.class);
                intent.putExtra("gameID", gamePreview.getId());
                mContext.startActivity(intent);
            }
        });

        holder.mParent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                WishlistManager wishlistManager = WishlistManager.getInstance(mContext);
                GamePreviewList gamePreviewList = wishlistManager.getWishlist();
                boolean result = gamePreviewList.contains( mGamePreviewList.get(position) );

                if (result) {

                    new AlertDialog.Builder(mContext)
                            .setTitle( mContext.getString(R.string.dialog_remove_game_from_wishlist_title) )
                            .setMessage( mContext.getString(R.string.dialog_remove_game_from_wishlist_text) )

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    WishlistManager wishlist =
                                            WishlistManager.getInstance(mContext.getApplicationContext());

                                    GamePreview gamePreview = mGamePreviewList.get(position);
                                    wishlist.removeGameFromWishlist(gamePreview);

                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action
                            .setNegativeButton(android.R.string.no, null)
                            .show();

                } else {

                    new AlertDialog.Builder(mContext)
                            .setTitle( mContext.getString(R.string.dialog_add_game_to_wishlist_title) )
                            .setMessage( mContext.getString(R.string.dialog_add_game_to_wishlist_text) )

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    WishlistManager wishlist =
                                            WishlistManager.getInstance(mContext.getApplicationContext());

                                    GamePreview gamePreview = mGamePreviewList.get(position);
                                    wishlist.add(gamePreview);

                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action
                            .setNegativeButton(android.R.string.no, null)
                            .show();

                }

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mGamePreviewList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final ViewGroup mParent;

        private final ImageView mCoverView;
        private final TextView mTitleView;
        private final TextView mPlatformView;
        private final TextView mPublisherView;

        private final TextView mNewPriceView;
        private final TextView mUsedPriceView;
        private final TextView mDigitalPriceView;
        private final TextView mPreorderPriceView;

        private final TextView mOlderNewPricesView;
        private final TextView mOlderUsedPricesView;
        private final TextView mOlderDigitalPricesView;
        private final TextView mOlderPreorderPricesView;

        private final LinearLayout mCategoryNewView;
        private final LinearLayout mCategoryUsedView;
        private final LinearLayout mCategoryDigitalView;
        private final LinearLayout mCategoryPreorderView;

        ViewHolder(View view) {
            super(view);

            mParent = view.findViewById(R.id.parent_layout);

            mCoverView = view.findViewById(R.id.cover);
            mTitleView = view.findViewById(R.id.title);
            mPlatformView = view.findViewById(R.id.platform);
            mPublisherView = view.findViewById(R.id.publisher);

            mNewPriceView = view.findViewById(R.id.new_price);
            mUsedPriceView = view.findViewById(R.id.used_price);
            mDigitalPriceView = view.findViewById(R.id.digital_price);
            mPreorderPriceView = view.findViewById(R.id.preorder_price);

            mCategoryNewView = view.findViewById(R.id.category_new);
            mCategoryUsedView = view.findViewById(R.id.category_used);
            mCategoryDigitalView = view.findViewById(R.id.category_digital);
            mCategoryPreorderView = view.findViewById(R.id.category_preorder);

            mOlderNewPricesView = view.findViewById(R.id.older_new_prices);
            mOlderUsedPricesView = view.findViewById(R.id.older_used_prices);
            mOlderDigitalPricesView = view.findViewById(R.id.older_digital_prices);
            mOlderPreorderPricesView = view.findViewById(R.id.older_preorder_prices);
        }

    }

    public void setDataset(GamePreviewList dataset) {
        mGamePreviewList.clear();
        mGamePreviewList.addAll(dataset);
        notifyDataSetChanged();
    }

}
