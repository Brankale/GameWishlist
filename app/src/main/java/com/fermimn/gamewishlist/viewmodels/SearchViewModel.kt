package com.fermimn.gamewishlist.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fermimn.gamewishlist.gamestop.GameStop
import com.fermimn.gamewishlist.models.GamePreviews
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val _searchResults = MutableLiveData<GamePreviews>()
    val searchResults: LiveData<GamePreviews> = _searchResults

    private val _isSearching = MutableLiveData<Boolean>()
    val isSearching: LiveData<Boolean> = _isSearching

    fun search(game: String) {
        _isSearching.value = true
        viewModelScope.launch(Dispatchers.IO) {
            _searchResults.postValue(GameStop.search(game))
        }
        _isSearching.value = false
    }

}