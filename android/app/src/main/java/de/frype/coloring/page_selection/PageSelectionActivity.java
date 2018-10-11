package de.frype.coloring.page_selection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import de.frype.coloring.library.Library;
import de.frype.coloring.R;
import de.frype.coloring.coloring.ColoringActivity;
import de.frype.coloring.picture_gallery.PictureGalleryActivity;

/**
 * Once a coloring book has been chosen, select a page here. Also a way to show the gallery for this book.
 */
public class PageSelectionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_selection);

        // back button action: go back
        ImageButton imageButton = findViewById(R.id.backButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // picture gallery button action: start picture gallery activity
        imageButton = findViewById(R.id.galleryButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PageSelectionActivity.this, PictureGalleryActivity.class);
                startActivity(intent);
            }
        });

        // populate the grid view using a PageSelectionAdapter
        final GridView gridView = findViewById(R.id.pageSelectionGridView);
        gridView.setAdapter(new PageSelectionAdapter(this));

        // on item click listener for grid view, start coloring
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // current page in library
                Library.getInstance().setCurrentPage(position);
                // start coloring activity
                Intent intent = new Intent(PageSelectionActivity.this, ColoringActivity.class);
                startActivity(intent);
            }
        });
    }
}
