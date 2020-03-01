package com.fermimn.gamewishlist.models;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Game extends GamePreview {

    private String mPlayers;
    private String mReleaseDate;
    private String mDescription;
    private String mOfficialWebSite;
    private List<Promo> mPromos;
    private List<String> mGenres;
    private List<String> mPegi;
    private List<Uri> mGallery;
    private boolean mValidForPromo;

    public Game(int id) {
        super(id);
    }

    /**
     * @return a list of strings where every string identifies a type of pegi of the game,
     *         null if the game has no info about pegi
     */
    @Nullable
    public List<String> getPegi() {
        return mPegi;
    }

    /**
     * Set a list of string where every string identifies a type of pegi of the game
     * @param pegi a list of string where every string identifies a type of pegi of the game
     */
    @SuppressWarnings("unused")
    public void setPegi(@Nullable List<String> pegi) {
        if (pegi != null && !pegi.isEmpty()) {
            mPegi = pegi;
        }
    }

    /**
     * Add a string that identifies a type of pegi assigned to the game
     * @param pegi a string that identifies a type of pegi assigned to the game
     */
    public void addPegi(@Nullable String pegi) {
        if (pegi != null) {
            if (mPegi == null) {
                mPegi = new ArrayList<>();
            }
            mPegi.add(pegi);
        }
    }

    /**
     * @return a string representing the release date, null if the game hasn't got the release date
     * NB: The returned value is not necessarily a date but also something like "ND" or "N/A"
     */
    @Nullable
    public String getReleaseDate() {
        return mReleaseDate;
    }

    /**
     * Set a string representing the release date
     * @param releaseDate a string representing the release date
     * NB: The assigned value is not necessarily a date but also something like "ND" or "N/A"
     */
    public void setReleaseDate(@Nullable String releaseDate) {
        mReleaseDate = releaseDate;
    }

    /**
     * @return a list of strings where every string identify a genre of the game,
     *         null if the game has no info about the genres
     */
    @Nullable
    public List<String> getGenres() {
        return mGenres;
    }

    /**
     * Set a list of strings where every string identifies a genre of the game
     * @param genres a list of strings where every string identifies a genre of the game
     */
    @SuppressWarnings("unused")
    public void setGenres(@Nullable List<String> genres) {
        if (genres != null && !genres.isEmpty()) {
            mGenres = genres;
        }
    }

    /**
     * Add a string representing a genre of the game
     * @param genre a string representing a genre of the game
     */
    public void addGenre(@Nullable String genre) {
        if (genre != null) {
            if (mGenres == null) {
                mGenres = new ArrayList<>();
            }
            mGenres.add(genre);
        }
    }

    /**
     * @return a string representing the description of the game
     * NB: The description can be an HTML
     */
    @Nullable
    public String getDescription() {
        return mDescription;
    }

    /**
     * Set the description of the game
     * @param description a string representing the description of the game
     * NB: The description can be an HTML
     */
    public void setDescription(@Nullable String description) {
        mDescription = description;
    }

    /**
     * @return a string representing the URL to the official website of the game
     */
    @Nullable
    public String getOfficialWebSite() {
        return mOfficialWebSite;
    }

    /**
     * Set the official site of the game
     * @param officialWebSite a string representing the URL to the official website of the game
     */
    public void setOfficialWebSite(@Nullable String officialWebSite) {
        mOfficialWebSite = officialWebSite;
    }

    /**
     * @return a string representing the maximum number of the players of the game
     * NB: the string doesn't have to be necessarily a number
     */
    @Nullable
    public String getPlayers() {
        return mPlayers;
    }

    /**
     * Set the number of the players
     * @param players a string representing the maximum number of the players of the game
     * NB: the string doesn't have to be necessarily a number
     */
    public void setPlayers(@Nullable String players) {
        mPlayers = players;
    }

    /**
     * @return true if the game can be used to buy other games at lower prices
     *         in some kind of promotions, false otherwise
     */
    public boolean isValidForPromotions() {
        return mValidForPromo;
    }

    /**
     * Set if the game is valid for promotions
     * @param validForPromo true if the game can be used to buy other games at lower prices
     *                           in some kind of promotions, false otherwise
     */
    public void setValidForPromo(boolean validForPromo) {
        mValidForPromo = validForPromo;
    }

    /**
     * @return a List of Promo objects related to the game
     */
    @Nullable
    public List<Promo> getPromo() {
        return mPromos;
    }

    /**
     * Set a list of Promo related to the game
     * @param promo a list of Promo related to the game
     */
    @SuppressWarnings("unused")
    public void setPromo(@Nullable List<Promo> promo) {
        if (promo != null && !promo.isEmpty()) {
            mPromos = promo;
        }
    }

    /**
     * Add a Promo to the game
     * @param promo of the game
     */
    public void addPromo(@Nullable Promo promo) {
        if (promo != null) {
            if (mPromos == null) {
                mPromos = new ArrayList<>();
            }
            mPromos.add(promo);
        }
    }

    /**
     * @return a List of Uri representing links to offline or online resources
     */
    @Nullable
    public List<Uri> getGallery() {
        return mGallery;
    }

    /**
     * Set a List of Uri representing links to offline or online resources
     * @param gallery a List of Uri representing links to offline or online resources
     */
    @SuppressWarnings("unused")
    public void setGallery(@Nullable List<Uri> gallery) {
        if (gallery != null && !gallery.isEmpty()) {
            mGallery = gallery;
        }
    }

    /**
     * Add a Uri representing a link to an offline or online resource
     * @param image a Uri representing a link to an offline or online resource
     */
    public void addToGallery(@Nullable Uri image) {
        // image.toString.isEmpty() can be useful in rare cases
        // example: https://www.gamestop.it/PS3/Games/22408/catherine
        if (image != null && !image.toString().isEmpty()) {
            if (mGallery == null) {
                mGallery = new ArrayList<>();
            }
            mGallery.add(image);
        }
    }

    /**
     * @return a string with all the info about the game
     */
    @NonNull
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
                ", mValidForPromotions=" + mValidForPromo +
                ", mPromos=" + mPromos +
                ", mGallery=" + mGallery +
                '}';
    }

}
