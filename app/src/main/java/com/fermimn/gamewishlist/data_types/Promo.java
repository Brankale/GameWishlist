package com.fermimn.gamewishlist.data_types;

// TODO: add documentation
// TODO: consider to change the name of the variables

public class Promo {

    private String m_header;
    private String m_validity;
    private String m_message;
    private String m_messageURL;

    public Promo() {
    }

    public String getHeader() {
        return m_header;
    }

    public void setHeader(String header) {
        m_header = header;
    }

    public String getValidity() {
        return m_validity;
    }

    public void setValidity(String validity) {
        m_validity = validity;
    }

    public String getMessage() {
        return m_message;
    }

    public void setMessage(String message) {
        m_message = message;
    }

    public String getMessageURL() {
        return m_messageURL;
    }

    public void setMessageURL(String messageURL) {
        m_messageURL = messageURL;
    }

}
