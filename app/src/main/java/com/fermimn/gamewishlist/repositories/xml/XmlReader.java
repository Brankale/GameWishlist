package com.fermimn.gamewishlist.repositories.xml;

import android.net.Uri;

import com.fermimn.gamewishlist.models.Game;
import com.fermimn.gamewishlist.models.Promo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlReader {

    @SuppressWarnings("unused")
    private static final String TAG = XmlReader.class.getSimpleName();

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

    /**
     * Get a Game object from an XML file
     * @param xml the file xml
     * @return a Game object obtained by parsing the XML
     * @throws XmlPullParserException if the parser encounters problems while parsing
     * @throws IOException if there are errors while reading the file
     */
    public static Game parse(File xml) throws XmlPullParserException, IOException  {

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();

        InputStream stream = null;

        try {

            stream = new FileInputStream(xml);
            parser.setInput(stream, "UTF-8");
            return getGame(parser);

        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Get the game from an XmlPullParser. The parser must be already initialized with an
     * InputStream of a game xml file. The parser must point at the beginning of the XML.
     * @param parser an XmlPullParser
     * @return a Game object
     * @throws IOException if there are problems while parsing
     * @throws XmlPullParserException if there are problems while parsing
     */
    private static Game getGame(XmlPullParser parser)
            throws XmlPullParserException, IOException  {

        Game game = null;

        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                switch (parser.getName()) {
                    case GAME:          game = new Game( Integer.parseInt(parser.getAttributeValue(0)) );   break;
                    case TITLE:         game.setTitle( parser.nextText() );                 break;
                    case PLATFORM:      game.setPlatform( parser.nextText() );              break;
                    case PUBLISHER:     game.setPublisher( parser.nextText() );             break;
                    case PRICES:        setPrices(parser, game);                            break;
                    case PEGI:          game.setPegi( getPegi(parser) );                    break;
                    case GENRES:        game.setGenres( getGenres(parser) );                break;
                    case OFFICIAL_SITE: game.setOfficialWebSite( parser.nextText() );       break;
                    case PLAYERS:       game.setPlayers( parser.nextText() );               break;
                    case RELEASE_DATE:  game.setReleaseDate( parser.nextText() );           break;
                    case PROMOS:        game.setPromo( getPromos(parser) );                 break;
                    case DESCRIPTION:   game.setDescription( parser.nextText() );           break;
                    case COVER:         game.setCover( parser.nextText() );    break;
                    case GALLERY:       game.setGallery( getGallery(parser) );              break;
                    case VALID_FOR_PROMO:
                        game.setValidForPromo( Boolean.parseBoolean( parser.nextText() ) ); break;
                }
            }

            eventType = parser.next();
        }

        return game;
    }

    /**
     * Init game prices given an XmlPullParser. The parser must be already initialized with an
     * InputStream of a game xml file. The parser must point at the beginning of the tag PRICES.
     * @param parser an XmlPullParser
     * @param game a Game object
     * @throws XmlPullParserException if there are problems while parsing
     * @throws IOException if there are problems while parsing
     */
    private static void setPrices(XmlPullParser parser, Game game)
            throws XmlPullParserException, IOException {

        int eventType = parser.next();
        while ( !(eventType == XmlPullParser.END_TAG && parser.getName().equals(PRICES)) ) {
            if (eventType == XmlPullParser.START_TAG) {
                switch (parser.getName()) {
                    case NEW: game.setNewPrice( Double.parseDouble( parser.nextText() ) ); break;
                    case USED: game.setUsedPrice( Double.parseDouble( parser.nextText() ) ); break;
                    case PREORDER: game.setPreorderPrice( Double.parseDouble( parser.nextText() ) ); break;
                    case DIGITAL: game.setDigitalPrice( Double.parseDouble( parser.nextText() ) ); break;
                    case OLD_NEW: game.setOlderNewPrices( getOlderPrices(parser, OLD_NEW) ); break;
                    case OLD_USED: game.setOlderUsedPrices( getOlderPrices(parser, OLD_USED) ); break;
                    case OLD_PREORDER: game.setOlderPreorderPrices( getOlderPrices(parser, OLD_PREORDER) ); break;
                    case OLD_DIGITAL: game.setOlderDigitalPrices( getOlderPrices(parser, OLD_DIGITAL) ); break;
                }
            }
            eventType = parser.next();
        }
    }

    /**
     * Return game older prices given an XmlPullParser. The parser must be already initialized with
     * an InputStream of a game xml file. The parser must point at the beginning of a tag OLD_*.
     * @param parser an XmlPullParser
     * @param type the older price tag (ex. OLD_NEW, OLD_USED, OLD_PREORDER, OLD_DIGITAL)
     * @return a List<Double> with the older prices
     * @throws XmlPullParserException if there are problems while parsing
     * @throws IOException if there are problems while parsing
     */
    private static List<Double> getOlderPrices(XmlPullParser parser, String type)
            throws XmlPullParserException, IOException  {

        List<Double> olderPrices = new ArrayList<>();

        int eventType = parser.next();
        while ( !(eventType == XmlPullParser.END_TAG && parser.getName().equals(type)) ) {
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals(PRICE)) {
                    olderPrices.add( Double.parseDouble( parser.nextText() ) );
                }
            }
            eventType = parser.next();
        }

        return olderPrices;
    }

    /**
     * Return game pegi types given an XmlPullParser. The parser must be already initialized with
     * an InputStream of a game xml file. The parser must point at the beginning of the tag PEGI.
     * @param parser an XmlPullParser
     * @return a List<String> with pegi types
     * @throws XmlPullParserException if there are problems while parsing
     * @throws IOException if there are problems while parsing
     */
    private static List<String> getPegi(XmlPullParser parser)
            throws XmlPullParserException, IOException {

        List<String> pegi = new ArrayList<>();

        int eventType = parser.next();
        while ( !(eventType == XmlPullParser.END_TAG && parser.getName().equals(PEGI)) ) {
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals(PEGI_TYPE)) {
                    pegi.add( parser.nextText() );
                }
            }
            eventType = parser.next();
        }

        return pegi;
    }

    /**
     * Return game genres given an XmlPullParser. The parser must be already initialized with
     * an InputStream of a game xml file. The parser must point at the beginning of the tag GENRES.
     * @param parser an XmlPullParser
     * @return a List<String> with game genres
     * @throws XmlPullParserException if there are problems while parsing
     * @throws IOException if there are problems while parsing
     */
    private static List<String> getGenres(XmlPullParser parser)
            throws XmlPullParserException, IOException {

        List<String> genres = new ArrayList<>();

        int eventType = parser.next();
        while ( !(eventType == XmlPullParser.END_TAG && parser.getName().equals(GENRES)) ) {
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals(GENRE)) {
                    genres.add( parser.nextText() );
                }
            }
            eventType = parser.next();
        }

        return genres;
    }

    /**
     * Return game pegi types given an XmlPullParser. The parser must be already initialized with
     * an InputStream of a game xml file. The parser must point at the beginning of the tag GALLERY.
     * @param parser an XmlPullParser
     * @return a List<Uri> with game images
     * @throws XmlPullParserException if there are problems while parsing
     * @throws IOException if there are problems while parsing
     */
    private static List<String> getGallery(XmlPullParser parser)
            throws XmlPullParserException, IOException {

        List<String> gallery = new ArrayList<>();

        int eventType = parser.next();
        while ( !(eventType == XmlPullParser.END_TAG && parser.getName().equals(GALLERY)) ) {
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals(IMAGE)) {
                    gallery.add( parser.nextText() );
                }
            }
            eventType = parser.next();
        }

        return gallery;
    }

    /**
     * Return game promos given an XmlPullParser. The parser must be already initialized with
     * an InputStream of a game xml file. The parser must point at the beginning of the tag PROMOS.
     * @param parser an XmlPullParser
     * @return a List<Promo> with game promos
     * @throws XmlPullParserException if there are problems while parsing
     * @throws IOException if there are problems while parsing
     */
    private static List<Promo> getPromos(XmlPullParser parser)
            throws XmlPullParserException, IOException {

        List<Promo> promos = new ArrayList<>();

        int eventType = parser.next();
        while ( !(eventType == XmlPullParser.END_TAG && parser.getName().equals(PROMOS)) ) {
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals(PROMO)) {
                    promos.add( getPromo(parser) );
                }
            }
            eventType = parser.next();
        }

        return promos;
    }

    /**
     * Return a game promo given an XmlPullParser. The parser must be already initialized with
     * an InputStream of a game xml file. The parser must point at the beginning of the tag PROMO.
     * @param parser an XmlPullParser
     * @return a Promo
     * @throws XmlPullParserException if there are problems while parsing
     * @throws IOException if there are problems while parsing
     */
    private static Promo getPromo(XmlPullParser parser)
            throws XmlPullParserException, IOException {

        Promo promo = null;

        int eventType = parser.next();
        while ( !(eventType == XmlPullParser.END_TAG && parser.getName().equals(PROMO)) ) {
            if (eventType == XmlPullParser.START_TAG) {
                switch (parser.getName()) {
                    case HEADER:        promo = new Promo( parser.nextText() );     break;
                    case SUB_HEADER:    promo.setText( parser.nextText() );           break;
                    case FIND_MORE:     promo.setFindMore( parser.nextText() );       break;
                    case FIND_MORE_URL: promo.setFindMoreUrl( parser.nextText() );      break;
                }
            }
            eventType = parser.next();
        }

        return promo;
    }

}
