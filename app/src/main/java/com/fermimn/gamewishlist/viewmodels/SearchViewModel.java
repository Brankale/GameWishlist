package com.fermimn.gamewishlist.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fermimn.gamewishlist.models.GamePreview;
import com.fermimn.gamewishlist.models.GamePreviewList;
import com.fermimn.gamewishlist.utils.Gamestop;

import java.util.ArrayList;

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
                GamePreviewList previousResults = mSearchResults.getValue();

                ArrayList<GamePreview> results = Gamestop.Companion.searchGame(gameTitle);

                if (previousResults != null) {
                    previousResults.clear();
                }

                if (previousResults != null && results != null) {
                    previousResults.addAll(results);
                }

                mSearchResults.postValue(previousResults);
                mIsSearching.postValue(false);
            }
        }.start();
    }

}
