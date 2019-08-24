package com.fermimn.gamewishlist.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fermimn.gamewishlist.models.GamePreviewList;
import com.fermimn.gamewishlist.repositories.WishListRepository;

public class WishListViewModel extends ViewModel {

    private MutableLiveData<GamePreviewList> mWishlist;

    public void init() {
        if (mWishlist != null) {
            return;
        }

        WishListRepository repository = WishListRepository.getInstance();
        mWishlist = repository.getWishList();
    }

    public LiveData<GamePreviewList> getWishlist() {
        return mWishlist;
    }

}
