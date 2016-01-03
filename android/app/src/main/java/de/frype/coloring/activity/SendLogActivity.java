package de.frype.coloring.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
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

public class SendLogActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_log);

        // TODO what if display is rotated...

        // test if error log exists
        File errorLog = getFileStreamPath(getString(R.string.error_log_file));
        if (!errorLog.exists()) {
            finish();
            return;
        }

        // read and delete
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

        EditText log = (EditText) findViewById(R.id.logEditText);
        log.setText(logText);

        Button button = (Button) findViewById(R.id.cancelButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColoringUtils.deleteErrorLogFile(SendLogActivity.this);
                finish();
            }
        });

        button = (Button) findViewById(R.id.sendButton);
        final String finalLogText = logText;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColoringUtils.deleteErrorLogFile(SendLogActivity.this);
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"error_reports@frype.de"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "An Error occurred");
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
