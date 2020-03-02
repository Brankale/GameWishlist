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
import com.fermimn.gamewishlist.repositories.xml.XmlReader;
import com.fermimn.gamewishlist.repositories.xml.XmlWriter;
import com.fermimn.gamewishlist.utils.Gamestop;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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
                            Game game = XmlReader.parse(folder);
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
    public Game getGame(int gameId) {
        GamePreviewList wishlist = mWishlist.getValue();
        if (wishlist != null) {
            for (GamePreview gamePreview : wishlist) {
                if (gamePreview.getId() == gameId) {
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
            boolean result = initGameFolder( Integer.toString(game.getId()) );
            if (result) {
                // store the game on local memory
                File xml = new File( getGameXml( Integer.toString(game.getId()) ) );

                try {
                    xml.createNewFile();
                    XmlWriter xw = new XmlWriter();
                    xw.saveTo(xml, game);
                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();
                }

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
                removeGame(game.getId());
            }
        }

        return false;
    }

    /**
     * Remove the game given its ID
     * @param gameId the ID of the game to remove
     * @return true if the game has been removed, false otherwise
     */
    public boolean removeGame(int gameId) {
        boolean result = deleteFolder( new File( getGameFolder(Integer.toString(gameId)) ) );

        GamePreviewList wishlist = mWishlist.getValue();
        if (wishlist != null) {
            for (int i = 0; i < wishlist.size(); ++i) {
                if (wishlist.get(i).getId() == gameId) {
                    wishlist.remove(i);
                    mWishlist.postValue(wishlist);
                    break;
                }
            }
        }

        if (result) {
            Log.d(TAG, '[' + getGameFolder(Integer.toString(gameId)) +  "] - folder deleted successfully");
            return true;
        } else {
            Log.e(TAG, '[' + getGameFolder(Integer.toString(gameId)) +  "] - errors occurred while deleting the folder");
            return false;
        }
    }

    public Game updateGame(int gameId) {
        GamePreviewList wishlist = mWishlist.getValue();
        if (wishlist != null) {
            for (int i = 0; i < wishlist.size(); ++i) {
                if (wishlist.get(i).getId() == gameId) {
                    Gamestop gamestop = new Gamestop();
                    Game game = gamestop.downloadGame(gameId);

                    if (game != null) {
                        wishlist.set(i, game);
                        mWishlist.postValue(wishlist);

                        File xml = new File(getGameXml(Integer.toString(game.getId())));

                        try {
                            xml.createNewFile();
                            XmlWriter xw = new XmlWriter();
                            xw.saveTo(xml, game);
                        } catch (IOException | XmlPullParserException e) {
                            e.printStackTrace();
                        }
                    }

                    return game;
                }
            }
        }
        return null;
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
    // TODO: use DownloadManager to handle images download
    //       otherwise images cannot be downloaded if the user close the app
    private void downloadGameImages(final Game game) {

        new Thread() {

            public void run() {
                // download cover
                File cover = new File(getGameFolder(Integer.toString(game.getId())), "cover.jpg");
                downloadImage(cover, game.getCover());

                // download gallery
                if (game.getGallery() != null) {
                    File galleryFolder = new File( getGalleryFolder(Integer.toString(game.getId())) );
                    for (String uri : game.getGallery()) {
                        String url = uri;
                        String name = url.substring( url.lastIndexOf('/') );
                        File galleryImage = new File(galleryFolder, name);
                        downloadImage(galleryImage, uri);
                    }
                }
            }

        }.start();

    }

    // TODO: this method should be rewritten
    private void downloadImage(File img, String uri) {
        try {

            // download bitmap
            InputStream is = (InputStream) new URL(uri).getContent();
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
