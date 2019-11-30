package com.fermimn.gamewishlist.utils;

import android.net.Uri;
import android.util.Log;
import android.util.Pair;

import com.fermimn.gamewishlist.models.Game;
import com.fermimn.gamewishlist.exceptions.GameException;
import com.fermimn.gamewishlist.models.GamePreview;
import com.fermimn.gamewishlist.models.GamePreviewList;
import com.fermimn.gamewishlist.models.Promo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Gamestop implements Store {

    @SuppressWarnings("unused")
    private static final String TAG = Gamestop.class.getSimpleName();

    private static final String WEBSITE_URL = "https://www.gamestop.it";
    private static final String SEARCH_URL = WEBSITE_URL + "/SearchResult/QuickSearch?q=";
    private static final String GAME_URL = WEBSITE_URL + "/Platform/Games/";

    public static String getLink(String gameId) {
        return GAME_URL + gameId;
    }

    /**
     * Search games on Gamestop website
     * @param searchedGame a String with the name of a game
     * @return the list of the games found, null if there have been problems or nothing was found
     */
    @Override
    public GamePreviewList searchGame(String searchedGame) {

        try {
            String url = SEARCH_URL + URLEncoder.encode(searchedGame, "UTF-8");

            // get the HTML
            Log.d(TAG, "Downloading GamePreviews..." + url);
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

                // get & set main info
                String id = game.getElementsByClass("prodImg").get(0).attr("href").split("/")[3];
                String title = game.getElementsByTag("h3").get(0).text();
                String publisher = game.getElementsByTag("h4").get(0).getElementsByTag("strong").text();
                String platform = game.getElementsByTag("h4").get(0).textNodes().get(0).text().trim();

                GamePreview gamePreview = new GamePreview(id, title, platform);
                gamePreview.setPublisher(publisher);

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
                gamePreview.setCover(Uri.parse(imageUrl));

                // add the game to the array
                results.add(gamePreview);
            }

            return results;

        } catch (Exception e) {
            // it's not a good practise catching Exception but it's necessary because the HTML
            // can change and the app mustn't crash
            // TODO: show an error message
            e.printStackTrace();
        }


        return null;
    }

    /**
     * Download a Game from Gamestop given the id
     * @param id of the game
     * @return a Game object if found, otherwise null
     */
    @Override
    public Game downloadGame(String id) {

        try {

            // Get the URL and establish the connection
            String url = getLink(id);

            // Get the HTML
            Log.d(TAG, "[" + id + "] - Downloading Game... [" + url + "]");
            Document html = Jsoup.connect(url).get();

            // Init the Game
            // TODO: use more significant names for methods
            Log.d(TAG, "[" + id + "] - Fetching main info...");
            Game game = updateMainInfo(html.body(), id);

            if (game == null) {
                return null;
            }

            Log.d(TAG, "[" + id + "] - Fetching metadata...");
            updateMetadata(html.body(), game);
            Log.d(TAG, "[" + id + "] - Fetching prices...");
            updatePrices(html.body(), game);
            Log.d(TAG, "[" + id + "] - Fetching pegi...");
            updatePegi(html.body(), game);
            Log.d(TAG, "[" + id + "] - Fetching cover...");
            updateCover(html.body(), game);
            Log.d(TAG, "[" + id + "] - Fetching gallery...");
            updateGallery(html.body(), game);
            Log.d(TAG, "[" + id + "] - Fetching promos...");
            updatePromos(html.body(), game);
            Log.d(TAG, "[" + id + "] - Fetching description...");
            updateDescription(html.body(), game);

            return game;

        } catch (Exception e) {
            // it's not a good practise catching Exception but it's necessary because the HTML
            // can change and the app mustn't crash
            // TODO: return a controlled exception and show an alert dialog message
            e.printStackTrace();
        }

        return null;
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
     * Used by downloadGame() method to get a Game object
     * @param prodTitle it's an Element containing a class called "prodTitle"
     * @param id of the game
     * @return a Game object, null if it hasn't been possible create the game
     */
    private Game updateMainInfo(Element prodTitle, String id) {

        // Check if there's a tag with a specific class inside the Element
        prodTitle = getElementByClass(prodTitle, "prodTitle");
        if (prodTitle == null) {
            return null;
        }

        // init main info
        String title = prodTitle.getElementsByTag("h1").text();
        String platform = prodTitle.getElementsByTag("p").get(0)
                .getElementsByTag("span").text();

        Game game = new Game(id, title, platform);
        game.setPublisher( prodTitle.getElementsByTag("strong").text() );
        return game;
    }

    /**
     * Used by downloadGame() method to set: genre, official site,
     * players and release date of a Game object
     * @param addedDetInfo it's an Element containing a class called "addedDetInfo"
     * @param game the object where the method store parameters
     */
    private void updateMetadata(Element addedDetInfo, Game game) {

        // Check if there's a tag with a specific class inside the Element
        addedDetInfo = getElementByClass(addedDetInfo, "addedDetInfo");
        if (addedDetInfo == null) {
            throw new GameException();
        }

        // init metadata
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
                        game.setOfficialWebSite( e.child(1).getElementsByTag("a")
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
        Elements validForPromoClass = addedDetInfo.getElementsByClass("ProdottoNonValido");
        if (!validForPromoClass.isEmpty()) {
            if (validForPromoClass.get(0).text().equals("Prodotto VALIDO per le promozioni")) {
                game.setValidForPromo(true);
            }
        }
    }

    /**
     * Used by downloadGame() method to set prices of a Game object
     * @param buySection it's an Element containing a class called "buySection"
     * @param game the object where the method store parameters
     */
    private void updatePrices(Element buySection, Game game) {

        // Check if there's a tag with a specific class inside the Element
        buySection = getElementByClass(buySection, "buySection");
        if (buySection == null) {
            throw new GameException();
        }

        // init prices
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
                    //       If the app crashes here, the error can be fixed using the crash log.
                    //       So LEAVE this code UNCOMMENTED.
                    for (Element olderPrice : svt.getElementsByClass("olderPrice")) {
                        price = olderPrice.text();
                        game.addOlderPreorderPrice( stringToPrice(price) );
                    }
                    break;

                // set DigitalPrice & OlderDigitalPrice
                case "Digitale":
                    price = svt.getElementsByClass("prodPriceCont").get(0).text();
                    game.setDigitalPrice( stringToPrice(price) );

                    // TODO: this code can be wrong due to the absence of test cases.
                    //       If the app crashes here, the error can be fixed using the crash log.
                    //       So LEAVE this code UNCOMMENTED.
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

        // Check if there's a tag with a specific class inside the Element
        ageBlock = getElementByClass(ageBlock, "ageBlock");
        if (ageBlock == null) {
            return;
        }

        // init PEGI
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

        // Check if there's a tag with a specific class inside the Element
        prodImgMax = getElementByClass(prodImgMax, "prodImg max");
        if (prodImgMax == null) {
            return;
        }

        // init cover
        String url = prodImgMax.attr("href");
        game.setCover( Uri.parse(url) );
    }

    /**
     * Used by downloadGame() method to set the gallery of a Game object
     * @param mediaImages it's an Element containing a class called "mediaImages"
     * @param game the object where the method store parameters
     */
    private void updateGallery(Element mediaImages, Game game) {

        // Check if there's a tag with a specific class inside the Element
        mediaImages = getElementByClass(mediaImages, "mediaImages");
        if (mediaImages == null) {
            return;
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

        // Check if there's a tag with a specific id inside the Element
        bonusBlock = getElementById(bonusBlock, "bonusBlock");
        if (bonusBlock == null) {
            return;
        }

        // init the promotions
        for (Element prodSinglePromo : bonusBlock.getElementsByClass("prodSinglePromo")) {

            Elements h4 = prodSinglePromo.getElementsByTag("h4");
            Elements p = prodSinglePromo.getElementsByTag("p");

            Promo promo = new Promo(h4.text());
            promo.setSubHeader( p.get(0).text() );

            // if the promotion has other info
            if (p.size() >= 2) {
                String url = "http://www.gamestop.it" +
                        p.get(1).getElementsByTag("a").attr("href");

                promo.setFindMoreMsg(p.get(1).text(), url);
            }

            // add the promo
            game.addPromo(promo);
        }
    }

    /**
     * Used by downloadGame() method to set the description of a Game object
     * @param prodDesc it's an Element containing a class called "prodDesc"
     * @param game the object where the method store parameters
     */
    private void updateDescription(Element prodDesc, Game game) {

        // Check if there's a tag with a specific id inside the Element
        prodDesc = getElementById(prodDesc, "prodDesc");
        if (prodDesc == null) {
            return;
        }

        // init description
        prodDesc.getElementsByClass("prodToTop").remove();
        prodDesc.getElementsByClass("prodSecHead").remove();
        game.setDescription( prodDesc.outerHtml() );
    }

    /**
     * Used by update methods to work on the right Element object
     * @param e Element object
     * @param className the class of a tag inside the HTML
     * @return the first tag found inside the Element which has the given className,
     *         null if nothing was found
     */
    private Element getElementByClass(Element e, String className) {

        if (e.className().equals(className)) {
            return e;
        }

        Elements elements = e.getElementsByClass(className);
        return elements.isEmpty() ? null : elements.get(0);
    }

    /**
     * Used by update methods to work on the right Element object
     * @param e Element object
     * @param idName the id of a tag inside the HTML
     * @return the first tag found inside the Element which has the given id name,
     *         null if nothing was found
     */
    private Element getElementById(Element e, String idName) {
        return e.className().equals(idName) ? e : e.getElementById(idName);
    }

}
