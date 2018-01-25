package de.frype.util;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Various general methods.
 */
public final class Utils {

    private Utils() {}

    public static String readText(InputStream is) throws IOException {
        return readText(is, "UTF8");
    }

    public static String readText(InputStream is, String charSetName) throws IOException {

        InputStreamReader isr = new InputStreamReader(is, charSetName);
        BufferedReader reader = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        reader.close();

        // TODO or StringBuffer (http://docs.oracle.com/javase/tutorial/i18n/text/stream.html)

        return sb.toString();
    }

    public static void writeText(OutputStream os, String content) throws IOException {
        writeText(os, content, "UTF8");
    }

    public static void writeText(OutputStream os, String content, String charSetName) throws IOException {

        // final OutputStream out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        OutputStreamWriter writer = new OutputStreamWriter(os, charSetName);
        writer.write(content);
        writer.flush();
        os.close();
    }

    /**
     * Removes an OnGlobalLayoutListener from a view. Also works for SDK version below 16.
     *
     * @param view the view
     * @param listener the listener
     */
    public static void removeOnGlobalLayoutListener(View view, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (Build.VERSION.SDK_INT < 16) {
            //noinspection deprecation
            view.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        } else {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }

    /**
     * Returns a drawable from Resources. Hides that the implementation is different below SDK version 22.
     *
     * @param resources the resources
     * @param id the drawable id
     * @return
     */
    public static Drawable getDrawable(Resources resources, int id) {
        Drawable drawable;
        if (Build.VERSION.SDK_INT < 22) {
            //noinspection deprecation
            drawable = resources.getDrawable(id);
        } else {
            drawable = resources.getDrawable(id, null);
        }
        return drawable;
    }

    public static void setBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT < 16) {
            //noinspection deprecation
            view.setBackgroundDrawable(drawable);
        } else {
            view.setBackground(drawable);
        }
    }


    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference an object reference
     * @param <T>
     * @return the non-null reference that was checked
     */
    public static <T> T verifyNotNull(T reference) throws NullPointerException {
        if (null == reference) {
            throw new NullPointerException();
        }
        return reference;
    }
}
