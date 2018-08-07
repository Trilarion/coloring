package de.frype.coloring.error_log;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import de.frype.coloring.ColoringUtils;
import de.frype.coloring.R;
import de.frype.util.Utils;

/**
 * Simple page that displays the content of the error log and prompts the user to push a button and start an
 * email client, so the error log can be sent to the developer.
 */
public class SendLogActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_log);

        // if error log file does not exist, return immediately
        File errorLog = getFileStreamPath(getString(R.string.error_log_file));
        if (!errorLog.exists()) {
            finish();
            return;
        }

        // read error log file
        InputStream is = null;
        try {
            is = openFileInput(getString(R.string.error_log_file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String logText = null;
        try {
            logText = Utils.readText(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        EditText log = findViewById(R.id.logEditText);
        log.setText(logText);

        Button button = findViewById(R.id.cancelButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // user clicked cancel, delete log and return
                ColoringUtils.deleteErrorLogFile(SendLogActivity.this);
                finish();
            }
        });

        button = findViewById(R.id.sendButton);
        final String finalLogText = logText;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // user clicked submit, delete log and start intent
                ColoringUtils.deleteErrorLogFile(SendLogActivity.this);

                // send to intent
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.fromParts("mailto","abc@gmail.com", null));
                // intent.setType("plain/text");
                // intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"error_reports@frype.de"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Coloring app: An Error occurred");
                intent.putExtra(Intent.EXTRA_TEXT, finalLogText);
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast toast = Toast.makeText(SendLogActivity.this, "No service found to send emails.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
}
