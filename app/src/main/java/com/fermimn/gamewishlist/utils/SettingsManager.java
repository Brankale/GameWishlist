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
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class SettingsManager {

    private static SettingsManager mInstance;
    private final Context mContext;
    private boolean mChanges;

    private int mDarkMode = AppCompatDelegate.MODE_NIGHT_NO;
    private boolean mHighQualityPreview;

    public static SettingsManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SettingsManager(context);
        }
        return mInstance;
    }

    private SettingsManager(Context context) {
        mContext = context.getApplicationContext();
        mChanges = false;
        init();
    }

    public void setDarkMode(int mode) {
        mDarkMode = mode;
        mChanges = true;
    }

    public int getDarkMode() {
        return mDarkMode;
    }

    public void setHighQualityPreview(boolean state) {
        mHighQualityPreview = state;
        mChanges = true;
    }

    public boolean isHighQualityPreview() {
        return mHighQualityPreview;
    }

    public boolean commit() {

        try {

            if (mChanges) {
                File settings = new File(mContext.getFilesDir(), "settings.xml");

                Document doc = DocumentBuilderFactory
                        .newInstance()
                        .newDocumentBuilder()
                        .newDocument();

                Element root = doc.createElement("settings");

                Element darkMode = doc.createElement("dark_mode");
                darkMode.setTextContent(Integer.toString(mDarkMode));
                root.appendChild(darkMode);

                Element HQPreview = doc.createElement("high_quality_preview");
                HQPreview.setTextContent(""+mHighQualityPreview);
                root.appendChild(HQPreview);

                doc.appendChild(root);

                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                transformer.transform(new DOMSource(doc), new StreamResult(settings));

                mChanges = false;
            }

            return true;

        } catch (ParserConfigurationException | TransformerException e) {
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

            NodeList HQPreview = doc.getElementsByTagName("high_quality_preview");
            if (HQPreview.getLength() > 0) {
                mHighQualityPreview = Boolean.parseBoolean( HQPreview.item(0).getTextContent() );
            }

        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

}
