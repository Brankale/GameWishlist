package com.fermimn.gamewishlist.utils;

import android.content.Context;
import android.util.Log;

import com.fermimn.gamewishlist.data_types.Game;
import com.fermimn.gamewishlist.data_types.GamePreviewList;

import com.fermimn.gamewishlist.data_types.Promo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class DirectoryManager {

    private static final String TAG = DirectoryManager.class.getSimpleName();

    private Context mContext;

    public DirectoryManager(Context context){
        mContext = context;
    }

    public void saveGame(Game game) throws TransformerException, ParserConfigurationException {

        // Create Document
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

        // Create the root element
        Element gameElement = doc.createElement("game");

        // Set attribute ID
        gameElement.setAttribute("id", game.getId());

        // Create Element Title and append
        Element elementTitle = doc.createElement("title");
        elementTitle.appendChild( doc.createCDATASection( game.getTitle() ) );
        gameElement.appendChild(elementTitle);

        // Create Element Publisher and append
        Element elementPublisher = doc.createElement("publisher");
        elementPublisher.appendChild( doc.createCDATASection( game.getPublisher() ) );
        gameElement.appendChild(elementPublisher);

        // Element Platform and append
        Element elementPlatform = doc.createElement("platform");
        elementPlatform.appendChild( doc.createCDATASection( game.getPlatform() ) );
        gameElement.appendChild(elementPlatform);

        // Create Element Prices
        Element prices = doc.createElement("prices");

        // Create Element NewPrice and append
        if (game.hasNewPrice()) {
            Element elementNewPrice = doc.createElement("newPrice");
            elementNewPrice.setTextContent( String.valueOf( game.getNewPrice() ) );
            prices.appendChild(elementNewPrice);
        }

        // Create Element OlderNewPrices and append
        if (game.hasOlderNewPrices()) {
            Element elementOlderNewPrice = doc.createElement("olderNewPrices");

            for (Double price : game.getOlderNewPrices()) {
                Element elementPrice = doc.createElement("price");
                elementPrice.setTextContent( price.toString() );
                elementOlderNewPrice.appendChild(elementPrice);
            }

            prices.appendChild(elementOlderNewPrice);
        }

        // Create Element UsedPrice and append
        if (game.hasUsedPrice()) {
            Element elementUsedPrice = doc.createElement("usedPrice");
            elementUsedPrice.setTextContent( String.valueOf( game.getUsedPrice() ) );
            prices.appendChild(elementUsedPrice);
        }

        // Create Element OlderUsedPrices and append
        if (game.hasOlderUsedPrices()) {
            Element elementOlderUsedPrice = doc.createElement("olderUsedPrices");

            for (Double price : game.getOlderUsedPrices()) {
                Element elementPrice = doc.createElement("price");
                elementPrice.setTextContent( price.toString() );
                elementOlderUsedPrice.appendChild(elementPrice);
            }

            prices.appendChild(elementOlderUsedPrice);
        }

        // Create Element PreorderPrice and append
        if (game.hasPreorderPrice()) {
            Element elementPreorderPrice = doc.createElement("preorderPrice");
            elementPreorderPrice.setTextContent( String.valueOf( game.getPreorderPrice() ) );
            prices.appendChild(elementPreorderPrice);
        }

        // Element OlderPreorderPrices and append
        if (game.hasOlderPreorderPrices()) {
            Element elementOlderPreorderPrice = doc.createElement("olderPreorderPrices");

            for (Double price : game.getOlderDigitalPrices()) {
                Element elementPrice = doc.createElement("price");
                elementPrice.setTextContent( price.toString() );
                elementOlderPreorderPrice.appendChild(elementPrice);
            }

            prices.appendChild(elementOlderPreorderPrice);
        }

        // Element DigitalPrice and append
        if (game.hasDigitalPrice()) {
            Element elementDigitalPrice = doc.createElement("digitalPrice");
            elementDigitalPrice.setTextContent( String.valueOf( game.getDigitalPrice() ) );
            prices.appendChild(elementDigitalPrice);
        }

        // Element OlderDigitalPrices and append
        if (game.hasOlderDigitalPrices()) {
            Element elementolderDigitalPrice = doc.createElement("olderDigitalPrices");

            for (Double price : game.getOlderDigitalPrices()) {
                Element elementPrice = doc.createElement("price");
                elementPrice.setTextContent( price.toString() );
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
                elementGenre.appendChild( doc.createCDATASection(genre) );
                elementGenres.appendChild(elementGenre);
            }

            gameElement.appendChild(elementGenres);
        }

        // Element OfficialSite and append
        if (game.hasOfficialSite()) {
            Element elementOfficialSite = doc.createElement("officialSite");
            elementOfficialSite.appendChild( doc.createCDATASection( game.getOfficialSite() ) );
            gameElement.appendChild(elementOfficialSite);
        }

        // Element Players and append
        if (game.hasPlayers()) {
            Element elementPlayers = doc.createElement("players");
            elementPlayers.appendChild( doc.createCDATASection( game.getPlayers() ) );
            gameElement.appendChild(elementPlayers);
        }

        // Create Element ReleaseDate and append
        Element elementReleaseDate = doc.createElement("releaseDate");
        elementReleaseDate.setTextContent( game.getReleaseDate() );
        gameElement.appendChild(elementReleaseDate);

        // Create Promo and append
        if (game.hasPromo()) {
            Element elementPromos = doc.createElement("promos");

            for (Promo p : game.getPromo()) {
                Element elementPromo = doc.createElement("promo");

                Element elementHeader = doc.createElement("header");
                elementHeader.appendChild( doc.createCDATASection( p.getHeader() ) );
                elementPromo.appendChild(elementHeader);

                Element elementValidity = doc.createElement("validity");
                elementValidity.appendChild( doc.createCDATASection( p.getValidity() ) );
                elementPromo.appendChild(elementValidity);

                if (p.getMessage() != null) {
                    Element elementMessage = doc.createElement("message");
                    elementMessage.appendChild( doc.createCDATASection( p.getMessage() ) );
                    elementPromo.appendChild(elementMessage);

                    Element elementMessageURL = doc.createElement("messageURL");
                    elementMessageURL.appendChild( doc.createCDATASection( p.getMessageURL() ) );
                    elementPromo.appendChild(elementMessageURL);
                }

                elementPromos.appendChild(elementPromo);
            }

            gameElement.appendChild(elementPromos);
        }

        // Create Element Description and append
        if (game.hasDescription()) {
            Element elementDescription = doc.createElement("description");
            elementDescription.appendChild( doc.createCDATASection( game.getDescription() ) );
            gameElement.appendChild(elementDescription);
        }

        // Create Element ValidForPromos and append
        if (game.isValidForPromotions()) {
            Element elementValidForPromo = doc.createElement("validForPromo");
            elementValidForPromo.setTextContent( "" + game.isValidForPromotions() );
            gameElement.appendChild(elementValidForPromo);
        }

        // Append the root element to the root node
        doc.appendChild(gameElement);

        // Create the XML file
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        //File f = getGameXML(game.getId());
        //transformer.transform( new DOMSource(doc), new StreamResult(f) );
    }

    public void deleteGame(Game game) {
        Log.d(TAG, "to implement");
    }

    public void saveGames(GamePreviewList gamePreviewList) {
        Log.d(TAG, "to implement");
    }

    public void getWishList() {
        Log.d(TAG, "to implement");
    }

}
