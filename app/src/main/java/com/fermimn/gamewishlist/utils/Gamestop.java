package com.fermimn.gamewishlist.utils;

import android.util.Log;
import android.util.Pair;

import com.fermimn.gamewishlist.data_types.Game;
import com.fermimn.gamewishlist.data_types.GamePreview;
import com.fermimn.gamewishlist.data_types.GamePreviews;
import com.fermimn.gamewishlist.fragments.SearchGamesFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public GamePreviews searchGame(String searchedGame) throws IOException {

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

        GamePreviews results = new GamePreviews();

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

            // add the game to the array
            results.add(gamePreview);
        }

        return results;
    }

    @Override
    public GamePreview downloadGame(String id) {
        // TODO: to implement
        Log.w("NOT IMPLEMENTED", "Gamestop.downloadGame : not implemented");
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

        // example "Nuovo 19.99€"
        price = price.replaceAll("[^0-9.,]","");    // remove all the characters except for numbers, ',' and '.'
        price = price.replace(".", "");             // to handle prices over 999,99€ like 1.249,99€
        price = price.replace(',', '.');            // to convert the price in a string that can be parsed

        return Double.parseDouble(price);
    }


}
