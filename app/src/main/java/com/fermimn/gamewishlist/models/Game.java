package com.fermimn.gamewishlist.models;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class Game extends GamePreview {

    private List<String> mPegi;
    private String mReleaseDate;
    private List<String> mGenres;
    private String mDescription;
    private String mOfficialWebSite;
    private String mPlayers;
    private boolean mValidForPromotions;
    private List<Promo> mPromos;
    private List<Uri> mGallery;

    /**
     * A game can exist without a price but it must be recognisable through
     * a title a platform and an ID in order to be managed
     * @param id of the game associated with store
     * @param title of the game
     * @param platform of the game
     */
    public Game(String id, String title, String platform) {
        super(id, title, platform);
    }

    /**
     * @return a list of strings where every string identifies a type of pegi of the game
     */
    public List<String> getPegi() {
        return mPegi;
    }

    /**
     * Set a list of string where every string identifies a type of pegi of the game
     * @param pegi a list of string where every string identifies a type of pegi of the game
     */
    @SuppressWarnings("unused")
    public void setPegi(List<String> pegi) {
        mPegi = pegi;
    }

    /**
     * Add a string that identifies a type of pegi assigned to the game
     * @param pegi a string that identifies a type of pegi assigned to the game
     */
    public void addPegi(String pegi) {
        if (!hasPegi()) {
            mPegi = new ArrayList<>();
        }
        mPegi.add(pegi);
    }

    /**
     * @return true if the game has some pegi info, false otherwise
     */
    public boolean hasPegi() {
        if (mPegi == null) {
            return false;
        }
        return !mPegi.isEmpty();
    }

    /**
     * @return a string representing the release date
     * NB: The returned value is not necessarily a date but also something like "ND" or "N/A"
     */
    public String getReleaseDate() {
        return mReleaseDate;
    }

    /**
     * Set a string representing the release date
     * @param releaseDate a string representing the release date
     * NB: The assigned value is not necessarily a date but also something like "ND" or "N/A"
     */
    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    /**
     * @return true if the game has some info about the date, false otherwise
     */
    public boolean hasReleaseDate() {
        return mReleaseDate != null;
    }

    /**
     * @return a list of strings where every string identify a genre of the game
     */
    public List<String> getGenres() {
        return mGenres;
    }

    /**
     * Set a list of strings where every string identifies a genre of the game
     * @param genres a list of strings where every string identifies a genre of the game
     */
    @SuppressWarnings("unused")
    public void setGenres(List<String> genres) {
        mGenres = genres;
    }

    /**
     * Add a string representing a genre of the game
     * @param genre a string representing a genre of the game
     */
    public void addGenre(String genre) {
        if (mGenres == null) {
            mGenres = new ArrayList<>();
        }
        mGenres.add(genre);
    }

    /**
     * @return true if the game have some genres, false otherwise
     */
    public boolean hasGenres() {
        if (mGenres == null) {
            return false;
        }
        return !mGenres.isEmpty();
    }

    /**
     * @return a string representing the description of the game
     * NB: The description can be an HTML
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * Set the description of the game
     * @param description a string representing the description of the game
     * NB: The description can be an HTML
     */
    public void setDescription(String description) {
        mDescription = description;
    }

    /**
     * @return true if the game has the description, false otherwise
     */
    public boolean hasDescription() {
        return mDescription != null;
    }

    /**
     * @return a string representing the URL to the official website of the game
     */
    public String getOfficialWebSite() {
        return mOfficialWebSite;
    }

    /**
     * Set the official site of the game
     * @param officialWebSite a string representing the URL to the official website of the game
     */
    public void setOfficialWebSite(String officialWebSite) {
        mOfficialWebSite = officialWebSite;
    }

    /**
     * @return true if the game has an official website
     */
    public boolean hasOfficialWebSite() {
        return mOfficialWebSite != null;
    }

    /**
     * @return a string representing the maximum number of the players of the game
     * NB: the string doesn't have to be necessarily a number
     */
    public String getPlayers() {
        return mPlayers;
    }

    /**
     * Set the number of the players
     * @param players a string representing the maximum number of the players of the game
     * NB: the string doesn't have to be necessarily a number
     */
    public void setPlayers(String players) {
        mPlayers = players;
    }

    /**
     * @return true if the game have some info about the players, false otherwise
     */
    public boolean hasPlayers() {
        return mPlayers != null;
    }

    /**
     * @return true if the game can be used to buy other games at lower prices
     *         in some kind of promotions, false otherwise
     */
    public boolean isValidForPromotions() {
        return mValidForPromotions;
    }

    /**
     * Set if the game is valid for promotions
     * @param validForPromotions true if the game can be used to buy other games at lower prices
     *                           in some kind of promotions, false otherwise
     */
    public void setValidForPromotions(boolean validForPromotions) {
        mValidForPromotions = validForPromotions;
    }

    /**
     * @return a List of Promo objects related to the game
     */
    public List<Promo> getPromo() {
        return mPromos;
    }

    /**
     * Set a list of Promo related to the game
     * @param promo a list of Promo related to the game
     */
    @SuppressWarnings("unused")
    public void setPromo(List<Promo> promo) {
        mPromos = promo;
    }

    /**
     * @return true if the game has some promo, false otherwise
     */
    public boolean hasPromo() {
        if (mPromos == null) {
            return false;
        }
        return !mPromos.isEmpty();
    }

    /**
     * Add a Promo to the game
     * @param promo of the game
     */
    public void addPromo(Promo promo) {
        if (!hasPromo()) {
            mPromos = new ArrayList<>();
        }
        mPromos.add(promo);
    }

    /**
     * @return a List of Uri representing links to offline or online resources
     */
    public List<Uri> getGallery() {
        return mGallery;
    }

    /**
     * Set a List of Uri representing links to offline or online resources
     * @param gallery a List of Uri representing links to offline or online resources
     */
    @SuppressWarnings("unused")
    public void setGallery(List<Uri> gallery) {
        mGallery = gallery;
    }

    /**
     * Add a Uri representing a link to an offline or online resource
     * @param image a Uri representing a link to an offline or online resource
     */
    public void addToGallery(Uri image) {

        if (mGallery == null) {
            mGallery = new ArrayList<>();
        }

        if (!image.toString().isEmpty()) {
            mGallery.add(image);
        }
    }

    /**
     * @return true if the game has some gallery images, false otherwise
     */
    public boolean hasGallery() {
        if (mGallery == null) {
            return false;
        }
        return !mGallery.isEmpty();
    }

    /**
     * @return a string with all the info about the game
     */
    @Override
    public String toString() {
        return "Game {" +
                super.toString() +
                "mPegi=" + mPegi +
                ", mReleaseDate='" + mReleaseDate + '\'' +
                ", mGenres=" + mGenres +
                ", mDescription='" + mDescription + '\'' +
                ", mOfficialSite='" + mOfficialWebSite + '\'' +
                ", mPlayers='" + mPlayers + '\'' +
                ", mValidForPromotions=" + mValidForPromotions +
                ", mPromos=" + mPromos +
                ", mGallery=" + mGallery +
                '}';
    }

}
