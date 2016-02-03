package de.frype.coloring.library;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import de.frype.coloring.R;
import de.frype.util.Utils;

/**
 * Created by Jan on 04.12.2015.
 */
public class Library {

    private static Library instance = null;

    private JSONArray books;
    private JSONObject currentBook;
    private JSONObject currentPage;
    private AssetManager assetManager;

    private int selectedColor = Color.BLUE;

    private Library(String json, AssetManager assetManager) {
        try {
            books = new JSONArray(json);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        this.assetManager = assetManager;
    }

    public static void setUp(Context context) {
        if (instance != null) {
            throw new RuntimeException("Method can only be called once.");
        }

        // read json
        String json = null;
        try {
            InputStream is = context.getAssets().open(context.getString(R.string.library_file));
            json = Utils.readText(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        instance = new Library(json, context.getAssets());
    }

    public static Library getInstance() {
        return instance;
    }

    public int getNumberBooks() {
        return books.length();
    }

    private JSONObject getBook(int position) {
        try {
            return books.getJSONObject(position);
        } catch (JSONException e) {
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
    }

    public int getNumberPagesFromCurrentBook() {
        try {
            JSONArray pages = currentBook.getJSONArray("pages");
            return pages.length();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void setCurrentPage(int position) {
        try {
            JSONArray pages = currentBook.getJSONArray("pages");
            if (position < 0 || position >= pages.length()) {
                throw new RuntimeException("Invalid page position.");
            }
            currentPage = pages.getJSONObject(position);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public String getStringFromCurrentPage(String name) {
        try {
            return currentPage.getString(name);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private String getCurrentPageFilePath() {
        return "library/" + getStringFromCurrentBook("folder") + "/" + getStringFromCurrentPage("file");
    }

    public Bitmap loadCurrentPageBitmap() {
        String pathName = getCurrentPageFilePath();
        InputStream is;
        try {
            is = assetManager.open(pathName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        if (bitmap == null) {
            throw new RuntimeException("Page bitmap could not be loaded!");
        }
        return bitmap;
    }

    public void setSelectedColor(int color) {
        selectedColor = color;
    }

    public int getSelectedColor() {
        return selectedColor;
    }
}
