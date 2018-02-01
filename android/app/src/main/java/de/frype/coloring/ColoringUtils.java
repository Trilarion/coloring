package de.frype.coloring;

import android.content.Context;
import android.graphics.Color;

import java.io.File;

/**
 * Some utility methods that are specific to the coloring app.
 */
public final class ColoringUtils {

    private final static float DARKEN_LIGHTEN_FACTOR = 0.8f;

    private ColoringUtils() {}

    /**
     * Deletes the error log file.
     * @param context
     */
    public static void deleteErrorLogFile(Context context) {
        File errorLog = context.getFileStreamPath(context.getString(R.string.error_log_file));
        if (errorLog.exists()) {
            //noinspection ResultOfMethodCallIgnored
            errorLog.delete();
        }
    }

    /**
     * Given a color returns two new colors which can be used as start and end colors of a gradient. The two colors are
     * darkened and lightened version of the original color. The first is darkened, the second lightened.
     *
     * @param color A given color.
     * @return An array of two colors.
     */
    public static int[] colorSelectionButtonBackgroundGradient(int color) {
        int[] gradientColors = new int[2];
        float[] hsv = new float[3];

        // darken
        Color.colorToHSV(color, hsv);
        hsv[2] *= DARKEN_LIGHTEN_FACTOR;
        gradientColors[0] = Color.HSVToColor(hsv);

        // lighten
        Color.colorToHSV(color, hsv);
        hsv[2] = 1 - DARKEN_LIGHTEN_FACTOR * (1 - hsv[2]);
        gradientColors[1] = Color.HSVToColor(hsv);

        return gradientColors;
    }
}
