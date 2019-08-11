package com.fermimn.gamewishlist.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.data_types.Game;
import com.fermimn.gamewishlist.data_types.GamePreview;
import com.fermimn.gamewishlist.data_types.Promo;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class WishlistManager {

    @SuppressWarnings("unused")
    private static final String TAG = WishlistManager.class.getSimpleName();

    private static WishlistManager ourInstance;

    private final Context mContext;

    private final String mDataDir;

    private List<GamePreview> mWishlist;

    public static WishlistManager getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new WishlistManager(context);
        }
        return ourInstance;
    }

    private WishlistManager(Context context) {
        // init app directory
        mContext = context;
        mDataDir = mContext.getApplicationInfo().dataDir;
        mWishlist = new ArrayList<>();
    }

    public void addGameToWishList(Context context, GamePreview gamePreview) {

        new AlertDialog.Builder(context)
                .setTitle( "Errore" )
                .setMessage( "Non è stato possibile aggiungere il gioco alla wishlist" )

                // A null listener allows the button to dismiss the dialog and take no further action
                .setNegativeButton(android.R.string.ok, null)
                .show();
    }

    public void addGameToWishList(Game game) {

        if (!initGameXml(game)) {

        }

        if (!initWishlistCsv()) {

        }

        mWishlist.add(game);
    }

    public boolean removeGameFromWishlist(GamePreview gamePreview) {
        return false;
    }

    public List<GamePreview> getWishlist() {
        return mWishlist;
    }

    /**
     * Get the csv where you can read the wishlist or make some changes to the wishlist
     * @return the csv file, null if it's impossible creating the file
     */
    private File getWishlistCsv() {
        // create the folder if not already exists
        File csv = new File(mDataDir + "/wishlist.csv");
        return csv.exists() ? csv : (csv.mkdir() ? csv : null);
    }

    /**
     * Get the game root folder where you can save game info
     * @param gamePreview the game to save
     * @return the root folder, null if it's impossible creating the folder
     */
    private File getGameRootFolder(GamePreview gamePreview) {
        // create the folder if not already exists
        File root = new File(mDataDir + "/" + gamePreview.getId());
        return root.exists() ? root : (root.mkdir() ? root : null);
    }

    /**
     * Get the game gallery folder where you can save gallery images
     * @param gamePreview the game to save
     * @return the gallery folder, null if it's impossible creating the folder
     */
    private File getGameGalleryFolder(GamePreview gamePreview) {

        // Get game root folder and check if it's null
        File root = getGameRootFolder(gamePreview);

        if (root == null) {
            return null;
        }

        // create the folder if not already exists
        File gallery = new File(root.getPath() + "/gallery");
        return gallery.exists() ? gallery : (gallery.mkdir() ? gallery : null);
    }

    /**
     * Get the game xml where you can read or save game data
     * @param gamePreview the game to save
     * @return the xml file, null if it's impossible creating the file
     */
    private File getGameXml(GamePreview gamePreview) {

        // Get game root folder and check if it's null
        File root = getGameRootFolder(gamePreview);

        if (root == null) {
            return null;
        }

        // create the xml file if not already exists
        File xml = new File(root.getPath() + "/data.xml");
        return xml.exists() ? xml : (xml.mkdir() ? xml : null);
    }

    /**
     * Create a CSV file where are stored game IDs in the wishlist
     * @return true if the file was created successfully, false otherwise
     */
    private boolean initWishlistCsv() {

        File csv = getWishlistCsv();

        if (csv == null) {
            return false;
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(csv));

            StringBuilder stringBuilder = new StringBuilder();
            for (GamePreview gamePreview : mWishlist) {
                stringBuilder.append(gamePreview.getId()).append(";");
            }

            writer.append(stringBuilder);
            writer.close();

            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Create a XML file where game data are stored
     * @param game the game to save
     * @return true if the file was created successfully, false otherwise
     */
    private boolean initGameXml(Game game) {

        try {

            File xml = getGameXml(game);

            if (xml == null) {
                return false;
            }

            // Create Document
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

            // Create the root element
            Element gameElement = doc.createElement("game");

            // Set attribute ID
            gameElement.setAttribute("id", game.getId());

            // Create Element Title and append
            Element elementTitle = doc.createElement("title");
            elementTitle.appendChild(doc.createCDATASection(game.getTitle()));
            gameElement.appendChild(elementTitle);

            // Create Element Publisher and append
            Element elementPublisher = doc.createElement("publisher");
            elementPublisher.appendChild(doc.createCDATASection(game.getPublisher()));
            gameElement.appendChild(elementPublisher);

            // Element Platform and append
            Element elementPlatform = doc.createElement("platform");
            elementPlatform.appendChild(doc.createCDATASection(game.getPlatform()));
            gameElement.appendChild(elementPlatform);

            // Create Element Prices
            Element prices = doc.createElement("prices");

            // Create Element NewPrice and append
            if (game.hasNewPrice()) {
                Element elementNewPrice = doc.createElement("newPrice");
                elementNewPrice.setTextContent(String.valueOf(game.getNewPrice()));
                prices.appendChild(elementNewPrice);
            }

            // Create Element OlderNewPrices and append
            if (game.hasOlderNewPrices()) {
                Element elementOlderNewPrice = doc.createElement("olderNewPrices");

                for (Double price : game.getOlderNewPrices()) {
                    Element elementPrice = doc.createElement("price");
                    elementPrice.setTextContent(price.toString());
                    elementOlderNewPrice.appendChild(elementPrice);
                }

                prices.appendChild(elementOlderNewPrice);
            }

            // Create Element UsedPrice and append
            if (game.hasUsedPrice()) {
                Element elementUsedPrice = doc.createElement("usedPrice");
                elementUsedPrice.setTextContent(String.valueOf(game.getUsedPrice()));
                prices.appendChild(elementUsedPrice);
            }

            // Create Element OlderUsedPrices and append
            if (game.hasOlderUsedPrices()) {
                Element elementOlderUsedPrice = doc.createElement("olderUsedPrices");

                for (Double price : game.getOlderUsedPrices()) {
                    Element elementPrice = doc.createElement("price");
                    elementPrice.setTextContent(price.toString());
                    elementOlderUsedPrice.appendChild(elementPrice);
                }

                prices.appendChild(elementOlderUsedPrice);
            }

            // Create Element PreorderPrice and append
            if (game.hasPreorderPrice()) {
                Element elementPreorderPrice = doc.createElement("preorderPrice");
                elementPreorderPrice.setTextContent(String.valueOf(game.getPreorderPrice()));
                prices.appendChild(elementPreorderPrice);
            }

            // Element OlderPreorderPrices and append
            if (game.hasOlderPreorderPrices()) {
                Element elementOlderPreorderPrice = doc.createElement("olderPreorderPrices");

                for (Double price : game.getOlderDigitalPrices()) {
                    Element elementPrice = doc.createElement("price");
                    elementPrice.setTextContent(price.toString());
                    elementOlderPreorderPrice.appendChild(elementPrice);
                }

                prices.appendChild(elementOlderPreorderPrice);
            }

            // Element DigitalPrice and append
            if (game.hasDigitalPrice()) {
                Element elementDigitalPrice = doc.createElement("digitalPrice");
                elementDigitalPrice.setTextContent(String.valueOf(game.getDigitalPrice()));
                prices.appendChild(elementDigitalPrice);
            }

            // Element OlderDigitalPrices and append
            if (game.hasOlderDigitalPrices()) {
                Element elementolderDigitalPrice = doc.createElement("olderDigitalPrices");

                for (Double price : game.getOlderDigitalPrices()) {
                    Element elementPrice = doc.createElement("price");
                    elementPrice.setTextContent(price.toString());
                    elementolderDigitalPrice.appendChild(elementPrice);
                }

                prices.appendChild(elementolderDigitalPrice);
            }

            gameElement.appendChild(prices);

            // Element Pegi and append
            if (game.hasPegi()) {
                Element elementPegiList = doc.createElement("pegi");
                for (String p : game.getPegi()) {
                    Element elementPegi = doc.createElement("type");
                    elementPegi.setTextContent(p);
                    elementPegiList.appendChild(elementPegi);
                }
                gameElement.appendChild(elementPegiList);
            }

            // Element Genres and append
            if (game.hasGenres()) {
                Element elementGenres = doc.createElement("genres");

                for (String genre : game.getGenres()) {
                    Element elementGenre = doc.createElement("genre");
                    elementGenre.appendChild(doc.createCDATASection(genre));
                    elementGenres.appendChild(elementGenre);
                }

                gameElement.appendChild(elementGenres);
            }

            // Element OfficialSite and append
            if (game.hasOfficialSite()) {
                Element elementOfficialSite = doc.createElement("officialSite");
                elementOfficialSite.appendChild(doc.createCDATASection(game.getOfficialSite()));
                gameElement.appendChild(elementOfficialSite);
            }

            // Element Players and append
            if (game.hasPlayers()) {
                Element elementPlayers = doc.createElement("players");
                elementPlayers.appendChild(doc.createCDATASection(game.getPlayers()));
                gameElement.appendChild(elementPlayers);
            }

            // Create Element ReleaseDate and append
            Element elementReleaseDate = doc.createElement("releaseDate");
            elementReleaseDate.setTextContent(game.getReleaseDate());
            gameElement.appendChild(elementReleaseDate);

            // Create Promo and append
            if (game.hasPromo()) {
                Element elementPromos = doc.createElement("promos");

                for (Promo p : game.getPromo()) {
                    Element elementPromo = doc.createElement("promo");

                    Element elementHeader = doc.createElement("header");
                    elementHeader.appendChild(doc.createCDATASection(p.getHeader()));
                    elementPromo.appendChild(elementHeader);

                    Element elementValidity = doc.createElement("validity");
                    elementValidity.appendChild(doc.createCDATASection(p.getValidity()));
                    elementPromo.appendChild(elementValidity);

                    if (p.getMessage() != null) {
                        Element elementMessage = doc.createElement("message");
                        elementMessage.appendChild(doc.createCDATASection(p.getMessage()));
                        elementPromo.appendChild(elementMessage);

                        Element elementMessageURL = doc.createElement("messageURL");
                        elementMessageURL.appendChild(doc.createCDATASection(p.getMessageURL()));
                        elementPromo.appendChild(elementMessageURL);
                    }

                    elementPromos.appendChild(elementPromo);
                }

                gameElement.appendChild(elementPromos);
            }

            // Create Element Description and append
            if (game.hasDescription()) {
                Element elementDescription = doc.createElement("description");
                elementDescription.appendChild(doc.createCDATASection(game.getDescription()));
                gameElement.appendChild(elementDescription);
            }

            // Create Element ValidForPromos and append
            if (game.isValidForPromotions()) {
                Element elementValidForPromo = doc.createElement("validForPromo");
                elementValidForPromo.setTextContent("" + game.isValidForPromotions());
                gameElement.appendChild(elementValidForPromo);
            }

            // TODO: save images

            // Append the root element to the root node
            doc.appendChild(gameElement);

            // Create the XML file
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(new DOMSource(doc), new StreamResult(xml));

            return true;

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerConfigurationException tce) {
            tce.printStackTrace();
        } catch (TransformerException te) {
            // TODO: if this exception occurs, restore a backup
            te.printStackTrace();
        }

        return false;
    }

}
