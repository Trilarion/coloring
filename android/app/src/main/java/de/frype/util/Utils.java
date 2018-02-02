package de.frype.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
     * Converts one color in a bitmap to another.
     *
     * @param sourceBitmap The bitmap.
     * @param sourceColor Color to be replaced.
     * @param targetColor Replacement color.
     * @return A copy of the bitmap with the color replaced.
     */
    public static Bitmap replaceColorInBitmap(Bitmap sourceBitmap, int sourceColor, int targetColor) {

        // get pixels of input bitmap
        int width =  sourceBitmap.getWidth();
        int height = sourceBitmap.getHeight();
        int number_pixels = width * height;
        int[] pixels = new int[number_pixels];
        sourceBitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        // go through all pixel and perform the replacement
        for (int i = 0; i < number_pixels; i++) {
            if (pixels[i] == sourceColor) {
                pixels[i] = targetColor;
            }
        }

        // set the pixels in the output bitmap
        Bitmap targetBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        targetBitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        return targetBitmap;
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

    /**
     * Given available dimensions (of an image) and required dimensions, calculates a power of 2 so that the division
     * of the available dimensions by that value is still larger than the required dimension. Useful for fast
     * loading of downscaled images where the original image size is much larger than needed.
     *
     * See also https://developer.android.com/topic/performance/graphics/load-bitmap.html#load-bitmap
     *
     * @param requiredWidth
     * @param requiredHeight
     * @return
     */
    public static int calculateInSampleSize(int availableWidth, int availableHeight, int requiredWidth, int requiredHeight) {
        int inSampleSize = 1;

        if (availableHeight > requiredHeight || availableWidth > requiredWidth) {

            final int halfHeight = availableHeight / 2;
            final int halfWidth = availableWidth / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= requiredHeight && (halfWidth / inSampleSize) >= requiredWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * Loads a Bitmap from a stream, given an InputStreamProvider.
     *
     * See also https://developer.android.com/topic/performance/graphics/load-bitmap.html#load-bitmap
     *
     * @param requiredWidth
     * @param requiredHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromStream(InputStreamProvider inputStreamProvider, int requiredWidth, int requiredHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStreamProvider.getStream(), null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options.outWidth, options.outHeight, requiredWidth, requiredHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(inputStreamProvider.getStream(), null, options);
    }
}
