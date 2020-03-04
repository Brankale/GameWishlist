package com.fermimn.gamewishlist.models;

import androidx.annotation.Nullable;

import java.util.ArrayList;

// TODO: need documentation
// TODO: there are methods that haven't been overridden

@Deprecated
public class GamePreviewList extends ArrayList<GamePreview> {

    @SuppressWarnings("unused")
    private static final String TAG = GamePreviewList.class.getSimpleName();

    public GamePreviewList() {
        super();
    }

    public GamePreviewList(ArrayList<GamePreview> gameList) {
        super();
        if (gameList != null) {
            for (GamePreview gamePreview : gameList) {
                add(gamePreview);
            }
        }
    }

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
    public boolean remove(@Nullable Object o) {

        if (o == null) {
            return false;
        }

        if (o instanceof GamePreview) {
            GamePreview gamePreview = (GamePreview) o;
            for (int i = 0; i < this.size(); ++i) {
                if (this.get(i).equals(gamePreview)) {
                    super.remove(i);
                    return true;
                }
            }
        }

        return false;
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
