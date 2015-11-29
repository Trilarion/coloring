package de.frype.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

/**
 * Created by Jan on 29.11.2015.
 */
public final class Utils {

    private Utils() {
    }

    public static String loadJSON(String path) throws IOException {
        return loadJSON(path, "UTF8");
    }

    public static String loadJSON(String path, String charSetName) throws IOException {
        // TODO try with resources and still just throw the exception

        InputStream is = new FileInputStream(path);
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
}
