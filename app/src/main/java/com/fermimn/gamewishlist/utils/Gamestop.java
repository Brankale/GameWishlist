package com.fermimn.gamewishlist.utils;

import android.net.Uri;
import android.util.Log;
import android.util.Pair;

import com.fermimn.gamewishlist.data_types.Game;
import com.fermimn.gamewishlist.data_types.GameException;
import com.fermimn.gamewishlist.data_types.GamePreview;
import com.fermimn.gamewishlist.data_types.GamePreviewList;
import com.fermimn.gamewishlist.data_types.Promo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

// TODO: searchGame() & downloadGame() must call use an AsyncTask
//       otherwise you must do use AsyncTask in the caller class

// TODO: try to make the class static

public class Gamestop implements Store {

    private static final String TAG = Gamestop.class.getSimpleName();

    private static String WEBSITE_URL = "https://www.gamestop.it/SearchResult/QuickSearch?q=";

    /**
     * Search games on Gamestop website
     * @param searchedGame a String with the name of a game
     * @return the list of the games found
     * @throws IOException if the connection fails
     * @throws IOException if the searchedGame cannot be encoded
     */
    @Override
    public GamePreviewList searchGame(String searchedGame) throws IOException {

        String url = WEBSITE_URL + URLEncoder.encode(searchedGame, "UTF-8");

        // get the HTML
        Document doc = Jsoup.connect(url).get();
        Element body = doc.body();

        // get the list of the games
        Elements gamesList = body.getElementsByClass("singleProduct");

        // if there are no games
        if (gamesList.isEmpty()) {
            return null;
        }

        GamePreviewList results = new GamePreviewList();

        // save the games in the array
        for (Element game : gamesList) {

            GamePreview gamePreview = new GamePreview();

            // get & set main info
            String id = game.getElementsByClass("prodImg").get(0).attr("href").split("/")[3];
            String title = game.getElementsByTag("h3").get(0).text();
            String publisher = game.getElementsByTag("h4").get(0).getElementsByTag("strong").text();
            String platform = game.getElementsByTag("h4").get(0).textNodes().get(0).text().trim();

            gamePreview.setId(id);
            gamePreview.setTitle(title);
            gamePreview.setPublisher(publisher);
            gamePreview.setPlatform(platform);

            // get & set prices
            Pair<Double, List<Double>> categoryPrices;

            categoryPrices = getCategoryPrices(game, "buyNew");         // new
            gamePreview.setNewPrice(categoryPrices.first);
            gamePreview.setOlderNewPrices(categoryPrices.second);

            categoryPrices = getCategoryPrices(game, "buyUsed");        // used
            gamePreview.setUsedPrice(categoryPrices.first);
            gamePreview.setOlderUsedPrices(categoryPrices.second);

            categoryPrices = getCategoryPrices(game, "buyPresell");     // preorder
            gamePreview.setPreorderPrice(categoryPrices.first);
            gamePreview.setOlderPreorderPrices(categoryPrices.second);

            categoryPrices = getCategoryPrices(game, "buyDLC");         // digital
            gamePreview.setDigitalPrice(categoryPrices.first);
            gamePreview.setOlderDigitalPrices(categoryPrices.second);

            // set the Cover
            String imageUrl = game.getElementsByClass("prodImg").get(0)
                    .getElementsByTag("img").get(0).attr("data-llsrc");
            gamePreview.setCover( Uri.parse(imageUrl) );

            // add the game to the array
            results.add(gamePreview);
        }

        return results;
    }

    /**
     * Download a Game from Gamestop given the id
     * @param id of teh game
     * @return a Game object if found, otherwise null
     */
    @Override
    public Game downloadGame(String id) {

        try {

            // Get the URL and enstablish the connection
            String url = GamePreview.getUrlById(id);
            Document html = Jsoup.connect(url).get();

            // Init the Game
            Game game = new Game();
            game.setId(id);
            updateMainInfo(html.body(), game);
            updateMetadata(html.body(), game);
            updatePrices(html.body(), game);
            updatePegi(html.body(), game);
            updateCover(html.body(), game);
            updateGallery(html.body(), game);
            updatePromos(html.body(), game);
            // TODO: update Description

            return game;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method is used by searchGame()
     * It's used to get the prices of a game
     * @param element is a Jsoup Element which contains the HTML
     * @param category is the HTML classes which contains prices of a category
     * @return a Pair: first is the price, second are the olderPrices
     */
    private Pair<Double, List<Double>> getCategoryPrices(Element element, String category) {

        // search the category
        Elements e = element.getElementsByClass(category);

        // if the category hasn't been found
        if (e == null){
            return new Pair<>(null, null);
        }

        Double price = null;
        List<Double> olderPrices = new ArrayList<>();

        if (!e.isEmpty()) {
            // <em> tag is present only if there are multiple prices
            Elements foundPrices = e.get(0).getElementsByTag("em");

            // if there's just one price
            if (foundPrices.isEmpty()) {
                String strPrice = e.get(0).text();
                price = stringToPrice(strPrice);
            }

            // if more than one price is present
            for (int i = 0; i < foundPrices.size(); ++i) {
                String strPrice = foundPrices.get(i).text();

                if (i == 0) {
                    price = stringToPrice(strPrice);
                } else {
                    olderPrices.add( stringToPrice(strPrice) );
                }
            }
        }

        return new Pair<>(price, olderPrices);
    }

    /**
     * Convert a string to a price
     * @param price the string
     * @return a double that represents the price
     */
    private static double stringToPrice(String price) {

        // example string "Nuovo 19.99€"

        // remove all the characters except for numbers, ',' and '.'
        price = price.replaceAll("[^0-9.,]","");
        // to handle prices over 999,99€ like 1.249,99€
        price = price.replace(".", "");
        // to convert the price in a string that can be parsed
        price = price.replace(',', '.');

        return Double.parseDouble(price);
    }

    /**
     * Used by downloadGame() method to set: title, publisher & platform of a Game object
     * @param prodTitle it's an Element containing a class called "prodTitle"
     * @param game the object where the method store parameters
     */
    private void updateMainInfo(Element prodTitle, Game game) {

        // if the element hasn't got the class name "prodTitle"
        if (!prodTitle.className().equals("prodTitle")) {
            // search for a tag with this class name
            if (prodTitle.getElementsByClass("prodTitle").isEmpty()) {
                throw new GameException();
            }

            // TODO: improve this part of code
            prodTitle = prodTitle.getElementsByClass("prodTitle").get(0);
        }

        // set parameters
        game.setTitle( prodTitle.getElementsByTag("h1").text() );
        game.setPublisher( prodTitle.getElementsByTag("strong").text() );
        game.setPlatform( prodTitle.getElementsByTag("p").get(0)
                .getElementsByTag("span").text() );
    }

    /**
     * Used by downloadGame() method to set: genre, official site,
     * players and release date of a Game object
     * @param addedDetInfo it's an Element containing a class called "addedDetInfo"
     * @param game the object where the method store parameters
     */
    private void updateMetadata(Element addedDetInfo, Game game) {

        // if the element hasn't got the class name "addedDetInfo"
        if (!addedDetInfo.className().equals("addedDetInfo")) {
            // search for a tag with this class name
            if (addedDetInfo.getElementsByClass("addedDetInfo").isEmpty()) {
                throw new GameException();
            }

            // TODO: improve this part of code
            addedDetInfo = addedDetInfo.getElementsByClass("addedDetInfo").get(0);
        }

        for (Element e : addedDetInfo.getElementsByTag("p")) {

            // important check to avoid IndexOutOfBound Exception
            // TODO: check why you need this if
            if (e.childNodeSize() > 1) {

                switch (e.child(0).text()) {

                    // set Genre attribute
                    case "Genere":
                        String strGenres = e.child(1).text();    // return example: Action/Adventure
                        for (String genre : strGenres.split("/")) {
                            game.addGenre(genre);
                        }
                        break;

                    // set Official Site attribute
                    case "Sito Ufficiale":
                        game.setOfficialSite( e.child(1).getElementsByTag("a")
                                .attr("href") );
                        break;

                    // set Players attribute
                    case "Giocatori":
                        game.setPlayers( e.child(1).text() );
                        break;

                    // set ReleaseDate attribute
                    case "Rilascio":
                        // replace is used to make the date comparable
                        game.setReleaseDate( e.child(1).text().replace(".", "/") );
                        break;

                    default:
                        break;
                }
            }
        }

        // set ValidForPromotions attribute
        if ( !addedDetInfo.getElementsByClass("ProdottoValido").isEmpty() ) {
            game.setValidForPromotions(true);
        }
    }

    /**
     * Used by downloadGame() method to set prices of a Game object
     * @param buySection it's an Element containing a class called "buySection"
     * @param game the object where the method store parameters
     */
    private void updatePrices(Element buySection, Game game) {

        // if the element hasn't got the class name "buySection"
        if (!buySection.className().equals("buySection")) {
            // search for a tag with this class name
            if (buySection.getElementsByClass("buySection").isEmpty()) {
                throw new GameException();
            }

            // TODO: improve this part of code
            buySection = buySection.getElementsByClass("buySection").get(0);
        }

        for (Element svd : buySection.getElementsByClass("singleVariantDetails")) {

            // TODO: what is this?
            if (svd.getElementsByClass("singleVariantText").isEmpty()) {
                throw new GameException();
            }

            Element svt = svd.getElementsByClass("singleVariantText").get(0);
            String variant = svt.getElementsByClass("variantName").get(0).text();

            switch (variant) {

                // set NewPrice & OlderNewPrice
                case "Nuovo":
                    String price = svt.getElementsByClass("prodPriceCont").get(0).text();
                    game.setNewPrice( stringToPrice(price) );

                    for (Element olderPrice : svt.getElementsByClass("olderPrice")) {
                        price = olderPrice.text();
                        game.addOlderNewPrice( stringToPrice(price) );
                    }
                    break;

                // set UsedPrice & OlderUsedPrice
                case "Usato":
                    price = svt.getElementsByClass("prodPriceCont").get(0).text();
                    game.setUsedPrice( stringToPrice(price) );

                    for (Element olderPrice : svt.getElementsByClass("olderPrice")) {
                        price = olderPrice.text();
                        game.addOlderUsedPrice( stringToPrice(price) );
                    }
                    break;

                // set PreorderPrice & OlderPreorderPrice
                case "Prenotazione":
                    price = svt.getElementsByClass("prodPriceCont").get(0).text();
                    game.setPreorderPrice( stringToPrice(price) );

                    // TODO: this code can be wrong due to the absence of test cases.
                    //       If the app crashes here, the error can be fixed.
                    //       So leave this code UNCOMMENTED.
                    for (Element olderPrice : svt.getElementsByClass("olderPrice")) {
                        price = olderPrice.text();
                        game.addOlderPreorderPrice( stringToPrice(price) );
                    }
                    break;

                // set DigitalPrice & OlderDigitalPrice
                case "Contenuto Digitale":
                    price = svt.getElementsByClass("prodPriceCont").get(0).text();
                    game.setDigitalPrice( stringToPrice(price) );

                    // TODO: this code can be wrong due to the absence of test cases.
                    //       If the app crashes here, the error can be fixed.
                    //       So leave this code UNCOMMENTED.
                    svt.getElementsByClass("pricetext2").remove();
                    svt.getElementsByClass("detailsLink").remove();
                    price = svt.text().replaceAll("[^0-9.,]","");

                    if (!price.isEmpty()) {
                        game.addOlderDigitalPrice( stringToPrice(price) );
                    }
                    break;

                default:
                    break;
            }
        }
    }

    /**
     * Used by downloadGame() method to set pegi of a Game object
     * @param ageBlock it's an Element containing a class called "ageBlock"
     * @param game the object where the method store parameters
     */
    private void updatePegi(Element ageBlock, Game game) {

        // if the element hasn't got the class name "ageBlock"
        if (!ageBlock.className().equals("ageBlock")) {
            // search for a tag with this class name
            if (ageBlock.getElementsByClass("ageBlock").isEmpty()) {
                return;
            }

            // TODO: improve this part of code
            ageBlock = ageBlock.getElementsByClass("ageBlock").get(0);
        }

        for (Element e : ageBlock.getAllElements()) {

            String variant = e.attr("class");

            switch (variant) {
                case "pegi18": game.addPegi("pegi18"); break;
                case "pegi16": game.addPegi("pegi16"); break;
                case "pegi12": game.addPegi("pegi12"); break;
                case "pegi7": game.addPegi("pegi7"); break;
                case "pegi3": game.addPegi("pegi3"); break;
                case "ageDescr BadLanguage": game.addPegi("bad-language"); break;
                case "ageDescr violence": game.addPegi("violence"); break;
                case "ageDescr online": game.addPegi("online"); break;
                case "ageDescr sex": game.addPegi("sex"); break;
                case "ageDescr fear": game.addPegi("fear"); break;
                case "ageDescr drugs": game.addPegi("drugs"); break;
                case "ageDescr discrimination": game.addPegi("discrimination"); break;
                case "ageDescr gambling": game.addPegi("gambling"); break;
                default: break;
            }
        }
    }

    /**
     * Used by downloadGame() method to set the cover of a Game object
     * @param prodImgMax it's an Element containing a class called "prodImgMax"
     * @param game the object where the method store parameters
     */
    private void updateCover(Element prodImgMax, Game game) {

        // if the element hasn't got the class name "prodImg max"
        if (!prodImgMax.className().equals("prodImg max")) {
            // search for a tag with this class name
            if (prodImgMax.getElementsByClass("prodImg max").isEmpty()) {
                return;
            }

            // TODO: improve this part of code
            prodImgMax = prodImgMax.getElementsByClass("prodImg max").get(0);
        }

        String url = prodImgMax.attr("href");
        game.setCover( Uri.parse(url) );
    }

    /**
     * Used by downloadGame() method to set the gallery of a Game object
     * @param mediaImages it's an Element containing a class called "mediaImages"
     * @param game the object where the method store parameters
     */
    private void updateGallery(Element mediaImages, Game game) {

        // if the element hasn't got the class name "mediaImages"
        if (!mediaImages.className().equals("mediaImages")) {
            // search for a tag with this class name
            if (mediaImages.getElementsByClass("mediaImages").isEmpty()) {
                return;
            }

            // TODO: improve this part of code
            mediaImages = mediaImages.getElementsByClass("mediaImages").get(0);
        }

        // init the gallery
        for (Element e : mediaImages.getElementsByTag("a")) {
            String url = e.attr("href");
            game.addToGallery( Uri.parse(url) );
        }
    }

    /**
     * Used by downloadGame() method to set the promotions of a Game object
     * @param bonusBlock it's an Element containing a class called "bonusBlock"
     * @param game the object where the method store parameters
     */
    private void updatePromos(Element bonusBlock, Game game) {

        // TODO: improve this part of code
        // if the element hasn't got the id name "bonusBlock"
        if (!bonusBlock.id().equals("bonusBlock")) {
            // search for a tag with this id name
            bonusBlock = bonusBlock.getElementById("bonusBlock");

            if (bonusBlock == null) {
                return;
            }
        }

        // init the promotions
        for (Element prodSinglePromo : bonusBlock.getElementsByClass("prodSinglePromo")) {

            Elements h4 = prodSinglePromo.getElementsByTag("h4");
            Elements p = prodSinglePromo.getElementsByTag("p");

            Promo promo = new Promo();
            promo.setHeader( h4.text() );
            promo.setValidity( p.get(0).text() );

            // if the promotion has other info
            if (p.size() >= 2) {
                String url = "http://www.gamestop.it" +
                        p.get(1).getElementsByTag("a").attr("href");

                promo.setMessage( p.get(1).text() );
                promo.setMessageURL(url);
            }

            // add the promo
            game.addPromo(promo);
        }
    }

}
