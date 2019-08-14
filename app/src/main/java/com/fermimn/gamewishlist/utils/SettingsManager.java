package com.fermimn.gamewishlist.utils;

import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class SettingsManager {

    private static SettingsManager mInstance;
    private final Context mContext;

    private int mDarkMode = AppCompatDelegate.MODE_NIGHT_NO;

    public static SettingsManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SettingsManager(context);
        }
        return mInstance;
    }

    private SettingsManager(Context context) {
        mContext = context.getApplicationContext();
        init();
    }

    public void setDarkMode(int mode) {
        mDarkMode = mode;
    }

    public int getDarkMode() {
        return mDarkMode;
    }

    public boolean commit() {

        try {

            File settings = new File(mContext.getFilesDir(), "settings.xml");

            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

            Element root = doc.createElement("settings");

            Element darkMode = doc.createElement("dark_mode");
            darkMode.setTextContent( Integer.toString(mDarkMode) );

            root.appendChild(darkMode);
            doc.appendChild(root);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(new DOMSource(doc), new StreamResult(settings));

            return true;

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void init() {

        try {

            File settings = new File(mContext.getFilesDir(), "settings.xml");

            if (!settings.exists()) {
                return;
            }

            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(settings);

            NodeList tags = doc.getElementsByTagName("dark_mode");
            if (tags.getLength() > 0) {
                String darkMode = tags.item(0).getTextContent();
                mDarkMode = Integer.parseInt(darkMode);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

}
