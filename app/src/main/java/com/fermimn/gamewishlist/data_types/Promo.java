package com.fermimn.gamewishlist.data_types;

// TODO: add documentation
// TODO: consider to change the name of the variables

public class Promo {

    private String mHeader;
    private String mValidity;
    private String mMessage;
    private String mMessageURL;

    public String getHeader() {
        return mHeader;
    }

    public void setHeader(String header) {
        mHeader = header;
    }

    public String getValidity() {
        return mValidity;
    }

    public void setValidity(String validity) {
        mValidity = validity;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getMessageURL() {
        return mMessageURL;
    }

    public void setMessageURL(String messageURL) {
        mMessageURL = messageURL;
    }

}
