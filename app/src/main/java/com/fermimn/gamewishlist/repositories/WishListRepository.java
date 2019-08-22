package com.fermimn.gamewishlist.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.fermimn.gamewishlist.data_types.GamePreview;
import com.fermimn.gamewishlist.data_types.GamePreviewList;

// TODO: find a way to join WishListManager with WishListRepository
public class WishListRepository {

    @SuppressWarnings("unused")
    private static final String TAG = WishListRepository.class.getSimpleName();

    private static WishListRepository mInstance;
    private final GamePreviewList mWishList;
    private MutableLiveData<GamePreviewList> mData;

    private WishListRepository() {
        mWishList = new GamePreviewList();
    }

    public static WishListRepository getInstance() {
        if (mInstance == null) {
            mInstance = new WishListRepository();
        }
        return mInstance;
    }

    public MutableLiveData<GamePreviewList> getWishList() {
        mData = new MutableLiveData<>();
        mData.setValue(mWishList);
        return mData;
    }

    // TODO: it must be a Game
    public void add(GamePreview gamePreview) {
        mWishList.add(gamePreview);
        mData.setValue(mWishList);
    }

    // TODO: it must be a Game
    public void remove(GamePreview gamePreview) {
        mWishList.remove(gamePreview);
        mData.setValue(mWishList);
    }

}
