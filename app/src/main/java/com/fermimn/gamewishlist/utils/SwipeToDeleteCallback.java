package com.fermimn.gamewishlist.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.adapters.GamePreviewListAdapter;
import com.fermimn.gamewishlist.models.GamePreview;
import com.fermimn.gamewishlist.models.GamePreviewList;
import com.fermimn.gamewishlist.viewmodels.WishlistViewModel;

// DOCS: https://medium.com/@zackcosborn/step-by-step-recyclerview-swipe-to-delete-and-undo-7bbae1fce27e
// DOCS: https://www.youtube.com/watch?v=M1XEqqo6Ktg
// DOCS: https://www.youtube.com/watch?v=eEonjkmox-0

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private WishlistViewModel mWishListViewModel;
    private GamePreviewListAdapter mAdapter;
    private Context mContext;
    private Drawable mIcon;
    private ColorDrawable mBackground;

     public SwipeToDeleteCallback(Context context, GamePreviewListAdapter adapter, WishlistViewModel wishListViewModel) {
         super(0, ItemTouchHelper.LEFT);
         mAdapter = adapter;
         mWishListViewModel = wishListViewModel;
         mContext = context;
     }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        GamePreview gamePreview = mAdapter.getGamePreviewByPosition(position);

        boolean add = true;

        GamePreviewList wishlist = mWishListViewModel.getWishlist().getValue();
        for (GamePreview game : wishlist) {
            if (game.equals(gamePreview)){
                add = false;
            }
        }

        if (add) {
            mWishListViewModel.addGame(gamePreview);
        } else {
            mWishListViewModel.removeGame(gamePreview.getId());
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

         int position = viewHolder.getAdapterPosition();
         GamePreview gamePreview = mAdapter.getGamePreviewByPosition(position);

         boolean add = true;

         GamePreviewList wishlist = mWishListViewModel.getWishlist().getValue();
         for (GamePreview game : wishlist) {
             if (game.equals(gamePreview)){
                 add = false;
             }
         }

         if (add) {
             mIcon = mContext.getDrawable(R.drawable.ic_add_black_24dp);
             mBackground = new ColorDrawable(Color.rgb(17, 156, 0));
         } else {
             mIcon = mContext.getDrawable(R.drawable.ic_delete_black_24dp);
             mBackground = new ColorDrawable(Color.RED);
         }

         super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
         View itemView = viewHolder.itemView;
         int iconMargin = (itemView.getHeight() - mIcon.getIntrinsicHeight()) / 2;

         if (dX > 0) {

             c.clipRect(
                     itemView.getLeft(),
                     itemView.getTop(),
                     (int) dX,
                     itemView.getBottom()
             );

             mBackground.setBounds(
                     itemView.getLeft(),
                     itemView.getTop(),
                     (int) dX,
                     itemView.getBottom()
             );

             mIcon.setBounds(
                     itemView.getLeft() + iconMargin,
                     itemView.getTop() + iconMargin,
                     itemView.getLeft() + iconMargin + mIcon.getIntrinsicWidth(),
                     itemView.getBottom() - iconMargin
             );

         } else if (dX < 0) {

             c.clipRect(
                     itemView.getRight() + (int) dX,
                     itemView.getTop(),
                     itemView.getRight(),
                     itemView.getBottom()
             );

             mBackground.setBounds(
                     itemView.getRight() + (int) dX,
                     itemView.getTop(),
                     itemView.getRight(),
                     itemView.getBottom()
             );

             mIcon.setBounds(
                     itemView.getRight() - iconMargin - mIcon.getIntrinsicWidth(),
                     itemView.getTop() + iconMargin,
                     itemView.getRight() - iconMargin,
                     itemView.getBottom() - iconMargin
             );

         } else {
             //mBackground.setBounds(0, 0, 0, 0);
         }

         mBackground.draw(c);
         mIcon.draw(c);
    }
}
