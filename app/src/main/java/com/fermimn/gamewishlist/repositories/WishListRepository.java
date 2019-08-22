package com.fermimn.gamewishlist.repositories;

import androidx.lifecycle.MutableLiveData;

import com.fermimn.gamewishlist.data_types.GamePreview;
import com.fermimn.gamewishlist.data_types.GamePreviewList;

// TODO: find a way to join WishListrManager with WishListRepository
public class WishListRepository {

    private static WishListRepository mInstance;
    private final GamePreviewList mWishList;

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
        MutableLiveData<GamePreviewList> data = new MutableLiveData<>();
        data.setValue(mWishList);
        return data;
    }

    // TODO: it must be a Game
    public void add(GamePreview gamePreview) {
        mWishList.add(gamePreview);
    }

    // TODO: it must be a Game
    // TODO: implement remove in GamePreviewList
    public void remove(GamePreview gamePreview) {
        for (int i = 0; i < mWishList.size(); ++i) {
            if (mWishList.get(i).equals(gamePreview)) {
                mWishList.remove(i);
                return;
            }
        }
    }

}
