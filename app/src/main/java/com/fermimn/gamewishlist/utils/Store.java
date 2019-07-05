package com.fermimn.gamewishlist.utils;

import android.provider.ContactsContract;

import com.fermimn.gamewishlist.data_types.GamePreview;
import com.fermimn.gamewishlist.data_types.GamePreviews;

import java.io.IOException;
import java.util.List;

// TODO: use a more significant interface name
public interface Store {

    public GamePreviews searchGame(String game) throws IOException;
    public GamePreview downloadGame(String id);

}
