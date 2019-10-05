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
import com.fermimn.gamewishlist.viewmodels.WishListViewModel;

// DOCS: https://medium.com/@zackcosborn/step-by-step-recyclerview-swipe-to-delete-and-undo-7bbae1fce27e
// DOCS: https://www.youtube.com/watch?v=M1XEqqo6Ktg
// DOCS: https://www.youtube.com/watch?v=eEonjkmox-0

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private WishListViewModel mWishListViewModel;
    private GamePreviewListAdapter mAdapter;
    private Drawable mIcon;
    private final ColorDrawable mBackground;

     public SwipeToDeleteCallback(Context context, GamePreviewListAdapter adapter, WishListViewModel wishListViewModel) {
         super(0, ItemTouchHelper.LEFT);
         mAdapter = adapter;
         mIcon = context.getDrawable(R.drawable.ic_delete_black_24dp);
         mBackground = new ColorDrawable(Color.RED);
         mWishListViewModel = wishListViewModel;
     }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        GamePreview gamePreview = mAdapter.getGamePreviewByPosition(position);
        mWishListViewModel.removeGame(gamePreview);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

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

         } else {

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

         }

         mBackground.draw(c);
         mIcon.draw(c);
    }
}
