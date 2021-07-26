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
import androidx.lifecycle.ViewModelProvider
import com.fermimn.gamewishlist.R
import com.fermimn.gamewishlist.custom_views.GamePreviewAdapter
import com.fermimn.gamewishlist.custom_views.GamePreviewRecyclerView
import com.fermimn.gamewishlist.databinding.FragmentSearchBinding
import com.fermimn.gamewishlist.utils.isNetworkAvailable
import com.fermimn.gamewishlist.viewmodels.SearchViewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SearchViewModel

    // TODO: find a way to remove this variable
    private var firstStart: Boolean = true

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root

        val fragmentActivity: FragmentActivity? = activity

        fragmentActivity?.let {

            viewModel = ViewModelProvider(it).get(SearchViewModel::class.java)

            viewModel.searchResults.observe(it, { searchResults ->
                if ( searchResults.isNotEmpty() ) {
                    binding.searchResults.adapter?.notifyDataSetChanged()
                    binding.searchResults.scrollToPosition(0)
                } else {
                    if (!firstStart) {
                        Toast.makeText(it, getString(R.string.no_games_found), Toast.LENGTH_SHORT).show()
                    }
                    firstStart = false
                }
            })

            viewModel.isSearching.observe(it, { isSearching ->
                if (isSearching) {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.searchResults.visibility = View.GONE
                } else {
                    binding.progressBar.visibility = View.GONE
                    binding.searchResults.visibility = View.VISIBLE

                    // TODO: search view must lose the focus every time the user click
                    //       another part of the screen
                    binding.searchResults.requestFocus()
                }

            })

            binding.searchBar.setOnQueryTextListener(queryTextListener)

            binding.searchResults.adapter = GamePreviewAdapter(activity, viewModel.searchResults.value)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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