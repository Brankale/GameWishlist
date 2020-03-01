package com.fermimn.gamewishlist.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fermimn.gamewishlist.models.GamePreviewList;
import com.fermimn.gamewishlist.utils.Gamestop;

public class SearchViewModel extends ViewModel {

    @SuppressWarnings("unused")
    private static final String TAG = SearchViewModel.class.getSimpleName();

    private MutableLiveData<GamePreviewList> mSearchResults;
    private MutableLiveData<Boolean> mIsSearching;

    public LiveData<GamePreviewList> getSearchResults() {
        if (mSearchResults == null) {
            mSearchResults = new MutableLiveData<>();
            mSearchResults.setValue(new GamePreviewList());
        }
        return mSearchResults;
    }

    public LiveData<Boolean> isSearching() {
        if (mIsSearching == null) {
            mIsSearching = new MutableLiveData<>();
            mIsSearching.setValue(false);
        }
        return mIsSearching;
    }

    public void search(final String gameTitle) {
        mIsSearching.setValue(true);

        new Thread() {
            @Override
            public void run() {
                Gamestop store = new Gamestop();
                GamePreviewList searchResults = mSearchResults.getValue();
                GamePreviewList newResults = store.searchGame(gameTitle);

                searchResults.clear();

                if (newResults != null) {
                    searchResults.addAll(newResults);
                }

                mSearchResults.postValue(searchResults);
                mIsSearching.postValue(false);
            }
        }.start();
    }

}
