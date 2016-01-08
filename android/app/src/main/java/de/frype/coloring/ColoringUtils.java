package de.frype.coloring;

import android.content.Context;
import android.graphics.Color;

import java.io.File;

/**
 * Created by Jan on 07.12.2015.
 */
public final class ColoringUtils {

    private final static float DARKEN_LIGHTEN_FACTOR = 0.8f;

    private ColoringUtils() {}

    public static void deleteErrorLogFile(Context c) {
        File errorLog = c.getFileStreamPath(c.getString(R.string.error_log_file));
        if (errorLog.exists()) {
            errorLog.delete();
        }
    }

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
