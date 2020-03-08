package com.fermimn.gamewishlist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fermimn.gamewishlist.R
import com.fermimn.gamewishlist.custom_views.GamePreviewAdapter
import com.fermimn.gamewishlist.custom_views.GamePreviewRecyclerView
import com.fermimn.gamewishlist.utils.isNetworkAvailable
import com.fermimn.gamewishlist.viewmodels.SearchViewModel

class SearchFragmentNew : Fragment() {

    private lateinit var searchView: SearchView
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: GamePreviewRecyclerView

    private lateinit var viewModel: SearchViewModel

    // TODO: find a way to remove this variable
    private var firstStart: Boolean = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_search_new, container, false)

        searchView = view.findViewById(R.id.search_bar)
        progressBar = view.findViewById(R.id.progress_bar)
        recyclerView = view.findViewById(R.id.search_results)

        val fragmentActivity: FragmentActivity? = activity

        fragmentActivity?.let {

            viewModel = ViewModelProvider(it).get(SearchViewModel::class.java)

            viewModel.searchResults.observe(it, Observer { searchResults ->
                if ( searchResults.isNotEmpty() ) {
                    recyclerView.adapter?.notifyDataSetChanged()
                    recyclerView.scrollToPosition(0)
                } else {
                    if (!firstStart) {
                        Toast.makeText(it, getString(R.string.no_games_found), Toast.LENGTH_SHORT).show()
                    }
                    firstStart = false
                }
            })

            viewModel.isSearching.observe(it, Observer { isSearching ->
                if (isSearching) {
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE

                    // TODO: search view must lose the focus every time the user click
                    //       another part of the screen
                    recyclerView.requestFocus()
                }
            })

            searchView.setOnQueryTextListener(queryTextListener)

            recyclerView.adapter = GamePreviewAdapter(activity, viewModel.searchResults.value)
        }

        return view
    }

    private val queryTextListener: SearchView.OnQueryTextListener = object : SearchView.OnQueryTextListener {

        override fun onQueryTextSubmit(searchedGame: String): Boolean {

            context?.let {
                if (isNetworkAvailable(it)) {
                    viewModel.search(searchedGame)
                } else {
                    Toast.makeText(context, getString(R.string.toast_internet_not_available), Toast.LENGTH_SHORT).show()
                }
            }

            return false
        }

        override fun onQueryTextChange(s: String): Boolean = false

    }

}