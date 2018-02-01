package de.frype.coloring.about;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;

import de.frype.coloring.R;
import de.frype.coloring.settings.SettingsActivity;

/**
 * The about activity. Shows a button which shows preferences and a web view which shows an about web page stored
 * in the assets.
 */
public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // back button action: go back
        ImageButton imageButton = findViewById(R.id.backButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // settings button action: show settings
        imageButton = findViewById(R.id.settingsButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        // show about/help url in web view
        WebView webView = findViewById(R.id.aboutWebView);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.loadUrl("file:///android_asset/about/index.html");
    }
}
