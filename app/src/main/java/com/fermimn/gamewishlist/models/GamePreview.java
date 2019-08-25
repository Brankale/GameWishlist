package com.fermimn.gamewishlist.models;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fermimn.gamewishlist.exceptions.GameException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GamePreview implements Comparable {

    private final String mId;
    private final String mTitle;
    private final String mPlatform;
    private String mPublisher;
    private Uri mCover;

    private Double mNewPrice;
    private Double mUsedPrice;
    private Double mPreorderPrice;
    private Double mDigitalPrice;

    private List<Double> mOlderNewPrices;
    private List<Double> mOlderUsedPrices;
    private List<Double> mOlderDigitalPrices;
    private List<Double> mOlderPreorderPrices;

    /**
     * A game can exist without a price but it must be recognisable through
     * a title a platform and an ID in order to be managed
     * @param id of the game associated with store (it must be a number)
     * @param title of the game (it cannot be an empty string)
     * @param platform of the game (it cannot be an empty string)
     */
    public GamePreview(String id, String title, String platform) {

        // every parameter must be non null
        if (id == null || title == null || platform == null) {
            throw new GameException();
        }

        // strings must be non empty
        if (title.isEmpty() || platform.isEmpty()) {
            throw new GameException();
        }

        // id must be a number
        try {
            Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new GameException();
        }

        mId = id;
        mTitle = title;
        mPlatform = platform;
    }

    /**
     * @return the ID of the game
     */
    @NonNull
    public String getId() {
        return mId;
    }

    /**
     * @return the title of the game
     */
    @NonNull
    public String getTitle() {
        return mTitle;
    }

    /**
     * @return the platform of the game
     */
    @NonNull
    public String getPlatform() {
        return mPlatform;
    }

    /**
     * @return the publisher of the game, null otherwise
     */
    @Nullable
    public String getPublisher() {
        return mPublisher;
    }

    /**
     * Set the publisher of the game
     * @param publisher of the game
     */
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

    /**
     * @return the New Price of the game, null if the game don't have a new price
     */
    @Nullable
    public Double getNewPrice() {
        return mNewPrice;
    }

    /**
     * Set the New Price of the game
     * @param newPrice of the game
     */
    public void setNewPrice(@NonNull Double newPrice) {
        mNewPrice = newPrice;
    }

    /**
     * @return the Used Price of the game, null if the game don't have a used price
     */
    @Nullable
    public Double getUsedPrice() {
        return mUsedPrice;
    }

    /**
     * Set Used Price of the game
     * @param usedPrice o the game
     */
    public void setUsedPrice(@NonNull Double usedPrice) {
        mUsedPrice = usedPrice;
    }

    /**
     * @return the Preorder Price of the game, null if the game don't have a preorder price
     */
    @Nullable
    public Double getPreorderPrice() {
        return mPreorderPrice;
    }

    /**
     * Set the Preorder Price of the game
     * @param preorderPrice of the game
     */
    public void setPreorderPrice(@NonNull Double preorderPrice) {
        mPreorderPrice = preorderPrice;
    }

    /**
     * @return the DigitalPrice of the game, null if the game don't have a digital price
     */
    @Nullable
    public Double getDigitalPrice() {
        return mDigitalPrice;
    }

    /**
     * Set the Digital Price of the game
     * @param digitalPrice of the game
     */
    public void setDigitalPrice(@NonNull Double digitalPrice) {
        mDigitalPrice = digitalPrice;
    }

    /**
     * @return the Older New Prices of the game, null if the game has no older prices
     */
    @Nullable
    public List<Double> getOlderNewPrices() {
        return mOlderNewPrices;
    }

    /**
     * Set the Older New Prices of the game
     * @param olderNewPrices of the game
     */
    public void setOlderNewPrices(@Nullable List<Double> olderNewPrices) {
        if (olderNewPrices != null && !olderNewPrices.isEmpty()) {
            mOlderNewPrices = olderNewPrices;
        }
    }

    /**
     * @return the Older Used Prices of the game, null if the game has no older prices
     */
    @Nullable
    public List<Double> getOlderUsedPrices() {
        return mOlderUsedPrices;
    }

    /**
     * Set the Older Used Prices of the game
     * @param olderUsedPrices of teh game
     */
    public void setOlderUsedPrices(@Nullable List<Double> olderUsedPrices) {
        if (olderUsedPrices != null && !olderUsedPrices.isEmpty()) {
            mOlderUsedPrices = olderUsedPrices;
        }
    }

    /**
     * @return the Older Digital Prices of the game, null if the game has no older prices
     */
    @Nullable
    public List<Double> getOlderDigitalPrices() {
        return mOlderDigitalPrices;
    }

    /**
     * Set the Older Digital Prices of the game
     * @param olderDigitalPrices of the game
     */
    public void setOlderDigitalPrices(@Nullable List<Double> olderDigitalPrices) {
        if (olderDigitalPrices != null && !olderDigitalPrices.isEmpty()) {
            mOlderDigitalPrices = olderDigitalPrices;
        }
    }

    /**
     * @return the Older Preorder Prices of the game, null if the game has no older prices
     */
    @Nullable
    public List<Double> getOlderPreorderPrices() {
        return mOlderPreorderPrices;
    }

    /**
     * Set the Older PreorderPrices of the game
     * @param olderPreorderPrices of the game
     */
    public void setOlderPreorderPrices(@Nullable List<Double> olderPreorderPrices) {
        if (olderPreorderPrices != null && !olderPreorderPrices.isEmpty()) {
            mOlderPreorderPrices = olderPreorderPrices;
        }
    }

    /**
     * Add a Older New Price
     * @param olderNewPrice of the game
     */
    public void addOlderNewPrice(@Nullable Double olderNewPrice) {
        if (olderNewPrice != null) {
            if (mOlderNewPrices == null){
                mOlderNewPrices = new ArrayList<>();
            }
            mOlderNewPrices.add(olderNewPrice);
        }
    }

    /**
     * Add a Older Used Price
     * @param olderUsedPrice of the game
     */
    public void addOlderUsedPrice(@Nullable Double olderUsedPrice) {
        if (olderUsedPrice != null) {
            if (mOlderUsedPrices == null) {
                mOlderUsedPrices = new ArrayList<>();
            }
            mOlderUsedPrices.add(olderUsedPrice);
        }
    }

    /**
     * Add a Older Digital Price
     * @param olderDigitalPrice of the game
     */
    public void addOlderDigitalPrice(@Nullable Double olderDigitalPrice) {
        if (olderDigitalPrice != null) {
            if (mOlderDigitalPrices == null) {
                mOlderDigitalPrices = new ArrayList<>();
            }
            mOlderDigitalPrices.add(olderDigitalPrice);
        }
    }

    /**
     * Add a Older Preorder Price
     * @param olderPreorderPrice of the game
     */
    public void addOlderPreorderPrice(@Nullable Double olderPreorderPrice) {
        if (olderPreorderPrice != null) {
            if (mOlderPreorderPrices == null) {
                mOlderPreorderPrices = new ArrayList<>();
            }
            mOlderPreorderPrices.add(olderPreorderPrice);
        }
    }

    /**
     * @param o a GamePreview object
     * @return true if two GamePreview are identical, false otherwise
     */
    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GamePreview)) {
            return false;
        }
        GamePreview that = (GamePreview) o;
        return mId.equals(that.mId);
    }

    /**
     * @return the hashCode of the GamePreview object
     */
    @Override
    public int hashCode() {
        return Objects.hash(mId);
    }

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
        return mId.compareTo(game.mId);
    }

}
