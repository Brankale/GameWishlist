package com.fermimn.gamewishlist.utils;

import com.fermimn.gamewishlist.data_types.Game;
import com.fermimn.gamewishlist.data_types.GamePreviewList;

public interface Store {

    GamePreviewList searchGame(String game);

    Game downloadGame(String id);

}
