package com.fermimn.gamewishlist.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fermimn.gamewishlist.repos.GameStopRepository
import com.github.brankale.models.Game
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val gameStopRepository: GameStopRepository
): ViewModel() {

    private val _searchResults = MutableLiveData<List<Game>>()
    val searchResults: LiveData<List<Game>> = _searchResults

    private val _isSearching = MutableLiveData<Boolean>()
    val isSearching: LiveData<Boolean> = _isSearching

    fun search(game: String) {
        _isSearching.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val results = gameStopRepository.search(game)
            _searchResults.postValue(results)
        }
        _isSearching.value = false
    }

}