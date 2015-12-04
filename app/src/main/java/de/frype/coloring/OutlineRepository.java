package de.frype.coloring;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import de.frype.util.Utils;

/**
 * Created by Jan on 04.12.2015.
 */
public class OutlineRepository {

    private static OutlineRepository instance = null;

    private JSONArray books;
    private JSONObject currentBook;
    private JSONObject currentPage;

    private OutlineRepository(String json) {
        try {
            books = new JSONArray(json);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setUp(Context context) {
        if (instance != null) {
            throw new RuntimeException("Method can only be called once.");
        }

        // read json
        String json = null;
        try {
            InputStream is = context.getAssets().open(context.getString(R.string.outlines_repository_file));
            json = Utils.readText(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        instance = new OutlineRepository(json);
    }

    public static OutlineRepository getInstance() {
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
}
