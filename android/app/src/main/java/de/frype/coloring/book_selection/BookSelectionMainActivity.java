package de.frype.coloring.book_selection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import java.io.File;

import de.frype.coloring.library.Library;
import de.frype.coloring.R;
import de.frype.coloring.about.AboutActivity;
import de.frype.coloring.page_selection.PageSelectionActivity;
import de.frype.coloring.error_log.SendLogActivity;

/**
 * Launcher activity of the app. Select a book for coloring in a grid view.
 */
public class BookSelectionMainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_selection_main);

        // test first if error log file exists, if so, start send log activity
        File errorLog = getFileStreamPath(getString(R.string.error_log_file));
        if (errorLog.exists()) {
            Intent intent = new Intent(this, SendLogActivity.class);
            startActivity(intent);
            // in any case we want to finish this activity and not customize it any further
            finish();
            return;
        }

        // exit button action: going back which basically means finish
        ImageButton imageButton = findViewById(R.id.exitButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // about button action: show about activity
        imageButton = findViewById(R.id.aboutButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookSelectionMainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        // populate grid view using a BookSelectionAdapter
        GridView gridView = findViewById(R.id.bookSelectionGridView);
        gridView.setAdapter(new BookSelectionAdapter(this));

        // on item click listener for grid view, start page selection
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // set current book in library
                Library.getInstance().setCurrentBook(position);
                // start new activity
                Intent intent = new Intent(BookSelectionMainActivity.this, PageSelectionActivity.class);
                startActivity(intent);
            }
        });

        // debugging: for test exception handling, throw a test exception here
        // throw new IllegalArgumentException("test exception");
    }
}
