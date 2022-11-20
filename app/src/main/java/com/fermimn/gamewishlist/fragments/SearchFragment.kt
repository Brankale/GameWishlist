package com.fermimn.gamewishlist.fragments

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.fermimn.gamewishlist.R
import com.fermimn.gamewishlist.components.BaseFragment
import com.fermimn.gamewishlist.custom_views.GamePreviewAdapter
import com.fermimn.gamewishlist.databinding.FragmentSearchBinding
import com.fermimn.gamewishlist.utils.isNetworkAvailable
import com.fermimn.gamewishlist.viewmodels.SearchViewModel
import com.fermimn.gamewishlist.viewmodels.WishlistViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    // TODO: find a way to remove this variable
    private var firstStart: Boolean = true

    override fun getFragmentView(): Int = R.layout.fragment_search

    private val viewModel: SearchViewModel by lazy {
        ViewModelProvider(this)[SearchViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        binding.searchBar.setOnQueryTextListener(queryTextListener)
        binding.searchResults.adapter = GamePreviewAdapter(activity, viewModel.searchResults.value)
    }

    private fun setupObservers() {
        viewModel.searchResults.observe(viewLifecycleOwner) { results ->
            if (results.isNotEmpty()) {
                binding.searchResults.adapter?.notifyDataSetChanged()
                binding.searchResults.scrollToPosition(0)
            } else {
                if (!firstStart) {
                    Toast.makeText(context, getString(R.string.no_games_found), Toast.LENGTH_SHORT).show()
                    firstStart = false
                }
            }
        }

        viewModel.isSearching.observe(viewLifecycleOwner) { isSearching ->
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
        }
    }

    private val queryTextListener: SearchView.OnQueryTextListener = object : SearchView.OnQueryTextListener {

        override fun onQueryTextSubmit(searchedGame: String): Boolean {
            if (isNetworkAvailable(requireContext()))
                viewModel.search(searchedGame)
            else
                Toast.makeText(context, getString(R.string.toast_internet_not_available), Toast.LENGTH_SHORT).show()

            return false
        }

        override fun onQueryTextChange(s: String): Boolean = false

    }

}