package com.fermimn.gamewishlist.fragments

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.fermimn.gamewishlist.App
import com.fermimn.gamewishlist.R
import com.fermimn.gamewishlist.custom_views.GamePreviewAdapter
import com.fermimn.gamewishlist.custom_views.GamePreviewRecyclerView
import com.fermimn.gamewishlist.models.Game
import com.fermimn.gamewishlist.models.GamePreview
import com.fermimn.gamewishlist.models.GamePreviewDiffUtilCallback
import com.fermimn.gamewishlist.models.GamePreviews
import com.fermimn.gamewishlist.viewmodels.WishlistViewModel

class WishlistFragment : Fragment() {

    companion object {
        private val TAG: String = WishlistFragment::class.java.simpleName
    }

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: GamePreviewRecyclerView
    private lateinit var adapter: GamePreviewAdapter

    private lateinit var viewModel: WishlistViewModel
    private val wishlist: GamePreviews = GamePreviews()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_wishlist, container, false)

        swipeRefreshLayout = view.findViewById(R.id.swipe_to_refresh)
        recyclerView = view.findViewById(R.id.wishlist)

        activity?.let {
            viewModel = ViewModelProvider(it).get(WishlistViewModel::class.java)
            val gameList = viewModel.wishlist.value
            gameList?.let {
                wishlist.addAll(gameList)
            }

            adapter = GamePreviewAdapter(it, wishlist)
            recyclerView.adapter = adapter

            // update wishlist when the user add/remove a game
            viewModel.wishlist.observe(it, Observer { newItems ->

                val numItems = wishlist.size

                val diffResult = DiffUtil.calculateDiff(GamePreviewDiffUtilCallback(wishlist, newItems))

                wishlist.clear()
                wishlist.addAll(newItems)

                diffResult.dispatchUpdatesTo(adapter)

                if (wishlist.size > numItems) {
                    recyclerView.smoothScrollToPosition(adapter.itemCount-1)
                }

                Log.d(TAG, "wishlist updated")
            })

            // TODO: I don't like this
            // show toast while updating
            viewModel.isUpdating.observe(it, Observer<Pair<GamePreview?, Boolean>> { updatedGame ->

                Log.d(TAG, "updating wishlist...")

                if (updatedGame.first != null) {
                    val title: String = updatedGame.first?.title ?: ""

                    if (updatedGame.second) {
                        // TODO: remove hard-coded text
                        Toast.makeText(it, "Downloading: $title", Toast.LENGTH_SHORT).show()
                    } else {
                        // TODO: remove hard-coded text
                        Toast.makeText(it, "Aggiunto: $title", Toast.LENGTH_SHORT).show()
                    }
                }
            })

            swipeRefreshLayout.setOnRefreshListener {
                Update().execute()
            }

        }

        return view
    }

    // TODO: remove this thing as soon as possible
    // This class is responsible for crashes during updates
    private inner class Update : AsyncTask<GamePreviews?, Int?, Boolean?>() {

        override fun doInBackground(vararg gamePreviewLists: GamePreviews?): Boolean? {
            for (i in wishlist.indices) {
                val prev = wishlist[i] as Game
                viewModel.updateGame(wishlist[i].id)
                val current = wishlist[i] as Game
                if (current != null) {
                    var priceChanges = 0
                    val text = StringBuilder()

                    // the game has been released
                    if (current.newPrice != null && prev.preorderPrice != null) {
                        text.append(getString(R.string.notif_game_released))
                    }

                    // lower new price
                    if (current.newPrice != null && prev.newPrice != null) {
                        if (current.newPrice!! < prev.newPrice!!) {
                            text.append(getString(R.string.notif_lower_new_price))
                            priceChanges++
                        }
                    }

                    // lower used price
                    if (current.usedPrice != null && prev.usedPrice != null) {
                        if (current.usedPrice!! < prev.usedPrice!!) {
                            text.append(getString(R.string.notif_lower_used_price))
                            priceChanges++
                        }
                    }

                    // lower digital price
                    if (current.digitalPrice != null && prev.digitalPrice != null) {
                        if (current.digitalPrice!! < prev.digitalPrice!!) {
                            text.append(getString(R.string.notif_lower_digital_price))
                            priceChanges++
                        }
                    }

                    // lower preorder price
                    if (current.preorderPrice != null && prev.preorderPrice != null) {
                        if (current.preorderPrice!! < prev.preorderPrice!!) {
                            text.append(getString(R.string.notif_lower_preorder_price))
                            priceChanges++
                        }
                    }
                    if (text.isNotEmpty()) {
                        text.deleteCharAt(text.length - 1)
                        if (priceChanges == 1) {
                            App.sendOnUpdatesChannel(context, current, text.toString(), null)
                        } else {
                            App.sendOnUpdatesChannel(context, current, getString(R.string.notif_lower_prices), text.toString())
                        }
                    }
                }
            }
            return false
        }

        override fun onPostExecute(refreshing: Boolean?) {
            swipeRefreshLayout.isRefreshing = refreshing ?: false
        }

    }

}

