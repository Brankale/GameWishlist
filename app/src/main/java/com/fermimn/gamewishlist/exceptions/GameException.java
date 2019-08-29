package com.fermimn.gamewishlist.exceptions;

import androidx.annotation.NonNull;

public class GameException extends RuntimeException {

    @NonNull
    public String toString() {
        return "Game creation failed";
    }

}
