package de.frype.coloring;

import android.content.Context;

import java.io.File;

/**
 * Created by Jan on 07.12.2015.
 */
public final class ColoringUtils {

    private ColoringUtils() {}

    public static void deleteErrorLogFile(Context c) {
        File errorLog = c.getFileStreamPath(c.getString(R.string.error_log_file));
        if (errorLog.exists()) {
            errorLog.delete();
        }
    }
}
