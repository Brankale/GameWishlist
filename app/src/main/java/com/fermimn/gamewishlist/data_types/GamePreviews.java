package com.fermimn.gamewishlist.data_types;

import java.util.ArrayList;

// TODO: need documentation

public class GamePreviews extends ArrayList<GamePreview> {

    @Override
    public boolean add(GamePreview gamePreview) {

        if (gamePreview == null){
            return false;
        }

        // if the game is already present in the array
        if (super.contains(gamePreview)){
            return false;
        }

        return super.add(gamePreview);
    }

}
