package de.frype.util;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.GridView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import de.frype.coloring.R;

/**
 * Created by Jan on 29.11.2015.
 */
public final class Utils {

    private Utils() {
    }

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

    public static void removeOnGlobalLayoutListener(View view, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (Build.VERSION.SDK_INT < 16) {
            view.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        } else {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }

    public static Drawable getDrawable(Resources resources, int id) {
        Drawable drawable;
        if (Build.VERSION.SDK_INT < 22) {
            drawable = resources.getDrawable(id);
        } else {
            drawable = resources.getDrawable(id, null);
        }
        return drawable;
    }

    public static void setBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT < 16) {
            view.setBackgroundDrawable(drawable);
        } else {
            view.setBackground(drawable);
        }
    }
}
