package com.fermimn.gamewishlist.models;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GamePreview implements Comparable {

    private final int mId;
    private String mTitle;
    private String mPlatform;
    private String mPublisher;
    private String mCover;
    private Price mPrices;

    // TODO: a game can be out of stock, so it can have no price

    public GamePreview(int id) {
        mId = id;
        mPrices = new Price();
    }

    /**
     * @return the ID of the game
     */
    public int getId() {
        return mId;
    }

    @NonNull
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(@NonNull String title) {
        mTitle = title;
    }

    @NonNull
    public String getPlatform() {
        return mPlatform;
    }

    public void setPlatform(@NonNull String platform) {
        mPlatform = platform;
    }

    @Nullable
    public String getPublisher() {
        return mPublisher;
    }

    public void setPublisher(@NonNull String publisher) {
        mPublisher = publisher;
    }

    /**
     * @return a String representing the cover. The Uri contains a link to an online resource
     * if anything is found offline, otherwise is an offline resource
     */
    @Nullable
    public String getCover() {
        return mCover;
    }

    /**
     * Set the cover with an Uri. The Uri must be a link to an online resource.
     * @param cover a Uri containing a link to an online resource image
     */
    public void setCover(@NonNull String cover) {
        mCover = cover;
    }

    @Nullable
    public Float getNewPrice() {
        return mPrices.getNew();
    }

    public void setNewPrice(@NonNull Float newPrice) {
        mPrices.setNew(newPrice);
    }

    @Nullable
    public Float getUsedPrice() {
        return mPrices.getUsed();
    }

    public void setUsedPrice(@NonNull Float usedPrice) {
        mPrices.setUsed(usedPrice);
    }

    @Nullable
    public Float getPreorderPrice() {
        return mPrices.getPreorder();
    }

    public void setPreorderPrice(@NonNull Float preorderPrice) {
        mPrices.setPreorder(preorderPrice);
    }

    @Nullable
    public Float getDigitalPrice() {
        return mPrices.getDigital();
    }

    public void setDigitalPrice(@NonNull Float digitalPrice) {
        mPrices.setDigital(digitalPrice);
    }

    @Nullable
    public List<Float> getOlderNewPrices() {
        return mPrices.getOldNew();
    }

    public void setOlderNewPrices(@Nullable ArrayList<Float> olderNewPrices) {
        mPrices.setOldNew(olderNewPrices);
    }

    @Nullable
    public List<Float> getOlderUsedPrices() {
        return mPrices.getOldUsed();
    }

    public void setOlderUsedPrices(@Nullable ArrayList<Float> olderUsedPrices) {
        mPrices.setOldUsed(olderUsedPrices);
    }

    @Nullable
    public List<Float> getOlderDigitalPrices() {
        return mPrices.getOldDigital();
    }

    public void setOlderDigitalPrices(@Nullable ArrayList<Float> olderDigitalPrices) {
        mPrices.setOldDigital(olderDigitalPrices);
    }
    @Nullable
    public List<Float> getOlderPreorderPrices() {
        return mPrices.getOldPreorder();
    }

    public void setOlderPreorderPrices(@Nullable ArrayList<Float> olderPreorderPrices) {
        mPrices.setOldPreorder(olderPreorderPrices);
    }

    public void addOlderNewPrice(@Nullable Float olderNewPrice) {
        mPrices.addOldNew(olderNewPrice);
    }

    public void addOlderUsedPrice(@Nullable Float olderUsedPrice) {
        mPrices.addOldUsed(olderUsedPrice);
    }

    public void addOlderDigitalPrice(@Nullable Float olderDigitalPrice) {
        mPrices.addOldDigital(olderDigitalPrice);
    }

    public void addOlderPreorderPrice(@Nullable Float olderPreorderPrice) {
        mPrices.addOldPreorder(olderPreorderPrice);
    }

    public boolean isNewAvailable() {
        return mPrices.getNewAvailable();
    }

    public void setNewAvailable(boolean mNewAvailable) {
        mPrices.setNewAvailable(mNewAvailable);
    }

    public boolean isUsedAvailable() {
        return mPrices.getUsedAvailable();
    }

    public void setUsedAvailable(boolean mUsedAvailable) {
        mPrices.setUsedAvailable(mUsedAvailable);
    }

    public boolean isPreorderAvailable() {
        return mPrices.getPreorderAvailable();
    }

    public void setPreorderAvailable(boolean mPreorderAvailable) {
        mPrices.setPreorderAvailable(mPreorderAvailable);
    }

    public boolean isDigitalAvailable() {
        return mPrices.getDigitalAvailable();
    }

    public void setDigitalAvailable(boolean mDigitalAvailable) {
        mPrices.setDigitalAvailable(mDigitalAvailable);
    }

    /**
     * @param o a GamePreview object
     * @return true if two GamePreview are identical, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GamePreview)) return false;
        GamePreview that = (GamePreview) o;
        return mId == that.mId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mId);
    }

    @NonNull
    @Override
    public String toString() {
        return "GamePreview{" +
                "mId='" + mId + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mPublisher='" + mPublisher + '\'' +
                ", mPlatform='" + mPlatform + '\'' +
                ", mNewPrice=" + mPrices.getNew() +
                ", mUsedPrice=" + mPrices.getUsed() +
                ", mPreorderPrice=" + mPrices.getPreorder() +
                ", mDigitalPrice=" + mPrices.getDigital() +
                ", mOlderNewPrices=" + mPrices.getOldNew() +
                ", mOlderUsedPrices=" + mPrices.getOldUsed() +
                ", mOlderDigitalPrices=" + mPrices.getOldDigital() +
                ", mOlderPreorderPrices=" + mPrices.getOldPreorder() +
                '}';
    }

    @Override
    public int compareTo(@NonNull Object o) {
        GamePreview game = (GamePreview) o;
        return Integer.compare(mId, game.mId);
    }

}
