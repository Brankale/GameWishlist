package com.fermimn.gamewishlist.utils;

import com.fermimn.gamewishlist.data_types.Game;
import com.fermimn.gamewishlist.data_types.GamePreviewList;

import java.io.IOException;

// TODO: use a more significant interface name
public interface Store {

    public GamePreviewList searchGame(String game) throws IOException;
    public Game downloadGame(String id);

}
