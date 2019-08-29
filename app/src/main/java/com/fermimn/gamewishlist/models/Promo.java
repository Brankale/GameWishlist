package com.fermimn.gamewishlist.models;

import androidx.annotation.NonNull;

import com.fermimn.gamewishlist.exceptions.PromoException;

public class Promo {

    private final String mHeader;
    private String mSubHeader;
    private String mFindMoreMessage;
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
    public void setFindMoreMessage(String message, String url) {
        mFindMoreMessage = message;
        mFindMoreUrl = url;
    }

    /**
     * @return a message inviting the user to discover the promotion
     */
    public String getFindMoreMessage() {
        return mFindMoreMessage;
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
    public boolean hasFindMoreMessage() {
        return mFindMoreMessage != null && !mFindMoreMessage.isEmpty();
    }

    /**
     * @return a string with all the info abbout the Promo
     */
    @NonNull
    @Override
    public String toString() {
        return "Promo {" +
                "header='" + mHeader + '\'' +
                ", sub header='" + mSubHeader + '\'' +
                ", find more='" + mFindMoreMessage + '\'' +
                ", find more url='" + mFindMoreUrl + '\'' +
                '}';
    }
}
