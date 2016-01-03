package de.frype.coloring.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import de.frype.coloring.Library;
import de.frype.coloring.R;
import de.frype.coloring.adapter.PageSelectionAdapter;

public class PageSelectionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outline_selection);

        ImageButton imageButton = (ImageButton) findViewById(R.id.backButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageButton = (ImageButton) findViewById(R.id.galleryButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PageSelectionActivity.this, PictureGalleryActivity.class);
                startActivity(intent);
            }
        });

        GridView gridView = (GridView) findViewById(R.id.outlineSelectionGridView);
        gridView.setAdapter(new PageSelectionAdapter(this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PageSelectionActivity.this, ColoringActivity.class);
                Library.getInstance().setCurrentPage(position);
                startActivity(intent);
            }
        });
    }
}
