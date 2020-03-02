package com.fermimn.gamewishlist.repositories.xml;

import com.fermimn.gamewishlist.models.Game;
import com.fermimn.gamewishlist.models.Promo;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

public class XmlWriter {

    @SuppressWarnings("unused")
    private static final String TAG = XmlWriter.class.getSimpleName();

    private static final String GAME = "game";
    private static final String TITLE = "title";
    private static final String PLATFORM = "platform";
    private static final String PUBLISHER = "publisher";
    private static final String PEGI = "pegi";
    private static final String PEGI_TYPE = "type";
    private static final String GENRES = "genres";
    private static final String GENRE = "genre";
    private static final String OFFICIAL_SITE = "officialSite";
    private static final String PLAYERS = "players";
    private static final String RELEASE_DATE = "releaseDate";
    private static final String DESCRIPTION = "description";
    private static final String COVER = "cover";
    private static final String GALLERY = "gallery";
    private static final String IMAGE = "image";
    private static final String VALID_FOR_PROMO = "validForPromo";

    private static final String PRICES = "prices";
    private static final String PRICE = "price";
    private static final String NEW = "newPrice";
    private static final String USED = "usedPrice";
    private static final String PREORDER = "preorderPrice";
    private static final String DIGITAL = "digitalPrice";
    private static final String OLD_NEW = "olderNewPrices";
    private static final String OLD_USED = "olderUsedPrices";
    private static final String OLD_PREORDER = "olderPreorderPrices";
    private static final String OLD_DIGITAL = "olderDigitalPrices";

    private static final String PROMOS = "promos";
    private static final String PROMO = "promo";
    private static final String HEADER = "header";
    private static final String SUB_HEADER = "subHeader";
    private static final String FIND_MORE = "findMoreMsg";
    private static final String FIND_MORE_URL = "findMoreUrl";

    private final XmlSerializer mSerializer;

    public XmlWriter() throws XmlPullParserException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        mSerializer = factory.newSerializer();
    }

    public void saveTo(File xml, Game game) throws IOException {
        FileWriter fw = new FileWriter(xml);
        StringWriter writer = create(game);
        fw.write(writer.toString());
        fw.close();
        writer.close();
    }

    private StringWriter create(Game game) throws IOException {

        StringWriter writer = new StringWriter();
        mSerializer.setOutput(writer);

        mSerializer.startDocument("UTF-8", true);

        mSerializer.startTag(null, GAME);
        mSerializer.attribute(null, "id", Integer.toString(game.getId()));
        addTag(TITLE, game.getTitle());
        addTag(PLATFORM, game.getPlatform());
        addTag(PUBLISHER, game.getPublisher());
        mSerializer.startTag(null, PRICES);
        addPrices(NEW, game.getNewPrice(), OLD_NEW, game.getOldNewPrices());
        addPrices(USED, game.getUsedPrice(), OLD_USED, game.getOldUsedPrices());
        addPrices(PREORDER, game.getPreorderPrice(), OLD_PREORDER, game.getOldPreorderPrices());
        addPrices(DIGITAL, game.getDigitalPrice(), OLD_DIGITAL, game.getOldDigitalPrices());
        mSerializer.endTag(null, PRICES);
        addPegi(game.getPegi());
        addGenres(game.getGenres());
        addTag(OFFICIAL_SITE, game.getWebsite());
        addTag(PLAYERS, game.getPlayers());
        addTag(RELEASE_DATE, game.getReleaseDate());
        addTag(DESCRIPTION, game.getDescription());
        if (game.getCover() != null) {
            addTag(COVER, game.getCover().toString());
        }
        addGallery(game.getGallery());
        addTag(VALID_FOR_PROMO, Boolean.toString(game.getValidForPromo()));
        addPromos(game.getPromos());
        mSerializer.endTag(null, GAME);

        mSerializer.endDocument();

        return writer;
    }

    private void addTag(String name, String value) throws IOException {
        if (value != null) {
            mSerializer.startTag(null, name);
            mSerializer.text(value);
            mSerializer.endTag(null, name);
        }
    }

    private void addPegi(List<String> pegi) throws IOException {
        if (pegi != null) {
            mSerializer.startTag(null, PEGI);
            for (String type : pegi) {
                addTag(PEGI_TYPE, type);
            }
            mSerializer.endTag(null, PEGI);
        }
    }

    private void addGenres(List<String> genres) throws IOException {
        if (genres != null) {
            mSerializer.startTag(null, GENRES);
            for (String genre : genres) {
                addTag(GENRE, genre);
            }
            mSerializer.endTag(null, GENRES);
        }
    }

    private void addGallery(List<String> gallery) throws IOException {
        if (gallery != null) {
            mSerializer.startTag(null, GALLERY);
            for (String image : gallery) {
                addTag(IMAGE, image);
            }
            mSerializer.endTag(null, GALLERY);
        }
    }

    private void addPromos(List<Promo> promos) throws IOException {
        if (promos != null) {
            mSerializer.startTag(null, PROMOS);
            for (Promo promo : promos) {
                addPromo(promo);
            }
            mSerializer.endTag(null, PROMOS);
        }
    }

    private void addPromo(Promo promo) throws IOException {
        mSerializer.startTag(null, PROMO);
        addTag(HEADER, promo.getHeader());
        addTag(SUB_HEADER, promo.getText());
        if (promo.hasFindMore()) {
            addTag(FIND_MORE, promo.getFindMore());
            addTag(FIND_MORE_URL, promo.getFindMoreUrl());
        }
        mSerializer.endTag(null, PROMO);
    }

    private void addPrices(String tag, Float price, String innerTag, List<Float> prices) throws IOException {
        if (price != null) {
            mSerializer.startTag(null, tag);
            mSerializer.text( Double.toString(price) );
            mSerializer.endTag(null, tag);
            addOlderPrices(innerTag, prices);
        }
    }

    private void addOlderPrices(String tag, List<Float> prices) throws IOException {
        if (prices != null) {
            mSerializer.startTag(null, tag);
            for (double price : prices) {
                addTag(PRICE, Double.toString(price));
            }
            mSerializer.endTag(null, tag);
        }
    }

}
