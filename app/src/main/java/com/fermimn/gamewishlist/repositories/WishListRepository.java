package com.fermimn.gamewishlist.repositories;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.fermimn.gamewishlist.models.Game;
import com.fermimn.gamewishlist.models.GamePreview;
import com.fermimn.gamewishlist.models.GamePreviewList;
import com.fermimn.gamewishlist.models.Promo;
import com.fermimn.gamewishlist.utils.Gamestop;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

// TODO: find a way to join WishListManager with WishListRepository
public class WishListRepository {

    @SuppressWarnings("unused")
    private static final String TAG = WishListRepository.class.getSimpleName();

    private static WishListRepository mInstance;
    private Application mContext;
    private GamePreviewList mWishList;
    private MutableLiveData<GamePreviewList> mDataSet;

    private WishListRepository(Application application) {
        mContext = application;

        File[] gamesFolders = mContext.getFilesDir().listFiles();
        mWishList = new GamePreviewList();

        if (gamesFolders != null) {
            for (File gameFolder : gamesFolders) {
                if (gameFolder.isDirectory()) {
                    String id = gameFolder.getName();
                    mWishList.add( getGameFromXml(id) );
                }
            }
        }

        mDataSet = new MutableLiveData<>();
        mDataSet.setValue(mWishList);
    }

    public static WishListRepository getInstance(Application application) {
        if (mInstance == null) {
            mInstance = new WishListRepository(application);
        }
        return mInstance;
    }

    public MutableLiveData<GamePreviewList> getWishList() {
        return mDataSet;
    }

    public Game getGame(GamePreview gamePreview) {
        return new Gamestop().downloadGame( gamePreview.getId() );
    }

    public void addGame(Game game) {
        createGameXml(game);
        downloadGameImages(game);
        mWishList.add(game);
        mDataSet.postValue(mWishList);
    }

    public void removeGame(GamePreview gamePreview) {
        deleteFolder( getGameFolder( gamePreview.getId() ) );

        for (int i = 0; i < mWishList.size(); ++i) {
            if (mWishList.get(i).equals(gamePreview)) {
                mWishList.remove(i);
                return;
            }
        }

        mDataSet.postValue(mWishList);
    }

    private File getGameXml(String gameId) {
        try {
            File file = new File(getGameFolder(gameId), "data.xml");
            return file.exists() ? file : (file.createNewFile() ? file : null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private File getGameFolder(String gameId) {
        File file = new File(mContext.getFilesDir(), gameId);
        return file.exists() ? file : ( file.mkdir() ? file : null );
    }

    private File getGameGalleryFolder(String gameId) {
        File file = new File(getGameFolder(gameId), "gallery");
        return file.exists() ? file : ( file.mkdir() ? file : null );
    }

    private boolean downloadGameImages(Game game) {

        // if true every image has been downloaded correctly
        boolean status = true;

        // download cover
        File cover = new File(getGameFolder(game.getId()), "cover.jpg");
        downloadImage(cover, game.getCover());

        // download gallery
        if (game.hasGallery()) {
            File galleryFolder = getGameGalleryFolder(game.getId());
            for (Uri uri : game.getGallery()) {
                String url = uri.toString();
                String name = url.substring( url.lastIndexOf('/') );
                File galleryImage = new File(galleryFolder, name);

                if (!downloadImage(galleryImage, uri)) {
                    status = false;
                }
            }
        }

        return status;
    }

    private boolean downloadImage(File img, Uri uri) {

        try {

            // download bitmap
            InputStream is = (InputStream) new URL(uri.toString()).getContent();
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            is.close();

            // save bitmap
            FileOutputStream fos = new FileOutputStream(img);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            return true;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void deleteFolder(@Nullable File folder) {

        if (folder == null) {
            return;
        }

        // listfiles returns null if the File is not a directory
        // or if there are errors while reading files
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String fileName = file.getName();
                    if (file.delete()) {
                        Log.d(TAG, "[" + fileName + "] - File deleted successfully");
                    } else {
                        Log.d(TAG, "[" + fileName + "] - ERROR WHILE DELETING FILE");
                    }
                } else {
                    deleteFolder(file);
                }
            }

            String folderName = folder.getName();
            if (folder.delete()) {
                Log.d(TAG, "[" + folderName + "] - Directory deleted successfully");
            } else {
                Log.d(TAG, "[" + folderName + "] - ERROR WHILE DELETING DIRECTORY");
            }
        }
    }

    private boolean createGameXml(Game game) {

        try {

            File xml = getGameXml(game.getId());

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

                for (Double price : game.getOlderPreorderPrices()) {
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
            if (game.hasOfficialWebSite()) {
                Element elementOfficialSite = doc.createElement("officialSite");
                elementOfficialSite.appendChild(doc.createCDATASection(game.getOfficialWebSite()));
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

                    Element elementValidity = doc.createElement("subHeader");
                    elementValidity.appendChild(doc.createCDATASection( p.getSubHeader() ));
                    elementPromo.appendChild(elementValidity);

                    if (p.getFindMoreMessage() != null) {
                        Element elementMessage = doc.createElement("findMoreMessage");
                        elementMessage.appendChild(doc.createCDATASection( p.getFindMoreMessage() ));
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

            // Save cover
            File cover = new File(getGameFolder(game.getId()), "cover.jpg");
            Element elementCover = doc.createElement("cover");
            elementCover.appendChild( doc.createCDATASection( Uri.fromFile(cover).toString() ));
            gameElement.appendChild(elementCover);

            // Save gallery
            if (game.hasGallery()) {
                Element elementGallery = doc.createElement("gallery");
                File galleryFolder = getGameGalleryFolder(game.getId());
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

    private Game getGameFromXml(String gameId) {

        try {

            File xml = getGameXml(gameId);
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xml);

            Element gameElement = doc.getDocumentElement();

            String id = gameElement.getAttribute("id");
            String title = gameElement.getElementsByTagName("title").item(0).getChildNodes().item(0).getTextContent();
            String platform = gameElement.getElementsByTagName("platform").item(0).getChildNodes().item(0).getTextContent();

            Game game = new Game(id, title, platform);

            game.setPublisher( gameElement.getElementsByTagName("publisher").item(0).getChildNodes().item(0).getTextContent() );


            Element prices = (Element) gameElement.getElementsByTagName("prices").item(0);

            //NEW PRICE
            org.w3c.dom.NodeList nl = prices.getElementsByTagName("newPrice");
            if (nl.getLength() > 0) {
                Element newPrice = (Element) nl.item(0);
                game.setNewPrice( Double.valueOf(newPrice.getTextContent()) );
            }

            //OLDER NEW PRICES
            nl = prices.getElementsByTagName("olderNewPrices");
            if (nl.getLength() > 0) {
                Element olderNewPrices = (Element) nl.item(0);
                nl = olderNewPrices.getElementsByTagName("price");
                for (int i = 0; i < nl.getLength(); i++) {
                    Element elementPrice = (Element) nl.item(i);
                    game.addOlderNewPrice( Double.valueOf(elementPrice.getTextContent() ));
                }
            }

            //USED PRICE
            nl = prices.getElementsByTagName("usedPrice");
            if (nl.getLength() > 0) {
                Element usedPrice = (Element) nl.item(0);
                game.setUsedPrice( Double.valueOf(usedPrice.getTextContent()) );
            }

            //OLDER USED PRICES
            nl = prices.getElementsByTagName("olderUsedPrices");
            if (nl.getLength() > 0) {
                Element olderUsedPrices = (Element) nl.item(0);
                nl = olderUsedPrices.getElementsByTagName("price");
                for (int i = 0; i < nl.getLength(); i++) {
                    Element elementPrice = (Element) nl.item(i);
                    game.addOlderUsedPrice( Double.valueOf(elementPrice.getTextContent()) );
                }
            }

            //PREORDER PRICE
            nl = prices.getElementsByTagName("preorderPrice");
            if (nl.getLength() > 0) {
                Element preorderPrice = (Element) nl.item(0);
                game.setPreorderPrice( Double.valueOf(preorderPrice.getTextContent() ));
            }

            //OLDER PREORDER PRICES
            nl = prices.getElementsByTagName("olderPreorderPrices");
            if (nl.getLength() > 0) {
                Element olderPreorderPrices = (Element) nl.item(0);
                nl = olderPreorderPrices.getElementsByTagName("price");
                for (int i = 0; i < nl.getLength(); i++) {
                    Element elementPrice = (Element) nl.item(i);
                    game.addOlderPreorderPrice( Double.valueOf(elementPrice.getTextContent()));
                }
            }

            //DIGITAL PRICE
            nl = prices.getElementsByTagName("digitalPrice");
            if (nl.getLength() > 0) {
                Element digitalPrice = (Element) nl.item(0);
                game.setDigitalPrice( Double.valueOf(digitalPrice.getTextContent()) );
            }

            //OLDER DIGITAL PRICES
            nl = prices.getElementsByTagName("olderDigitalPrices");
            if (nl.getLength() > 0) {
                Element olderDigitalPrices = (Element) nl.item(0);
                nl = olderDigitalPrices.getElementsByTagName("price");
                for (int i = 0; i < nl.getLength(); i++) {
                    Element elementPrice = (Element) nl.item(i);
                    game.addOlderDigitalPrice( Double.valueOf(elementPrice.getTextContent()));
                }
            }

            //PEGI
            nl = gameElement.getElementsByTagName("pegi");
            if (nl.getLength() > 0) {
                Element pegi = (Element) nl.item(0);
                nl = pegi.getElementsByTagName("type");
                for (int i = 0; i < nl.getLength(); i++) {
                    Element type = (Element) nl.item(i);
                    game.addPegi( type.getTextContent());
                }
            }

            //GENRES
            nl = gameElement.getElementsByTagName("genres");
            if (nl.getLength() > 0) {
                Element genres = (Element) nl.item(0);
                nl = genres.getElementsByTagName("genre");
                for (int i = 0; i < nl.getLength(); i++) {
                    Element genre = (Element) nl.item(i);
                    game.addGenre( genre.getTextContent());
                }
            }

            //OFFICIAL SITE
            nl = gameElement.getElementsByTagName("officialSite");
            if (nl.getLength() > 0) {
                Element officialSite = (Element) nl.item(0);
                game.setOfficialWebSite( officialSite.getChildNodes().item(0).getTextContent() );
            }

            //PLAYERS
            nl = gameElement.getElementsByTagName("players");
            if (nl.getLength() > 0) {
                Element players = (Element) nl.item(0);
                game.setPlayers( players.getChildNodes().item(0).getTextContent() );
            }

            //RELEASE DATE
            nl = gameElement.getElementsByTagName("releaseDate");
            if (nl.getLength() > 0) {
                Element releaseDate = (Element) nl.item(0);
                game.setReleaseDate( releaseDate.getTextContent() );
            }

            //PROMOS
            nl = gameElement.getElementsByTagName("promos");
            if (nl.getLength() > 0) {
                Element promos = (Element) nl.item(0);
                nl = promos.getElementsByTagName("promo");
                for (int i = 0; i < nl.getLength(); i++) {
                    Element promo = (Element) nl.item(i);

                    String header = promo.getElementsByTagName("header").item(0).getChildNodes().item(0).getTextContent();
                    String validity = promo.getElementsByTagName("subHeader").item(0).getChildNodes().item(0).getTextContent();

                    String message = null;
                    String messageURL = null;
                    if (promo.getElementsByTagName("findMoreMessage").getLength() > 0) {
                        message = promo.getElementsByTagName("findMoreMessage").item(0).getChildNodes().item(0).getTextContent();
                        messageURL = promo.getElementsByTagName("findMoreUrl").item(0).getChildNodes().item(0).getTextContent();
                    }

                    Promo p = new Promo(header);
                    p.setSubHeader(validity);
                    p.setFindMoreMessage(message, messageURL);

                    game.addPromo(p);
                }
            }

            //DESCRIPTION
            nl = gameElement.getElementsByTagName("description");
            if (nl.getLength() > 0) {
                Element description = (Element) nl.item(0);
                game.setDescription( description.getChildNodes().item(0).getTextContent() );
            }

            //VALID FOR PROMO
            nl = gameElement.getElementsByTagName("validForPromo");
            if (nl.getLength() > 0) {
                Element validForPromo = (Element) nl.item(0);
                game.setValidForPromotions( Boolean.valueOf(validForPromo.getTextContent()));
            }

            // set cover
            nl = gameElement.getElementsByTagName("cover");
            if (nl.getLength() > 0) {
                Element cover = (Element) nl.item(0);
                game.setCover( Uri.parse(cover.getChildNodes().item(0).getTextContent()) );
            }

            // set gallery
            nl = gameElement.getElementsByTagName("gallery");
            if (nl.getLength() > 0) {
                nl = ((Element)nl.item(0)).getElementsByTagName("image");
                for (int i = 0; i < nl.getLength(); ++i) {
                    game.addToGallery( Uri.parse(nl.item(i).getChildNodes().item(0).getTextContent() ) );
                }
            }

            return game;

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        return null;
    }

}
