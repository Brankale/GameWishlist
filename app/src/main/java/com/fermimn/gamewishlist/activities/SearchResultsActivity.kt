package com.fermimn.gamewishlist.activities

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fermimn.gamewishlist.R
import com.fermimn.gamewishlist.custom_views.GamePreviewAdapter
import com.fermimn.gamewishlist.custom_views.GamePreviewRecyclerView
import com.fermimn.gamewishlist.models.GamePreviews
import com.fermimn.gamewishlist.provider.SuggestionProvider
import com.fermimn.gamewishlist.viewmodels.SearchResultsViewModel

class SearchResultsActivity : AppCompatActivity() {

    companion object {
        private val TAG: String = SearchResultsActivity::class.java.simpleName
    }

    private lateinit var viewModel: SearchResultsViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: GamePreviewRecyclerView
    private lateinit var adapter: GamePreviewAdapter
    private val searchResults = GamePreviews()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this).get(SearchResultsViewModel::class.java)

        progressBar = findViewById(R.id.progress_bar)
        recyclerView = findViewById(R.id.search_results)

        adapter = GamePreviewAdapter(this, searchResults)
        recyclerView.adapter = adapter

        viewModel.searchResults.observe(this, Observer {
            searchResults.clear()
            searchResults.addAll(it)
            adapter.notifyDataSetChanged()
        })

        viewModel.isUpdating.observe(this, Observer {
            if (it) {
                recyclerView.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        })

        intent.getStringExtra(SearchManager.QUERY)?.also { query ->
            SearchRecentSuggestions(this, SuggestionProvider.AUTHORITY, SuggestionProvider.MODE)
                    .saveRecentQuery(query, null)
            Log.d(TAG, "Saved query")
        }

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)

            if (query != null) {
                viewModel.search(query)
            }
        }
    }

}