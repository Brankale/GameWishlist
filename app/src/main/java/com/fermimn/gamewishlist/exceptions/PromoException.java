package com.fermimn.gamewishlist.exceptions;

import androidx.annotation.NonNull;

public class PromoException extends RuntimeException {

    @NonNull
    public String toString() {
        return "Promo creation failed";
    }

}
