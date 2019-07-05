package com.fermimn.gamewishlist.utils;

import android.util.Log;

import com.fermimn.gamewishlist.data_types.GamePreview;

import java.util.List;

public class Gamestop implements Store {

    @Override
    public List<GamePreview> searchGame(String searchedGame) {
        // TODO: to implement
        Log.w("NOT IMPLEMENTED", "Gamestop.searchGame : not implemented");
        return null;
    }

    @Override
    public GamePreview downloadGame(int id) {
        // TODO: to implement
        Log.w("NOT IMPLEMENTED", "Gamestop.downloadGame : not implemented");
        return null;
    }

}
