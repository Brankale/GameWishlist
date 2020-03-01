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
    private Uri mCover;

    private Double mNewPrice;
    private Double mUsedPrice;
    private Double mPreorderPrice;
    private Double mDigitalPrice;

    private boolean mNewAvailable;
    private boolean mUsedAvailable;
    private boolean mPreorderAvailable;
    private boolean mDigitalAvailable;

    private List<Double> mOlderNewPrices;
    private List<Double> mOlderUsedPrices;
    private List<Double> mOlderDigitalPrices;
    private List<Double> mOlderPreorderPrices;

    // TODO: a game can be out of stock, so it can have no price

    public GamePreview(int id) {
        mId = id;
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
     * @return a Uri representing the cover. The Uri contains a link to an online resource
     * if anything is found offline, otherwise is an offline resource
     */
    @Nullable
    public Uri getCover() {
        return mCover;
    }

    /**
     * Set the cover with an Uri. The Uri must be a link to an online resource.
     * @param cover a Uri containing a link to an online resource image
     */
    public void setCover(@NonNull Uri cover) {
        mCover = cover;
    }

    @Nullable
    public Double getNewPrice() {
        return mNewPrice;
    }

    public void setNewPrice(@NonNull Double newPrice) {
        mNewPrice = newPrice;
    }

    @Nullable
    public Double getUsedPrice() {
        return mUsedPrice;
    }

    public void setUsedPrice(@NonNull Double usedPrice) {
        mUsedPrice = usedPrice;
    }

    @Nullable
    public Double getPreorderPrice() {
        return mPreorderPrice;
    }

    public void setPreorderPrice(@NonNull Double preorderPrice) {
        mPreorderPrice = preorderPrice;
    }

    @Nullable
    public Double getDigitalPrice() {
        return mDigitalPrice;
    }

    public void setDigitalPrice(@NonNull Double digitalPrice) {
        mDigitalPrice = digitalPrice;
    }

    @Nullable
    public List<Double> getOlderNewPrices() {
        return mOlderNewPrices;
    }

    public void setOlderNewPrices(@Nullable List<Double> olderNewPrices) {
        if (olderNewPrices != null && !olderNewPrices.isEmpty()) {
            mOlderNewPrices = olderNewPrices;
        }
    }

    @Nullable
    public List<Double> getOlderUsedPrices() {
        return mOlderUsedPrices;
    }

    public void setOlderUsedPrices(@Nullable List<Double> olderUsedPrices) {
        if (olderUsedPrices != null && !olderUsedPrices.isEmpty()) {
            mOlderUsedPrices = olderUsedPrices;
        }
    }

    @Nullable
    public List<Double> getOlderDigitalPrices() {
        return mOlderDigitalPrices;
    }

    public void setOlderDigitalPrices(@Nullable List<Double> olderDigitalPrices) {
        if (olderDigitalPrices != null && !olderDigitalPrices.isEmpty()) {
            mOlderDigitalPrices = olderDigitalPrices;
        }
    }
    @Nullable
    public List<Double> getOlderPreorderPrices() {
        return mOlderPreorderPrices;
    }

    public void setOlderPreorderPrices(@Nullable List<Double> olderPreorderPrices) {
        if (olderPreorderPrices != null && !olderPreorderPrices.isEmpty()) {
            mOlderPreorderPrices = olderPreorderPrices;
        }
    }

    public void addOlderNewPrice(@Nullable Double olderNewPrice) {
        if (olderNewPrice != null) {
            if (mOlderNewPrices == null){
                mOlderNewPrices = new ArrayList<>();
            }
            mOlderNewPrices.add(olderNewPrice);
        }
    }

    public void addOlderUsedPrice(@Nullable Double olderUsedPrice) {
        if (olderUsedPrice != null) {
            if (mOlderUsedPrices == null) {
                mOlderUsedPrices = new ArrayList<>();
            }
            mOlderUsedPrices.add(olderUsedPrice);
        }
    }

    public void addOlderDigitalPrice(@Nullable Double olderDigitalPrice) {
        if (olderDigitalPrice != null) {
            if (mOlderDigitalPrices == null) {
                mOlderDigitalPrices = new ArrayList<>();
            }
            mOlderDigitalPrices.add(olderDigitalPrice);
        }
    }

    public void addOlderPreorderPrice(@Nullable Double olderPreorderPrice) {
        if (olderPreorderPrice != null) {
            if (mOlderPreorderPrices == null) {
                mOlderPreorderPrices = new ArrayList<>();
            }
            mOlderPreorderPrices.add(olderPreorderPrice);
        }
    }

    public boolean isNewAvailable() {
        return mNewAvailable;
    }

    public void setNewAvailable(boolean mNewAvailable) {
        this.mNewAvailable = mNewAvailable;
    }

    public boolean isUsedAvailable() {
        return mUsedAvailable;
    }

    public void setUsedAvailable(boolean mUsedAvailable) {
        this.mUsedAvailable = mUsedAvailable;
    }

    public boolean isPreorderAvailable() {
        return mPreorderAvailable;
    }

    public void setPreorderAvailable(boolean mPreorderAvailable) {
        this.mPreorderAvailable = mPreorderAvailable;
    }

    public boolean isDigitalAvailable() {
        return mDigitalAvailable;
    }

    public void setDigitalAvailable(boolean mDigitalAvailable) {
        this.mDigitalAvailable = mDigitalAvailable;
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
                ", mNewPrice=" + mNewPrice +
                ", mUsedPrice=" + mUsedPrice +
                ", mPreorderPrice=" + mPreorderPrice +
                ", mDigitalPrice=" + mDigitalPrice +
                ", mOlderNewPrices=" + mOlderNewPrices +
                ", mOlderUsedPrices=" + mOlderUsedPrices +
                ", mOlderDigitalPrices=" + mOlderDigitalPrices +
                ", mOlderPreorderPrices=" + mOlderPreorderPrices +
                '}';
    }

    @Override
    public int compareTo(@NonNull Object o) {
        GamePreview game = (GamePreview) o;
        return Integer.compare(mId, game.mId);
    }

}
