package com.fermimn.gamewishlist.data_types;

import androidx.annotation.Nullable;

import java.util.ArrayList;

// TODO: need documentation

public class GamePreviewList extends ArrayList<GamePreview> {

    @SuppressWarnings("unused")
    private static final String TAG = GamePreviewList.class.getSimpleName();

    @Override
    public boolean add(GamePreview gamePreview) {

        if (gamePreview == null){
            return false;
        }

        // if the game is already present in the array
        if (contains(gamePreview)) {
            return false;
        }

        return super.add(gamePreview);
    }

    @Override
    public boolean contains(@Nullable Object o) {

        if (o == null) {
            return false;
        }

        if (o instanceof GamePreview) {
            for (GamePreview gamePreview : this) {
                if (gamePreview.equals(o)) {
                    return true;
                }
            }
        }

        return false;
    }
}
