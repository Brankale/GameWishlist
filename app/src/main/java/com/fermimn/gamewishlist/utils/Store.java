package com.fermimn.gamewishlist.utils;

import com.fermimn.gamewishlist.models.Game;
import com.fermimn.gamewishlist.models.GamePreviewList;

public interface Store {

    GamePreviewList searchGame(String game);

    Game downloadGame(String id);

}
