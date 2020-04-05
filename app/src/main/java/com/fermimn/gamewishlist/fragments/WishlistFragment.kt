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
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.fermimn.gamewishlist.R
import com.fermimn.gamewishlist.custom_views.GamePreviewAdapter
import com.fermimn.gamewishlist.custom_views.GamePreviewRecyclerView
import com.fermimn.gamewishlist.models.GamePreview
import com.fermimn.gamewishlist.models.GamePreviewDiffUtilCallback
import com.fermimn.gamewishlist.models.GamePreviews
import com.fermimn.gamewishlist.utils.isNetworkAvailable
import com.fermimn.gamewishlist.viewmodels.WishlistViewModel
import java.lang.Exception
import java.lang.ref.WeakReference

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

                if (wishlist.size == numItems+1) {
                    recyclerView.smoothScrollToPosition(adapter.itemCount-1)
                }

                Log.d(TAG, "wishlist updated")
            })

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
                        Toast.makeText(it, "Added: $title", Toast.LENGTH_SHORT).show()
                    }
                }
            })

            swipeRefreshLayout.setOnRefreshListener {
                if (isNetworkAvailable(it)) {
                    SwipeToRefresh(it, swipeRefreshLayout).execute()
                } else {
                    swipeRefreshLayout.isRefreshing = false
                    Toast.makeText(
                            it,
                            resources.getText(R.string.toast_internet_not_available),
                            Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }

        return view
    }

    // TODO: stop UpdateWorker while running this
    private class SwipeToRefresh(
            activity: FragmentActivity,
            swipeRefreshLayout: SwipeRefreshLayout
    ) : AsyncTask<Void?, Void?, Void?>() {

        companion object {
            private val TAG: String = SwipeToRefresh::class.java.simpleName
        }

        private val swipeRefreshLayout: WeakReference<SwipeRefreshLayout>
                = WeakReference(swipeRefreshLayout)
        private val viewModel: WishlistViewModel
                = ViewModelProvider(activity).get(WishlistViewModel::class.java)
        private val gamePreviews: GamePreviews

        init {
            gamePreviews = viewModel.wishlist.value ?: GamePreviews()
        }

        override fun doInBackground(vararg params: Void?): Void? {
            try {
                for (gamePreview in gamePreviews) {
                    Log.d(TAG, "updating game with id [${gamePreview.id}] ...")
                    viewModel.updateGame(gamePreview.id)
                }
            } catch (ex: Exception) {
                Log.e(TAG, "exception", ex)
            } finally {
                return null
            }
        }

        override fun onPostExecute(result: Void?) {
            swipeRefreshLayout.get()?.isRefreshing = false
        }

    }

}

