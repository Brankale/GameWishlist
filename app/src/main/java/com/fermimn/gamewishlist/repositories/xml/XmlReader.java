package com.fermimn.gamewishlist.repositories.xml;

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

public class XmlReader implements Xml {

    @SuppressWarnings("unused")
    private static final String TAG = XmlReader.class.getSimpleName();

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
                    case OFFICIAL_SITE: game.setWebsite( parser.nextText() );               break;
                    case PLAYERS:       game.setPlayers( parser.nextText() );               break;
                    case RELEASE_DATE:  game.setReleaseDate( parser.nextText() );           break;
                    case PROMOS:        game.setPromos( getPromos(parser) );                break;
                    case DESCRIPTION:   game.setDescription( parser.nextText() );           break;
                    case COVER:         game.setCover( parser.nextText() );                 break;
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
                    case NEW:
                        game.setNewAvailable(Boolean.parseBoolean(parser.getAttributeValue(null, NEW_AVAILABLE)));
                        game.setNewPrice( Float.parseFloat( parser.nextText() ) );
                        break;
                    case USED:
                        game.setUsedAvailable(Boolean.parseBoolean(parser.getAttributeValue(null, USED_AVAILABLE)));
                        game.setUsedPrice( Float.parseFloat( parser.nextText() ) );
                        break;
                    case PREORDER:
                        game.setPreorderAvailable(Boolean.parseBoolean(parser.getAttributeValue(null, PREORDER_AVAILABLE)));
                        game.setPreorderPrice( Float.parseFloat( parser.nextText() ) );
                        break;
                    case DIGITAL:
                        game.setDigitalAvailable(Boolean.parseBoolean(parser.getAttributeValue(null, DIGITAL_AVAILABLE)));
                        game.setDigitalPrice( Float.parseFloat( parser.nextText() ) );
                        break;
                    case OLD_NEW: game.addOldNewPrices( getOlderPrices(parser, OLD_NEW) ); break;
                    case OLD_USED: game.addOldUsedPrices( getOlderPrices(parser, OLD_USED) ); break;
                    case OLD_PREORDER: game.addOldPreorderPrices( getOlderPrices(parser, OLD_PREORDER) ); break;
                    case OLD_DIGITAL: game.addOldDigitalPrices( getOlderPrices(parser, OLD_DIGITAL) ); break;
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
    private static ArrayList<Float> getOlderPrices(XmlPullParser parser, String type)
            throws XmlPullParserException, IOException  {

        ArrayList<Float> olderPrices = new ArrayList<>();

        int eventType = parser.next();
        while ( !(eventType == XmlPullParser.END_TAG && parser.getName().equals(type)) ) {
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals(PRICE)) {
                    olderPrices.add( Float.parseFloat( parser.nextText() ) );
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
    private static ArrayList<String> getPegi(XmlPullParser parser)
            throws XmlPullParserException, IOException {

        ArrayList<String> pegi = new ArrayList<>();

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
    private static ArrayList<String> getGenres(XmlPullParser parser)
            throws XmlPullParserException, IOException {

        ArrayList<String> genres = new ArrayList<>();

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
    private static ArrayList<String> getGallery(XmlPullParser parser)
            throws XmlPullParserException, IOException {

        ArrayList<String> gallery = new ArrayList<>();

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
    private static ArrayList<Promo> getPromos(XmlPullParser parser)
            throws XmlPullParserException, IOException {

        ArrayList<Promo> promos = new ArrayList<>();

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
