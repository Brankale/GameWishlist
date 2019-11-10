package com.fermimn.gamewishlist.models;

import androidx.annotation.NonNull;

import com.fermimn.gamewishlist.exceptions.PromoException;

public class Promo {

    private final String mHeader;
    private String mSubHeader;
    private String mFindMoreMsg;
    private String mFindMoreUrl;

    /**
     * Constructor of Promo
     * @param header a string with a promotional message
     */
    public Promo(String header) {
        if (header != null && !header.isEmpty()) {
            mHeader = header;
        } else {
            throw new PromoException();
        }
    }

    /**
     * @return the promotional message
     */
    public String getHeader() {
        return mHeader;
    }

    /**
     * Add more info to the promotional message
     * @param subHeader a message that adds more info to the promotional message
     */
    public void setSubHeader(String subHeader) {
        mSubHeader = subHeader;
    }

    /**
     * @return more info about the promotional message
     */
    public String getSubHeader() {
        return mSubHeader;
    }

    /**
     * Add a message inviting the user to discover the promotion
     * @param message a message inviting the user to discover the promotion
     * @param url of the message
     */
    public void setFindMoreMsg(String message, String url) {
        mFindMoreMsg = message;
        mFindMoreUrl = url;
    }

    /**
     * @return a message inviting the user to discover the promotion
     */
    public String getFindMoreMsg() {
        return mFindMoreMsg;
    }

    /**
     * @return the url associated with the "Find More" message
     */
    public String getFindMoreUrl() {
        return mFindMoreUrl;
    }

    /**
     * @return true if the Promo has a "Find More" message
     */
    public boolean hasFindMoreMsg() {
        return mFindMoreMsg != null && !mFindMoreMsg.isEmpty();
    }

    /**
     * @return a string with all the info about the Promo
     */
    @NonNull
    @Override
    public String toString() {
        return "Promo {" +
                "header='" + mHeader + '\'' +
                ", sub header='" + mSubHeader + '\'' +
                ", find more='" + mFindMoreMsg + '\'' +
                ", find more url='" + mFindMoreUrl + '\'' +
                '}';
    }
}
