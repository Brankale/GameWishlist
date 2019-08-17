package com.fermimn.gamewishlist.data_types;

import android.net.Uri;

import androidx.annotation.NonNull;

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

        // string must be non empty
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
    public @NonNull String getId() {
        return mId;
    }

    /**
     * @return the title of the game
     */
    public @NonNull String getTitle() {
        return mTitle;
    }

    /**
     * @return the platform of the game
     */
    public @NonNull String getPlatform() {
        return mPlatform;
    }

    /**
     * @return the publisher of the game, null otherwise
     */
    public String getPublisher() {
        return mPublisher;
    }

    /**
     * Set the publisher of the game
     * @param publisher of the game
     */
    public void setPublisher(String publisher) {
        mPublisher = publisher;
    }

    /**
     * @return a Uri representing the cover. The Uri contains a link to an online resource
     * if anything is found offline, otherwise is an offline resource
     */
    public Uri getCover() {
        // TODO: check if the image has been already downloaded
        // TODO: if so, return the offline resource instead of the link
        return mCover;
    }

    /**
     * Set the cover with an Uri. The Uri must be a link to an online resource.
     * @param cover a Uri containing a link to an online resource image
     */
    public void setCover(Uri cover) {
        mCover = cover;
    }

    /**
     * @return the New Price of the game, null if the game don't have a new price
     */
    public Double getNewPrice() {
        return mNewPrice;
    }

    /**
     * Set the New Price of the game
     * @param newPrice of the game
     */
    public void setNewPrice(Double newPrice) {
        mNewPrice = newPrice;
    }

    /**
     * @return the Used Price of the game, null if the game don't have a used price
     */
    public Double getUsedPrice() {
        return mUsedPrice;
    }

    /**
     * Set Used Price of the game
     * @param usedPrice o the game
     */
    public void setUsedPrice(Double usedPrice) {
        mUsedPrice = usedPrice;
    }

    /**
     * @return the Preorder Price of the game, null if the game don't have a preorder price
     */
    public Double getPreorderPrice() {
        return mPreorderPrice;
    }

    /**
     * Set the Preorder Price of the game
     * @param preorderPrice of the game
     */
    public void setPreorderPrice(Double preorderPrice) {
        mPreorderPrice = preorderPrice;
    }

    /**
     * @return the DigitalPrice of the game, null if the game don't have a digital price
     */
    public Double getDigitalPrice() {
        return mDigitalPrice;
    }

    /**
     * Set the Digital Price of the game
     * @param digitalPrice of the game
     */
    public void setDigitalPrice(Double digitalPrice) {
        mDigitalPrice = digitalPrice;
    }

    /**
     * @return the Older New Prices of the game, null if the game has no older prices
     */
    public List<Double> getOlderNewPrices() {
        return mOlderNewPrices != null && !mOlderNewPrices.isEmpty() ? mOlderNewPrices : null;
    }

    /**
     * Set the Older New Prices of the game
     * @param olderNewPrices of the game
     */
    public void setOlderNewPrices(List<Double> olderNewPrices) {
        mOlderNewPrices = olderNewPrices;
    }

    /**
     * @return the Older Used Prices of the game, null if the game has no older prices
     */
    public List<Double> getOlderUsedPrices() {
        return mOlderUsedPrices != null && !mOlderUsedPrices.isEmpty() ? mOlderUsedPrices : null;
    }

    /**
     * Set the Older Used Prices of the game
     * @param olderUsedPrices of teh game
     */
    public void setOlderUsedPrices(List<Double> olderUsedPrices) {
        mOlderUsedPrices = olderUsedPrices;
    }

    /**
     * @return the Older Digital Prices of the game, null if the game has no older prices
     */
    public List<Double> getOlderDigitalPrices() {
        return mOlderDigitalPrices != null && !mOlderDigitalPrices.isEmpty() ?
                mOlderDigitalPrices : null;
    }

    /**
     * Set the Older Digital Prices of the game
     * @param olderDigitalPrices of the game
     */
    public void setOlderDigitalPrices(List<Double> olderDigitalPrices) {
        mOlderDigitalPrices = olderDigitalPrices;
    }

    /**
     * @return the Older Preorder Prices of the game, null if the game has no older prices
     */
    public List<Double> getOlderPreorderPrices() {
        return mOlderPreorderPrices != null && !mOlderPreorderPrices.isEmpty() ?
                mOlderPreorderPrices : null;
    }

    /**
     * Set the Older PreorderPrices of the game
     * @param olderPreorderPrices of the game
     */
    public void setOlderPreorderPrices(List<Double> olderPreorderPrices) {
        mOlderPreorderPrices = olderPreorderPrices;
    }

    /**
     * Add a Older New Price
     * @param olderNewPrice of the game
     */
    public void addOlderNewPrice(double olderNewPrice) {
        if (mOlderNewPrices == null){
            mOlderNewPrices = new ArrayList<>();
        }
        mOlderNewPrices.add(olderNewPrice);
    }

    /**
     * Add a Older Used Price
     * @param olderUsedPrice of the game
     */
    public void addOlderUsedPrice(double olderUsedPrice) {
        if (mOlderUsedPrices == null){
            mOlderUsedPrices = new ArrayList<>();
        }
        mOlderUsedPrices.add(olderUsedPrice);
    }

    /**
     * Add a Older Digital Price
     * @param olderDigitalPrice of the game
     */
    public void addOlderDigitalPrice(double olderDigitalPrice) {
        if (mOlderDigitalPrices == null){
            mOlderDigitalPrices = new ArrayList<>();
        }
        mOlderDigitalPrices.add(olderDigitalPrice);
    }

    /**
     * Add a Older Preorder Price
     * @param olderPreorderPrice of the game
     */
    public void addOlderPreorderPrice(double olderPreorderPrice) {
        if (mOlderPreorderPrices == null){
            mOlderPreorderPrices = new ArrayList<>();
        }
        mOlderPreorderPrices.add(olderPreorderPrice);
    }

    /**
     * @return true if there's a new price, false otherwise
     */
    public boolean hasNewPrice() {
        return mNewPrice != null;
    }

    /**
     * @return true if there's a used price, false otherwise
     */
    public boolean hasUsedPrice() {
        return mUsedPrice != null;
    }

    /**
     * @return true if there's a digital price, false otherwise
     */
    public boolean hasDigitalPrice() {
        return mDigitalPrice != null;
    }

    /**
     * @return true if there's a preorder price, false otherwise
     */
    public boolean hasPreorderPrice() {
        return mPreorderPrice != null;
    }

    /**
     * @return true if there are a older new prices, false otherwise
     */
    public boolean hasOlderNewPrices() {
        if (mOlderNewPrices == null){
            return false;
        }
        return !mOlderNewPrices.isEmpty();
    }

    /**
     * @return true if there are a older used prices, false otherwise
     */
    public boolean hasOlderUsedPrices() {
        if (mOlderUsedPrices == null){
            return false;
        }
        return !mOlderUsedPrices.isEmpty();
    }

    /**
     * @return true if there are a older digital prices, false otherwise
     */
    public boolean hasOlderDigitalPrices() {
        if (mOlderDigitalPrices == null){
            return false;
        }
        return !mOlderDigitalPrices.isEmpty();
    }

    /**
     * @return true if there are a older preorder prices, false otherwise
     */
    public boolean hasOlderPreorderPrices() {
        if (mOlderPreorderPrices == null){
            return false;
        }
        return !mOlderPreorderPrices.isEmpty();
    }

    /**
     * @param o a GamePreview object
     * @return true if two GamePreview are identical, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()){
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
        return this.getId().compareTo(game.getId());
    }

}
