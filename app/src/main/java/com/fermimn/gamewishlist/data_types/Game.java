package com.fermimn.gamewishlist.data_types;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

// TODO: add documentation

public class Game extends GamePreview {

    private List<String> mPegi;
    private String mReleaseDate;
    private List<String> mGenres;
    private String mDescription;
    private String mOfficialSite;
    private String mPlayers;
    private boolean mValidForPromotions;
    private List<Promo> mPromos;
    private List<Uri> mGallery;

    public List<String> getPegi() {
        return mPegi;
    }

    public void setPegi(List<String> mPegi) {
        this.mPegi = mPegi;
    }

    public void addPegi(String pegi) {
        if (!hasPegi()) {
            mPegi = new ArrayList<>();
        }
        mPegi.add(pegi);
    }

    public boolean hasPegi() {
        if (mPegi == null) {
            return false;
        }
        return !mPegi.isEmpty();
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.mReleaseDate = releaseDate;
    }

    public boolean hasReleaseDate() {
        return mReleaseDate != null;
    }

    public List<String> getGenres() {
        return mGenres;
    }

    public void setGenres(List<String> genres) {
        mGenres = genres;
    }

    public void addGenre(String genre) {
        if (mGenres == null) {
            mGenres = new ArrayList<>();
        }
        mGenres.add(genre);
    }

    public boolean hasGenres() {
        if (mGenres == null) {
            return false;
        }
        return !mGenres.isEmpty();
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public boolean hasDescription() {
        return mDescription != null;
    }

    public String getOfficialSite() {
        return mOfficialSite;
    }

    public void setOfficialSite(String officialSite) {
        mOfficialSite = officialSite;
    }

    public boolean hasOfficialSite() {
        return mOfficialSite != null;
    }

    public String getPlayers() {
        return mPlayers;
    }

    public void setPlayers(String players) {
        mPlayers = players;
    }

    public boolean hasPlayers() {
        return mPlayers != null;
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

    public boolean hasPromo() {
        if (mPromos == null) {
            return false;
        }
        return !mPromos.isEmpty();
    }

    public void addPromo(Promo promo) {
        if (!hasPromo()) {
            mPromos = new ArrayList<>();
        }

        mPromos.add(promo);
    }

    public List<Uri> getGallery() {
        return mGallery;
    }

    public void setGallery(List<Uri> mGallery) {
        this.mGallery = mGallery;
    }

    public void addToGallery(Uri image) {

        if (mGallery == null) {
            mGallery = new ArrayList<>();
        }

        if (!image.toString().isEmpty()) {
            mGallery.add(image);
        }
    }

    public boolean hasGallery() {
        if (mGallery == null) {
            return false;
        }
        return !mGallery.isEmpty();
    }

    @Override
    public String toString() {
        return "Game{" +
                super.toString() +
                "mPegi=" + mPegi +
                ", mReleaseDate='" + mReleaseDate + '\'' +
                ", mGenres=" + mGenres +
                ", mDescription='" + mDescription + '\'' +
                ", mOfficialSite='" + mOfficialSite + '\'' +
                ", mPlayers='" + mPlayers + '\'' +
                ", mValidForPromotions=" + mValidForPromotions +
                ", mPromos=" + mPromos +
                ", mGallery=" + mGallery +
                '}';
    }

}
