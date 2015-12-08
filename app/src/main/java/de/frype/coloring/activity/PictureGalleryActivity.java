package de.frype.coloring.activity;

import android.app.Activity;
import android.content.Intent;
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

        imageButton = (ImageButton) findViewById(R.id.shareButton);
        if (!sharedPref.getBoolean("setting_sharing_allowed", true)) {
            ((RelativeLayout) imageButton.getParent()).removeView(imageButton);
        } else {
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("image/png");
                    // sharingIntent.putExtra(Intent.EXTRA_STREAM, _newImageUri);
                    // startActivity(Intent.createChooser(sharingIntent, getString( R.string.dialog_share )));
                }
            });
        }

        imageButton = (ImageButton) findViewById(R.id.deleteButton);
        if (!sharedPref.getBoolean("setting_deletion_allowed", true)) {
            ((RelativeLayout) imageButton.getParent()).removeView(imageButton);
        } else {
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
