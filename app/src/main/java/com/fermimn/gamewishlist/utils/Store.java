package com.fermimn.gamewishlist.utils;

import com.fermimn.gamewishlist.data_types.Game;
import com.fermimn.gamewishlist.data_types.GamePreviewList;

import java.io.IOException;

// TODO: use more significant interface name
public interface Store {

    GamePreviewList searchGame(String game) throws IOException;
    Game downloadGame(String id);

}
