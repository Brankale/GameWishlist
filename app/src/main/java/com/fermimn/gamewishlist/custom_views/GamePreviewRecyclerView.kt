package com.fermimn.gamewishlist.custom_views

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fermimn.gamewishlist.R
import com.fermimn.gamewishlist.activities.GamePageActivity
import com.fermimn.gamewishlist.models.GamePreview
import com.fermimn.gamewishlist.viewmodels.WishlistViewModel


class GamePreviewRecyclerView(context: Context, attrs: AttributeSet?)
    : RecyclerView(context, attrs) {

    companion object {
        @Suppress("unused")
        private val TAG: String = GamePreviewRecyclerView::class.java.simpleName
    }

    init {
        setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(context)
        setLayoutManager(layoutManager)

        val dividerItemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        addItemDecoration(dividerItemDecoration)
    }

}

class GamePreviewAdapter(val context: FragmentActivity?, val gamePreviews: List<com.github.brankale.models.Game>?)
    : RecyclerView.Adapter<GamePreviewAdapter.GameViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val item = GamePreviewView(parent.context, null)
        return GameViewHolder(item)
    }

    override fun getItemCount(): Int = gamePreviews?.size ?: 0

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        gamePreviews?.let {
            holder.bind(it[position])
        }
    }

    override fun getItemId(position: Int): Long {
        gamePreviews?.let {
            return it[position].id.toLong()
        }
        return -1
    }

    inner class GameViewHolder(private val view: GamePreviewView):
            RecyclerView.ViewHolder(view),
            View.OnClickListener,
            View.OnLongClickListener {

        fun bind(gamePreview : com.github.brankale.models.Game) {
            view.bind(gamePreview)
            view.setOnClickListener(this)
            view.setOnLongClickListener(this)
        }

        override fun onClick(view: View?) {
            gamePreviews?.let {
                val intent = Intent(context, GamePageActivity::class.java)
                intent.putExtra("gameID", it[adapterPosition].id)
                context?.startActivity(intent)
            }
        }

        override fun onLongClick(v: View?): Boolean {
            // do nothing
            return true
        }

//        override fun onLongClick(view: View?): Boolean {
//            if (context != null && gamePreviews != null) {
//                val wishlistViewModel = ViewModelProvider(context).get(WishlistViewModel::class.java)
//                val wishlist = wishlistViewModel.wishlist.value
//
//                if (wishlist != null) {
//                    if (wishlist.contains(gamePreviews[adapterPosition])) {
//                        AlertDialog.Builder(context)
//                                .setTitle(context.getString(R.string.dialog_remove_game_from_wishlist_title))
//                                .setMessage(context.getString(R.string.dialog_remove_game_from_wishlist_text))
//                                // Specifying a listener allows you to take an action before dismissing the dialog.
//                                // The dialog is automatically dismissed when a dialog button is clicked.
//                                .setPositiveButton(android.R.string.yes) { _, _ ->
//                                    wishlistViewModel.removeGame(gamePreviews[adapterPosition].id)
//                                }
//                                // A null listener allows the button to dismiss the dialog and take no further action
//                                .setNegativeButton(android.R.string.no, null)
//                                .show()
//                    } else {
//                        AlertDialog.Builder(context)
//                                .setTitle(context.getString(R.string.dialog_add_game_to_wishlist_title))
//                                .setMessage(context.getString(R.string.dialog_add_game_to_wishlist_text))
//                                // Specifying a listener allows you to take an action before dismissing the dialog.
//                                // The dialog is automatically dismissed when a dialog button is clicked.
//                                .setPositiveButton(android.R.string.yes) { _, _ ->
//                                    wishlistViewModel.addGame(gamePreviews[adapterPosition])
//                                }
//                                // A null listener allows the button to dismiss the dialog and take no further action
//                                .setNegativeButton(android.R.string.no, null)
//                                .show()
//                    }
//                }
//            }
//
//            return true
//        }

    }

}

