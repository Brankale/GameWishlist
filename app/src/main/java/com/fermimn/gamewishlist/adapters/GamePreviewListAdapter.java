package com.fermimn.gamewishlist.adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.activities.GamePageActivity;
import com.fermimn.gamewishlist.models.GamePreview;
import com.fermimn.gamewishlist.models.GamePreviewList;
import com.fermimn.gamewishlist.viewmodels.WishListViewModel;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

// To increase performance of the Recycler see this guide
// DOCS: https://android.jlelse.eu/recyclerview-optimisations-a4b141dd433d

public class GamePreviewListAdapter extends RecyclerView.Adapter<GamePreviewListAdapter.ViewHolder> {

    @SuppressWarnings("unused")
    private static final String TAG = GamePreviewListAdapter.class.getSimpleName();

    private final GamePreviewList mGamePreviewList;
    private final FragmentActivity mContext;
    private final DecimalFormat mDecimalFormat;
    private final String mCurrency;

    public GamePreviewListAdapter(FragmentActivity context, GamePreviewList gamePreviewList) {
        mGamePreviewList = gamePreviewList;
        mContext = context;
        mDecimalFormat = new DecimalFormat("#.00");
        mCurrency = mContext.getString(R.string.currency);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "OnCreateViewHolder called");
        LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
        View view = inflater.inflate(R.layout.partial_game_preview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        GamePreview gamePreview = mGamePreviewList.get(position);

        holder.mTitleView.setText( gamePreview.getTitle() );
        holder.mPlatformView.setText( gamePreview.getPlatform() );
        holder.mPublisherView.setText( gamePreview.getPublisher() );

        // new prices
        if (gamePreview.getNewPrice() == null) {
            holder.mCategoryNewView.setVisibility(View.GONE);
        } else {
            holder.mNewPriceView.setText( mDecimalFormat.format( gamePreview.getNewPrice() ));
            holder.mNewPriceView.append(mCurrency);

            if (gamePreview.getOlderNewPrices() == null) {
                holder.mOlderNewPricesView.setVisibility(View.GONE);
            } else {
                holder.mOlderNewPricesView
                        .setText( mDecimalFormat.format( gamePreview.getOlderNewPrices().get(0) ));
                holder.mOlderNewPricesView.append(mCurrency);
                holder.mOlderNewPricesView.setVisibility(View.VISIBLE);
            }

            holder.mCategoryNewView.setVisibility(View.VISIBLE);
        }

        // used prices
        if (gamePreview.getUsedPrice() == null) {
            holder.mCategoryUsedView.setVisibility(View.GONE);
        } else {
            holder.mUsedPriceView.setText( mDecimalFormat.format( gamePreview.getUsedPrice() ));
            holder.mUsedPriceView.append(mCurrency);

            if (gamePreview.getOlderUsedPrices() == null) {
                holder.mOlderUsedPricesView.setVisibility(View.GONE);
            } else {
                holder.mOlderUsedPricesView
                        .setText( mDecimalFormat.format( gamePreview.getOlderUsedPrices().get(0) ));
                holder.mOlderUsedPricesView.append(mCurrency);
                holder.mOlderUsedPricesView.setVisibility(View.VISIBLE);
            }

            holder.mCategoryUsedView.setVisibility(View.VISIBLE);
        }

        // digital prices
        if (gamePreview.getDigitalPrice() == null) {
            holder.mCategoryDigitalView.setVisibility(View.GONE);
        } else {
            holder.mDigitalPriceView
                    .setText(mDecimalFormat.format( gamePreview.getDigitalPrice() ));
            holder.mDigitalPriceView.append(mCurrency);

            if (gamePreview.getOlderDigitalPrices() == null) {
                holder.mOlderDigitalPricesView.setVisibility(View.GONE);
            } else {
                holder.mOlderDigitalPricesView
                        .setText(mDecimalFormat.format( gamePreview.getOlderDigitalPrices().get(0) ));
                holder.mOlderDigitalPricesView.append(mCurrency);
                holder.mOlderDigitalPricesView.setVisibility(View.VISIBLE);
            }

            holder.mCategoryDigitalView.setVisibility(View.VISIBLE);
        }

        // preorder prices
        if (gamePreview.getPreorderPrice() == null) {
            holder.mCategoryPreorderView.setVisibility(View.GONE);
        } else {
            holder.mPreorderPriceView
                    .setText(mDecimalFormat.format( gamePreview.getPreorderPrice() ));
            holder.mPreorderPriceView.append(mCurrency);

            if (gamePreview.getOlderPreorderPrices() == null) {
                holder.mOlderPreorderPricesView.setVisibility(View.GONE);
            } else {
                holder.mOlderPreorderPricesView
                        .setText(mDecimalFormat.format( gamePreview.getOlderPreorderPrices().get(0) ));
                holder.mOlderPreorderPricesView.append(mCurrency);
                holder.mOlderPreorderPricesView.setVisibility(View.VISIBLE);
            }

            holder.mCategoryPreorderView.setVisibility(View.VISIBLE);
        }

        Picasso.get().load( gamePreview.getCover() ).into(holder.mCoverView);
    }

    @Override
    public int getItemCount() {
        return mGamePreviewList.size();
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong( mGamePreviewList.get(position).getId() );
    }

    public GamePreview getGamePreviewByPosition(int position) {
        // TODO: sometimes this line cause an IndexOutOfBoundsException
        //       because someone requires position -1
        return mGamePreviewList.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

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

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);

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

            mOlderNewPricesView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            mOlderUsedPricesView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            mOlderDigitalPricesView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            mOlderPreorderPricesView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }

        @Override
        public void onClick(View view) {
            GamePreview gamePreview = mGamePreviewList.get( getAdapterPosition() );
            Intent intent = new Intent(mContext, GamePageActivity.class);
            intent.putExtra("gameID", gamePreview.getId());
            mContext.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View view) {

            final WishListViewModel wishListViewModel =
                    ViewModelProviders.of(mContext).get(WishListViewModel.class);

            wishListViewModel.init();

            GamePreviewList gamePreviewList = wishListViewModel.getWishlist().getValue();
            boolean result = gamePreviewList.contains( mGamePreviewList.get( getAdapterPosition() ) );

            if (result) {

                new AlertDialog.Builder(mContext)
                        .setTitle( mContext.getString(R.string.dialog_remove_game_from_wishlist_title) )
                        .setMessage( mContext.getString(R.string.dialog_remove_game_from_wishlist_text) )

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                GamePreview gamePreview = mGamePreviewList.get( getAdapterPosition() );
                                wishListViewModel.removeGame(gamePreview);
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
                                GamePreview gamePreview = mGamePreviewList.get( getAdapterPosition() );
                                wishListViewModel.addGame(gamePreview);
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }

            return true;
        }
    }

}
