package com.fermimn.gamewishlist.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.fermimn.gamewishlist.gamestop.GameStop
import com.fermimn.gamewishlist.models.GamePreviews
import kotlin.concurrent.thread

class SearchResultsViewModel(application: Application) : AndroidViewModel(application) {

    // TODO: use repository to retrieve games

    val searchResults: MutableLiveData<GamePreviews> by lazy {
        MutableLiveData<GamePreviews>()
    }

    val isUpdating: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun search(query: String) {
        isUpdating.value = true

        thread {
            val results = GameStop.search(query)
            searchResults.postValue(results)
            isUpdating.postValue(false)
        }
    }

}