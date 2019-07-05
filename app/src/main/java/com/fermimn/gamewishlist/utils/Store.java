package com.fermimn.gamewishlist.utils;

import com.fermimn.gamewishlist.data_types.GamePreview;

import java.util.List;

// TODO: use a more significant interface name
public interface Store {

    public List<GamePreview> searchGame(String game);
    public GamePreview downloadGame(int id);

}
