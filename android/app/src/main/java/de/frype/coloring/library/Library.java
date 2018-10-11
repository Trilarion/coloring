package de.frype.coloring.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import de.frype.util.InputStreamProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import de.frype.coloring.R;
import de.frype.util.Utils;

/**
 * This holds important information in a singleton which is persistent.
 */
public class Library {

    private static Library instance = null;

    private final Context context;
    private final String libraryFileRootFolder;
    private final JSONArray books;
    private JSONObject currentBook;
    private JSONObject currentPage;

    private int selectedColor = Color.BLUE; // of the color picker on the coloring activity

    private Library(Context context) {
        this.context = context;
        libraryFileRootFolder = context.getString(R.string.library_root_folder) + File.separator;

        String libraryFilePath = libraryFileRootFolder + context.getString(R.string.library_file);
        // read json
        String json;
        try {
            InputStream is = context.getAssets().open(libraryFilePath);
            json = Utils.readText(is);
        } catch (IOException e) {
            throw new RuntimeException(e);

        }

        try {
            books = new JSONArray(json);
        } catch (JSONException e) {
            throw new RuntimeException("library json content problem: top level is not a json array", e);
        }
    }

    /**
     * @return The singleton instance of the Library.
     */
    public static Library getInstance() {
        return Utils.verifyNotNull(instance);
    }

    /**
     * Reads the library information about coloring books and pages from a json file.
     *
     * @param context App context.
     */
    public static void initialize(Context context) {
        if (instance != null) {
            throw new RuntimeException("Library initialize can only be called once.");
        }

        instance = new Library(context);
    }

    /**
     *
     * @return The number of books in the library.
     */
    public int getNumberBooks() {
        return books.length();
    }

    private JSONObject getBook(int position) {
        try {
            return books.getJSONObject(position);
        } catch (JSONException e) {
            throw new RuntimeException("library json content problem: retrieving book", e);
        }
    }

    public void setCurrentBook(int position) {
        if (position < 0 || position >= books.length()) {
            throw new RuntimeException("Invalid book position.");
        }
        currentBook = getBook(position);
    }

    public String getStringFromCurrentBook(String name) {
        try {
            return currentBook.getString(name);
        } catch (JSONException e) {
            throw new RuntimeException("library json content problem: retrieving named value from book", e);
        }
    }

    public int getNumberPagesFromCurrentBook() {
        try {
            JSONArray pages = currentBook.getJSONArray("pages");
            return pages.length();
        } catch (JSONException e) {
            throw new RuntimeException("library json content problem: retrieving number of pages entries in book", e);
        }
    }

    private String getCurrentBookCoverFilePath() {
        return libraryFileRootFolder + getStringFromCurrentBook("folder") + File.separator + getStringFromCurrentBook("cover");
    }

    public Bitmap loadCurrentBookCoverBitmapDownscaled(int requiredWidth, int requiredHeight) {
        String pathName = getCurrentBookCoverFilePath();
        InputStreamProvider inputStreamProvider = getInputStreamProviderForAssetPath(pathName);
        Bitmap bitmap = Utils.decodeSampledBitmapFromStream(inputStreamProvider, requiredWidth, requiredHeight);
        if (bitmap == null) {
            throw new RuntimeException("Bitmap could not be loaded!");
        }
        return bitmap;
    }



    public void setCurrentPage(int position) {
        try {
            JSONArray pages = currentBook.getJSONArray("pages");
            if (position < 0 || position >= pages.length()) {
                throw new RuntimeException("Invalid page position.");
            }
            currentPage = pages.getJSONObject(position);
        } catch (JSONException e) {
            throw new RuntimeException("library json content problem: getting a certain page from a book", e);
        }
    }

    public String getStringFromCurrentPage(String name) {
        try {
            return currentPage.getString(name);
        } catch (JSONException e) {
            throw new RuntimeException("library json content problem: retrieving named value from page", e);
        }
    }

    private String getCurrentPageFilePath() {
        return libraryFileRootFolder + getStringFromCurrentBook("folder") + File.separator + getStringFromCurrentPage("file");
    }

    public Bitmap loadCurrentPageBitmap() {
        String pathName = getCurrentPageFilePath();
        Bitmap bitmap = BitmapFactory.decodeStream(getInputStreamProviderForAssetPath(pathName).getStream());
        if (bitmap == null) {
            throw new RuntimeException("Page bitmap could not be loaded!");
        }
        return bitmap;
    }

    public Bitmap loadCurrentPageBitmapDownscaled(int requiredWidth, int requiredHeight) {
        String pathName = getCurrentPageFilePath();
        InputStreamProvider inputStreamProvider = getInputStreamProviderForAssetPath(pathName);
        Bitmap bitmap = Utils.decodeSampledBitmapFromStream(inputStreamProvider, requiredWidth, requiredHeight);
        if (bitmap == null) {
            throw new RuntimeException("Bitmap could not be loaded!");
        }
        return bitmap;
    }

    private InputStreamProvider getInputStreamProviderForAssetPath(final String pathName) {
        return new InputStreamProvider() {

            @Override
            public InputStream getStream() {
                InputStream is;
                try {
                    is = context.getAssets().open(pathName);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return is;
            }
        };
    }

    public void setSelectedColor(int color) {
        selectedColor = color;
    }

    public int getSelectedColor() {
        return selectedColor;
    }
}
