package com.fermimn.gamewishlist.data_types;

import android.net.Uri;

import java.util.List;

// TODO: add documentation

public class Game extends GamePreview {

    private List<String> mGenres;
    private String mDescription;
    private String mOfficialSite;
    private String mPlayers;
    private boolean mValidForPromotions;
    private List<Promo> mPromos;
    private List<Uri> mGallery;

    public List<String> getGenres() {
        return mGenres;
    }

    public void setGenres(List<String> genres) {
        mGenres = genres;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getOfficialSite() {
        return mOfficialSite;
    }

    public void setOfficialSite(String officialSite) {
        mOfficialSite = officialSite;
    }

    public String getPlayers() {
        return mPlayers;
    }

    public void setPlayers(String players) {
        mPlayers = players;
    }

    public boolean isValidForPromotions() {
        return mValidForPromotions;
    }

    public void setValidForPromotions(boolean validForPromotions) {
        mValidForPromotions = validForPromotions;
    }

    public List<Promo> getPromo() {
        return mPromos;
    }

    public void setPromo(List<Promo> promo) {
        mPromos = promo;
    }

    public List<Uri> getGallery() {
        return mGallery;
    }

    public void setGallery(List<Uri> mGallery) {
        this.mGallery = mGallery;
    }
}
