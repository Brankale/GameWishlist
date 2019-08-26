package com.fermimn.gamewishlist.viewmodels;

import android.app.Application;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fermimn.gamewishlist.models.Game;
import com.fermimn.gamewishlist.models.GamePreview;
import com.fermimn.gamewishlist.models.GamePreviewList;
import com.fermimn.gamewishlist.repositories.WishListRepository;

public class WishListViewModel extends AndroidViewModel {

    @SuppressWarnings("unused")
    private static final String TAG = WishListViewModel.class.getSimpleName();

    private MutableLiveData<GamePreviewList> mWishlist;
    private MutableLiveData<Pair<GamePreview, Boolean>> mIsUpdating;
    private WishListRepository mRepository;

    public WishListViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        if (mWishlist != null) {
            return;
        }

        mIsUpdating = new MutableLiveData<>();
        mIsUpdating.setValue(new Pair<GamePreview, Boolean>(null, false));

        mRepository = WishListRepository.getInstance( getApplication() );
        mWishlist = mRepository.getWishList();
    }

    public LiveData<GamePreviewList> getWishlist() {
        return mWishlist;
    }

    public LiveData<Pair<GamePreview, Boolean>> isUpdating() {
        return mIsUpdating;
    }

    // TODO: images can't be downloaded if the user close the app
    public void addGame(final GamePreview gamePreview) {

        mIsUpdating.postValue(new Pair<GamePreview, Boolean>(gamePreview, true));

        new Thread() {

            @Override
            public void run() {

                Game game;

                if (!(gamePreview instanceof Game)) {
                    Log.d(TAG, "Downloading [" + gamePreview.getTitle() + "]");
                    game = mRepository.getGame(gamePreview);
                    Log.d(TAG, "Game downloaded [" + gamePreview.getTitle() + "]");
                } else {
                    game = (Game) gamePreview;
                }

                if (game != null) {
                    mRepository.addGame(game);
                    GamePreviewList gamePreviewList = mWishlist.getValue();
                    if (gamePreviewList != null) {
                        gamePreviewList.add(game);
                        mWishlist.postValue(gamePreviewList);
                        mIsUpdating.postValue(new Pair<GamePreview, Boolean>(gamePreview, false));
                        Log.d(TAG, "Game has been added to the list");
                    }
                }
            }

        }.start();
    }

    public void removeGame(final GamePreview gamePreview) {
        GamePreviewList gamePreviews = mWishlist.getValue();
        for (int i = 0; i < gamePreviews.size(); ++i) {
            if (gamePreviews.get(i).equals(gamePreview)) {
                gamePreviews.remove(i);
                mWishlist.postValue(gamePreviews);
                mRepository.removeGame(gamePreview);
                return;
            }
        }
    }

}
