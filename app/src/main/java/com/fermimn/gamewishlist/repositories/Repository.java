package com.fermimn.gamewishlist.repositories;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fermimn.gamewishlist.models.Game;
import com.fermimn.gamewishlist.models.GamePreview;
import com.fermimn.gamewishlist.models.GamePreviewList;
import com.fermimn.gamewishlist.models.Promo;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

// TODO: check Singleton
// DOCS: https://medium.com/p/de6b951dfdb0/responses/show

public class Repository {

    @SuppressWarnings("unused")
    private static final String TAG = Repository.class.getSimpleName();

    private static Repository mInstance;
    private final String FILES_DIR;
    private final MutableLiveData<GamePreviewList> mWishlist;

    /**
     * Return the instance of Repository
     * @param application Application Context
     * @return the instance of Repository
     */
    public static Repository getInstance(Application application) {
        if (mInstance == null) {
            mInstance = new Repository(application);
        }
        return mInstance;
    }

    private Repository(Application application) {
        FILES_DIR = application.getFilesDir().getAbsolutePath();
        mWishlist = new MutableLiveData<>();
        mWishlist.setValue( initWishlist() );
    }

    /**
     * Get the list of games in the wishlist
     * @return the list of games in the wishlist, an empty list if there are no games
     */
    @NonNull
    private GamePreviewList initWishlist() {

        GamePreviewList wishlist = new GamePreviewList();

        File appDir = new File(FILES_DIR);
        if (appDir.exists()) {
            String[] gameFolders = appDir.list();
            if (gameFolders != null) {
                File folder;
                for (String gameFolder : gameFolders) {
                    folder = new File( getGameXml(gameFolder) );
                    if (folder.exists()) {
                        try {
                            Game game = XmlManager.parse(folder);
                            wishlist.add(game);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        return wishlist;
    }

    /**
     * Get a Game from the ID
     * @param gameId the ID of the game
     * @return a Game object if the game is in the wishlist, null otherwise
     */
    public Game getGame(@Nullable String gameId) {
        GamePreviewList wishlist = mWishlist.getValue();
        if (wishlist != null) {
            for (GamePreview gamePreview : wishlist) {
                if (gamePreview.getId().equals(gameId)) {
                    return (Game) gamePreview;
                }
            }
        }
        return null;
    }

    /**
     * Get the list of games in the wishlist
     * @return a MutableLiveData containing the list of games in the wishlist
     *         or containing null if there are no games in the wishlist
     */
    public LiveData<GamePreviewList> getWishlist() {
        return mWishlist;
    }

    /**
     * Save the game info on local storage
     * @param game the game to save
     * @return true if the game has been saved, false otherwise
     */
    public boolean addGame(@Nullable Game game) {

        if (game != null) {
            boolean result = initGameFolder( game.getId() );
            if (result) {
                // store the game on local memory
                createXml(game);
                downloadGameImages(game);

                // adding game to the wishlist
                GamePreviewList wishlist = mWishlist.getValue();
                if (wishlist == null) {
                    wishlist = new GamePreviewList();
                }
                wishlist.add(game);
                mWishlist.postValue(wishlist);

                return true;
            } else {
                // remove files if some errors occurred
                removeGame( game.getId() );
            }
        }

        return false;
    }

    /**
     * Remove the game given its ID
     * @param gameId the ID of the game to remove
     * @return true if the game has been removed, false otherwise
     */
    public boolean removeGame(@Nullable String gameId) {
        boolean result = deleteFolder( new File( getGameFolder(gameId) ) );

        GamePreviewList wishlist = mWishlist.getValue();
        if (wishlist != null) {
            for (int i = 0; i < wishlist.size(); ++i) {
                if (wishlist.get(i).getId().equals(gameId)) {
                    wishlist.remove(i);
                    mWishlist.postValue(wishlist);
                    break;
                }
            }
        }

        if (result) {
            Log.d(TAG, '[' + getGameFolder(gameId) +  "] - folder deleted successfully");
            return true;
        } else {
            Log.e(TAG, '[' + getGameFolder(gameId) +  "] - errors occurred while deleting the folder");
            return false;
        }
    }

    /**
     * @param gameId the ID of the game of which you want to have the folder
     * @return a String with the path of the game folder given the game ID
     */
    private String getGameFolder(@Nullable String gameId) {
        return FILES_DIR + '/' + gameId;
    }

    /**
     * @param gameId the ID of the game of which you want to have the folder
     * @return a String with the path of the game gallery folder given the game ID
     */
    private String getGalleryFolder(@Nullable String gameId) {
        return getGameFolder(gameId) + "/gallery";
    }

    /**
     * @param gameId the ID of the game of which you want to have the XML
     * @return a String with the path of the game XML file given the game ID
     */
    private String getGameXml(@Nullable String gameId) {
        return getGameFolder(gameId) + "/data.xml";
    }

    /**
     * Creates the necessary folders to store game information
     * @param gameId the ID of the game which you want to save
     * @return true if folders have been created, false otherwise
     */
    private boolean initGameFolder(@Nullable String gameId) {
        // create root folder
        if (!new File( getGameFolder(gameId) ).mkdir()) {
            return false;
        }
        // create gallery folder
        return new File( getGalleryFolder(gameId) ).mkdir();
    }

    /**
     * Delete the given folder and its content
     * @param file the directory to delete
     * @return true if the directory has been deleted correctly, false otherwise
     */
    private boolean deleteFolder(@Nullable File file) {

        boolean errors = false;
        boolean result;

        if (file != null && file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File child : files) {
                    if (child.isDirectory()) {
                        result = deleteFolder(child);
                        if (!result) { errors = true; }
                    } else {
                        result = child.delete();
                        if (!result) { errors = true; }
                    }
                }
            }

            result = file.delete();
            if (!result) { errors = true; }

            return !errors;
        }

        return false;
    }

    // TODO: this method should be rewritten
    private void createXml(Game game) {
        try {

            File xml = new File( getGameXml(game.getId()) );

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

            // Element Platform and append
            Element elementPlatform = doc.createElement("platform");
            elementPlatform.appendChild(doc.createCDATASection(game.getPlatform()));
            gameElement.appendChild(elementPlatform);

            // Create Element Publisher and append
            Element elementPublisher = doc.createElement("publisher");
            elementPublisher.appendChild(doc.createCDATASection(game.getPublisher()));
            gameElement.appendChild(elementPublisher);

            // Create Element Prices
            Element prices = doc.createElement("prices");

            // Create Element NewPrice and append
            if (game.getNewPrice() != null) {
                Element elementNewPrice = doc.createElement("newPrice");
                elementNewPrice.setTextContent(String.valueOf(game.getNewPrice()));
                prices.appendChild(elementNewPrice);
            }

            // Create Element OlderNewPrices and append
            if (game.getOlderNewPrices() != null) {
                Element elementOlderNewPrice = doc.createElement("olderNewPrices");

                for (Double price : game.getOlderNewPrices()) {
                    Element elementPrice = doc.createElement("price");
                    elementPrice.setTextContent(price.toString());
                    elementOlderNewPrice.appendChild(elementPrice);
                }

                prices.appendChild(elementOlderNewPrice);
            }

            // Create Element UsedPrice and append
            if (game.getUsedPrice() != null) {
                Element elementUsedPrice = doc.createElement("usedPrice");
                elementUsedPrice.setTextContent(String.valueOf(game.getUsedPrice()));
                prices.appendChild(elementUsedPrice);
            }

            // Create Element OlderUsedPrices and append
            if (game.getOlderUsedPrices() != null) {
                Element elementOlderUsedPrice = doc.createElement("olderUsedPrices");

                for (Double price : game.getOlderUsedPrices()) {
                    Element elementPrice = doc.createElement("price");
                    elementPrice.setTextContent(price.toString());
                    elementOlderUsedPrice.appendChild(elementPrice);
                }

                prices.appendChild(elementOlderUsedPrice);
            }

            // Create Element PreorderPrice and append
            if (game.getPreorderPrice() != null) {
                Element elementPreorderPrice = doc.createElement("preorderPrice");
                elementPreorderPrice.setTextContent(String.valueOf(game.getPreorderPrice()));
                prices.appendChild(elementPreorderPrice);
            }

            // Element OlderPreorderPrices and append
            if (game.getOlderPreorderPrices() != null) {
                Element elementOlderPreorderPrice = doc.createElement("olderPreorderPrices");

                for (Double price : game.getOlderPreorderPrices()) {
                    Element elementPrice = doc.createElement("price");
                    elementPrice.setTextContent(price.toString());
                    elementOlderPreorderPrice.appendChild(elementPrice);
                }

                prices.appendChild(elementOlderPreorderPrice);
            }

            // Element DigitalPrice and append
            if (game.getDigitalPrice() != null) {
                Element elementDigitalPrice = doc.createElement("digitalPrice");
                elementDigitalPrice.setTextContent(String.valueOf(game.getDigitalPrice()));
                prices.appendChild(elementDigitalPrice);
            }

            // Element OlderDigitalPrices and append
            if (game.getOlderDigitalPrices() != null) {
                Element elementOlderDigitalPrice = doc.createElement("olderDigitalPrices");

                for (Double price : game.getOlderDigitalPrices()) {
                    Element elementPrice = doc.createElement("price");
                    elementPrice.setTextContent(price.toString());
                    elementOlderDigitalPrice.appendChild(elementPrice);
                }

                prices.appendChild(elementOlderDigitalPrice);
            }

            gameElement.appendChild(prices);

            // Element Pegi and append
            if (game.getPegi() != null) {
                Element elementPegiList = doc.createElement("pegi");
                for (String p : game.getPegi()) {
                    Element elementPegi = doc.createElement("type");
                    elementPegi.setTextContent(p);
                    elementPegiList.appendChild(elementPegi);
                }
                gameElement.appendChild(elementPegiList);
            }

            // Element Genres and append
            if (game.getGenres() != null) {
                Element elementGenres = doc.createElement("genres");

                for (String genre : game.getGenres()) {
                    Element elementGenre = doc.createElement("genre");
                    elementGenre.appendChild(doc.createCDATASection(genre));
                    elementGenres.appendChild(elementGenre);
                }

                gameElement.appendChild(elementGenres);
            }

            // Element OfficialSite and append
            if (game.getOfficialWebSite() != null) {
                Element elementOfficialSite = doc.createElement("officialSite");
                elementOfficialSite.appendChild(doc.createCDATASection(game.getOfficialWebSite()));
                gameElement.appendChild(elementOfficialSite);
            }

            // Element Players and append
            if (game.getPlayers() != null) {
                Element elementPlayers = doc.createElement("players");
                elementPlayers.appendChild(doc.createCDATASection(game.getPlayers()));
                gameElement.appendChild(elementPlayers);
            }

            // Create Element ReleaseDate and append
            Element elementReleaseDate = doc.createElement("releaseDate");
            elementReleaseDate.setTextContent(game.getReleaseDate());
            gameElement.appendChild(elementReleaseDate);

            // Create Promo and append
            if (game.getPromo() != null) {
                Element elementPromos = doc.createElement("promos");

                for (Promo p : game.getPromo()) {
                    Element elementPromo = doc.createElement("promo");

                    Element elementHeader = doc.createElement("header");
                    elementHeader.appendChild(doc.createCDATASection(p.getHeader()));
                    elementPromo.appendChild(elementHeader);

                    Element elementValidity = doc.createElement("subHeader");
                    elementValidity.appendChild(doc.createCDATASection( p.getSubHeader() ));
                    elementPromo.appendChild(elementValidity);

                    if (p.getFindMoreMsg() != null) {
                        Element elementMessage = doc.createElement("findMoreMsg");
                        elementMessage.appendChild(doc.createCDATASection( p.getFindMoreMsg() ));
                        elementPromo.appendChild(elementMessage);

                        Element elementMessageURL = doc.createElement("findMoreUrl");
                        elementMessageURL.appendChild(doc.createCDATASection( p.getFindMoreUrl() ));
                        elementPromo.appendChild(elementMessageURL);
                    }

                    elementPromos.appendChild(elementPromo);
                }

                gameElement.appendChild(elementPromos);
            }

            // Create Element Description and append
            if (game.getDescription() != null) {
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

            // Save cover
            File cover = new File(getGameFolder(game.getId()), "cover.jpg");
            Element elementCover = doc.createElement("cover");
            elementCover.appendChild( doc.createCDATASection( Uri.fromFile(cover).toString() ));
            gameElement.appendChild(elementCover);

            // Save gallery
            if (game.getGallery() != null) {
                Element elementGallery = doc.createElement("gallery");
                File galleryFolder = new File( getGalleryFolder(game.getId()) );
                for (Uri uri : game.getGallery()) {

                    String url = uri.toString();
                    String name = url.substring( url.lastIndexOf('/') );
                    File imgPath = new File(galleryFolder, name);

                    Element elementImage = doc.createElement("image");
                    elementImage.appendChild( doc.createCDATASection( Uri.fromFile(imgPath).toString() ));
                    elementGallery.appendChild(elementImage);
                }
                gameElement.appendChild(elementGallery);
            }

            // Append the root element to the root node
            doc.appendChild(gameElement);

            // Create the XML file
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(new DOMSource(doc), new StreamResult(xml));

        } catch (ParserConfigurationException | TransformerException e) {
            // TODO: if this exception occurs, restore a backup
            e.printStackTrace();
        }
    }

    // TODO: this method should be rewritten
    // TODO: use DownloadManager to handle images download
    //       otherwise images cannot be downloaded if the user close the app
    private void downloadGameImages(final Game game) {

        new Thread() {

            public void run() {
                // download cover
                File cover = new File(getGameFolder(game.getId()), "cover.jpg");
                downloadImage(cover, game.getCover());

                // download gallery
                if (game.getGallery() != null) {
                    File galleryFolder = new File( getGalleryFolder(game.getId()) );
                    for (Uri uri : game.getGallery()) {
                        String url = uri.toString();
                        String name = url.substring( url.lastIndexOf('/') );
                        File galleryImage = new File(galleryFolder, name);
                        downloadImage(galleryImage, uri);
                    }
                }
            }

        }.start();

    }

    // TODO: this method should be rewritten
    private void downloadImage(File img, Uri uri) {
        try {

            // download bitmap
            InputStream is = (InputStream) new URL(uri.toString()).getContent();
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            is.close();

            // save bitmap
            FileOutputStream fos = new FileOutputStream(img);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
