package de.frype.coloring;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.io.OutputStream;

import de.frype.util.Utils;

/**
 * Created by Jan on 30.11.2015.
 */
public class ColoringApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        final Thread.UncaughtExceptionHandler oldDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                StringBuilder sb = new StringBuilder();

                // header
                sb.append("An unexpected exception occurred!");

                // app version
                String version;
                try {
                    version = ColoringApplication.this.getPackageManager().getPackageInfo("de.frype.coloring", 0).versionName;
                } catch (PackageManager.NameNotFoundException e) {
                    version = "undefined";
                }
                sb.append(String.format("App version: %s%n", version));

                // device model
                String model = Build.MODEL;
                if (!model.startsWith(Build.MANUFACTURER)) {
                    model = Build.MANUFACTURER + " " + model;
                }
                sb.append(String.format("Device model: %s%n", model));

                // SDK
                sb.append(String.format("Android SDK level: %d%n", Build.VERSION.SDK_INT));

                // thread name
                sb.append(String.format("Thread: %s%n", thread.getName()));

                // exception name
                sb.append(ex.toString());

                // exception trace
                sb.append(Log.getStackTraceString(ex));

                Context c = ColoringApplication.this.getApplicationContext();
                try {
                    OutputStream os = c.openFileOutput("error_report.txt", Context.MODE_PRIVATE);
                    Utils.writeString(os, sb.toString());
                } catch (Exception e) {
                    // we could not write the error
                }

                // continue with old handling
                if (oldDefaultUncaughtExceptionHandler != null) {
                    oldDefaultUncaughtExceptionHandler.uncaughtException(thread, ex);
                } else {
                    System.exit(-1);
                }
            }
        });

    }
}
