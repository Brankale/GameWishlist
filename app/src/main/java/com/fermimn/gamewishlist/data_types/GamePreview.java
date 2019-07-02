package com.fermimn.gamewishlist.data_types;

import java.util.ArrayList;
import java.util.List;

public class GamePreview {

    private String m_id;
    private String m_title;
    private String m_publisher;
    private String m_platform;

    private Double m_newPrice;
    private Double m_usedPrice;
    private Double m_preorderPrice;
    private Double m_digitalPrice;

    private List<Double> m_olderNewPrices;
    private List<Double> m_olderUsedPrices;
    private List<Double> m_olderDigitalPrices;
    private List<Double> m_olderPreorderPrices;

    /**
     * Create a GamePreview object
     */
    public GamePreview() {
    }

    /**
     * Get the ID of the game
     * @return the ID of the game
     */
    // TODO : check if it can return null
    public String getId() {
        return m_id;
    }

    /**
     * Set the ID of the game
     * @param id of the game
     */
    public void setId(String id) {
        m_id = id;
    }

    /**
     * Get the title of the game
     * @return the title of the game
     */
    // TODO : check if it can return null
    public String getTitle() {
        return m_title;
    }

    /**
     * Set the title of the game
     * @param title of the game
     */
    public void setTitle(String title) {
        m_title = title;
    }

    /**
     * Get the publisher of the game
     * @return the publisher of the game
     */
    // TODO : check if it can return null
    public String getPublisher() {
        return m_publisher;
    }

    /**
     * Set the publisher of the game
     * @param publisher of the game
     */
    public void setPublisher(String publisher) {
        m_publisher = publisher;
    }

    /**
     * Get the platform of the game
     * @return the platform of the game
     */
    // TODO : check if it can return null
    public String getPlatform() {
        return m_platform;
    }

    /**
     * Set the platform of the game
     * @param platform of the game
     */
    public void setPlatform(String platform) {
        m_platform = platform;
    }

    /**
     * Get the New Price of the game
     * @return the New Price of the game
     */
    // TODO : check if it can return null
    public Double getNewPrice() {
        return m_newPrice;
    }

    /**
     * Set the New Price of the game
     * @param newPrice of the game
     */
    public void setNewPrice(Double newPrice) {
        m_newPrice = newPrice;
    }

    /**
     * Get Used Price of the game
     * @return the Used Price of the game
     */
    // TODO : check if it can return null
    public Double getUsedPrice() {
        return m_usedPrice;
    }

    /**
     * Set Used Price of the game
     * @param usedPrice o the game
     */
    public void setUsedPrice(Double usedPrice) {
        m_usedPrice = usedPrice;
    }

    /**
     * Get the Preorder Price of the game
     * @return the Preorder Price of the game
     */
    // TODO : check if it can return null
    public Double getPreorderPrice() {
        return m_preorderPrice;
    }

    /**
     * Set the Preorder Price of the game
     * @param preorderPrice of the game
     */
    public void setPreorderPrice(Double preorderPrice) {
        m_preorderPrice = preorderPrice;
    }

    /**
     * Get the Digital Price of the game
     * @return the DigitalPrice of the game
     */
    // TODO : check if it can return null
    public Double getDigitalPrice() {
        return m_digitalPrice;
    }

    /**
     * Set the Digital Price of the game
     * @param digitalPrice of the game
     */
    public void setDigitalPrice(Double digitalPrice) {
        m_digitalPrice = digitalPrice;
    }

    /**
     * Get the Older New Prices of the game
     * @return the Older New Prices of the game
     */
    // TODO : check if it can return null (pay attention to the init or set process)
    public List<Double> getOlderNewPrices() {
        return m_olderNewPrices;
    }

    /**
     * Set the Older New Prices of the game
     * @param olderNewPrices of the game
     */
    public void setOlderNewPrices(List<Double> olderNewPrices) {
        m_olderNewPrices = olderNewPrices;
    }

    /**
     * Get the Older Used Prices of the game
     * @return the Older Used Prices of the game
     */
    // TODO : check if it can return null (pay attention to the init or set process)
    public List<Double> getOlderUsedPrices() {
        return m_olderUsedPrices;
    }

    /**
     * Set the Older Used Prices of the game
     * @param olderUsedPrices of teh game
     */
    public void setOlderUsedPrices(List<Double> olderUsedPrices) {
        m_olderUsedPrices = olderUsedPrices;
    }

    /**
     * Get the Older Digital Prices of the game
     * @return the Older Digital Prices of the game
     */
    // TODO : check if it can return null (pay attention to the init or set process)
    public List<Double> getOlderDigitalPrices() {
        return m_olderDigitalPrices;
    }

    /**
     * Set the Older Digital Prices of the game
     * @param olderDigitalPrices of the game
     */
    public void setOlderDigitalPrices(List<Double> olderDigitalPrices) {
        m_olderDigitalPrices = olderDigitalPrices;
    }

    /**
     * Get the Older Preorder Prices of the game
     * @return the Older Preorder Prices of the game
     */
    // TODO : check if it can return null (pay attention to the init or set process)
    public List<Double> getOlderPreorderPrices() {
        return m_olderPreorderPrices;
    }

    /**
     * Set the Older PreorderPrices of the game
     * @param olderPreorderPrices of the game
     */
    public void setOlderPreorderPrices(List<Double> olderPreorderPrices) {
        m_olderPreorderPrices = olderPreorderPrices;
    }

    /**
     * Add a Older New Price
     * @param olderNewPrice
     */
    public void addOlderNewPrice(double olderNewPrice) {
        if (m_olderNewPrices == null){
            m_olderNewPrices = new ArrayList<>();
        }
        m_olderNewPrices.add(olderNewPrice);
    }

    /**
     * Add a Older Used Price
     * @param olderUsedPrice
     */
    public void addOlderUsedPrice(double olderUsedPrice) {
        if (m_olderUsedPrices == null){
            m_olderUsedPrices = new ArrayList<>();
        }
        m_olderUsedPrices.add(olderUsedPrice);
    }

    /**
     * Add a Older Digital Price
     * @param olderDigitalPrice
     */
    public void addOlderDigitalPrice(double olderDigitalPrice) {
        if (m_olderDigitalPrices == null){
            m_olderDigitalPrices = new ArrayList<>();
        }
        m_olderDigitalPrices.add(olderDigitalPrice);
    }

    /**
     * Add a Older Preorder Price
     * @param olderPreorderPrice
     */
    public void addOlderPreorderPrice(double olderPreorderPrice) {
        if (m_olderPreorderPrices == null){
            m_olderPreorderPrices = new ArrayList<>();
        }
        m_olderPreorderPrices.add(olderPreorderPrice);
    }

    /**
     * @return true if there's a new price, false otherwise
     */
    public boolean hasNewPrice() {
        return m_newPrice != null;
    }

    /**
     * @return true if there's a used price, false otherwise
     */
    public boolean hasUsedPrice() {
        return m_usedPrice != null;
    }

    /**
     * @return true if there's a digital price, false otherwise
     */
    public boolean hasDigitalPrice() {
        return m_digitalPrice != null;
    }

    /**
     * @return true if there's a preorder price, false otherwise
     */
    public boolean hasPreorderPrice() {
        return m_preorderPrice != null;
    }

    /**
     * @return true if there are a older new prices, false otherwise
     */
    public boolean hasOlderNewPrices() {
        if (m_olderNewPrices == null){
            return false;
        }
        return !m_olderNewPrices.isEmpty();
    }

    /**
     * @return true if there are a older used prices, false otherwise
     */
    public boolean hasOlderUsedPrices() {
        if (m_olderUsedPrices == null){
            return false;
        }
        return !m_olderUsedPrices.isEmpty();
    }

    /**
     * @return true if there are a older digital prices, false otherwise
     */
    public boolean hasOlderDigitalPrices() {
        if (m_olderDigitalPrices == null){
            return false;
        }
        return !m_olderDigitalPrices.isEmpty();
    }

    /**
     * @return true if there are a older preorder prices, false otherwise
     */
    public boolean hasOlderPreorderPrices() {
        if (m_olderPreorderPrices == null){
            return false;
        }
        return !m_olderPreorderPrices.isEmpty();
    }
}
