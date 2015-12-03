package de.frype.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

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
}
