package com.fermimn.gamewishlist.data_types;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GamePreview {

    private String mId;
    private String mTitle;
    private String mPublisher;
    private String mPlatform;
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
     * @return the ID of the game
     */
    // TODO : check if it can return null
    public String getId() {
        return mId;
    }

    /**
     * Set the ID of the game
     * @param id of the game
     */
    public void setId(String id) {
        mId = id;
    }

    /**
     * @return the title of the game
     */
    // TODO : check if it can return null
    public String getTitle() {
        return mTitle;
    }

    /**
     * Set the title of the game
     * @param title of the game
     */
    public void setTitle(String title) {
        mTitle = title;
    }

    /**
     * @return the publisher of the game
     */
    // TODO : check if it can return null
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
     * @return the platform of the game
     */
    // TODO : check if it can return null
    public String getPlatform() {
        return mPlatform;
    }

    /**
     * Set the platform of the game
     * @param platform of the game
     */
    public void setPlatform(String platform) {
        mPlatform = platform;
    }

    /**
     * @return a Uri representing the cover. The Uri contains a link if anything is found
     * offline, otherwise is an offline resource
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
     * @return the New Price of the game
     */
    // TODO : check if it can return null
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
     * @return the Used Price of the game
     */
    // TODO : check if it can return null
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
     * @return the Preorder Price of the game
     */
    // TODO : check if it can return null
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
     * @return the DigitalPrice of the game
     */
    // TODO : check if it can return null
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
     * @return the Older New Prices of the game
     */
    // TODO : check if it can return null (pay attention to the init or set process)
    public List<Double> getOlderNewPrices() {
        return mOlderNewPrices;
    }

    /**
     * Set the Older New Prices of the game
     * @param olderNewPrices of the game
     */
    public void setOlderNewPrices(List<Double> olderNewPrices) {
        mOlderNewPrices = olderNewPrices;
    }

    /**
     * @return the Older Used Prices of the game
     */
    // TODO : check if it can return null (pay attention to the init or set process)
    public List<Double> getOlderUsedPrices() {
        return mOlderUsedPrices;
    }

    /**
     * Set the Older Used Prices of the game
     * @param olderUsedPrices of teh game
     */
    public void setOlderUsedPrices(List<Double> olderUsedPrices) {
        mOlderUsedPrices = olderUsedPrices;
    }

    /**
     * @return the Older Digital Prices of the game
     */
    // TODO : check if it can return null (pay attention to the init or set process)
    public List<Double> getOlderDigitalPrices() {
        return mOlderDigitalPrices;
    }

    /**
     * Set the Older Digital Prices of the game
     * @param olderDigitalPrices of the game
     */
    public void setOlderDigitalPrices(List<Double> olderDigitalPrices) {
        mOlderDigitalPrices = olderDigitalPrices;
    }

    /**
     * @return the Older Preorder Prices of the game
     */
    // TODO : check if it can return null (pay attention to the init or set process)
    public List<Double> getOlderPreorderPrices() {
        return mOlderPreorderPrices;
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

}
