package com.fermimn.gamewishlist.viewmodels;

import android.app.Application;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fermimn.gamewishlist.models.Game;
import com.fermimn.gamewishlist.models.GamePreview;
import com.fermimn.gamewishlist.models.GamePreviewList;
import com.fermimn.gamewishlist.repositories.Repository;
import com.fermimn.gamewishlist.utils.Gamestop;
import com.fermimn.gamewishlist.utils.Store;

public class WishlistViewModel extends AndroidViewModel {

    @SuppressWarnings("unused")
    private static final String TAG = WishlistViewModel.class.getSimpleName();

    private final Repository mRepository;
    private final MutableLiveData<Pair<GamePreview, Boolean>> mIsUpdating;

    public WishlistViewModel(@NonNull Application application) {
        super(application);
        mRepository = Repository.getInstance( getApplication() );
        mIsUpdating = new MutableLiveData<>();
        mIsUpdating.setValue(new Pair<GamePreview, Boolean>(null, false));
    }

    public LiveData<GamePreviewList> getWishlist() {
        return mRepository.getWishlist();
    }

    public LiveData<Pair<GamePreview, Boolean>> isUpdating() {
        return mIsUpdating;
    }

    public Game getGame(@Nullable String gameId) {
        return mRepository.getGame(gameId);
    }

    public void addGame(@Nullable final GamePreview gamePreview) {

        if (gamePreview != null) {
            mIsUpdating.postValue(new Pair<>(gamePreview, true));

            new Thread() {
                public void run() {
                    Store store = new Gamestop();
                    Game game = store.downloadGame(gamePreview.getId());
                    addGame(game);
                }
            }.start();
        }
    }

    public void addGame(@Nullable Game game) {
        mRepository.addGame(game);
        mIsUpdating.postValue(new Pair<>((GamePreview) game, false));
    }

    public Game updateGame(final String gameId) {
        return mRepository.updateGame(gameId);
    }

    public void removeGame(@Nullable String gameId) {
        mRepository.removeGame(gameId);
    }

}
