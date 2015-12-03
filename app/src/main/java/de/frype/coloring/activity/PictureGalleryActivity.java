package de.frype.coloring.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import de.frype.coloring.R;

public class PictureGalleryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_gallery);

        ImageButton imageButton = (ImageButton) findViewById(R.id.backButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // if the settings say so, remove the share and delete button from the gallery view
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        if (!sharedPref.getBoolean("setting_sharing_allowed", true)) {
            imageButton = (ImageButton) findViewById(R.id.shareButton);
            ((RelativeLayout) imageButton.getParent()).removeView(imageButton);
        }

        if (!sharedPref.getBoolean("setting_deletion_allowed", true)) {
            imageButton = (ImageButton) findViewById(R.id.deleteButton);
            ((RelativeLayout) imageButton.getParent()).removeView(imageButton);
        }
    }
}
