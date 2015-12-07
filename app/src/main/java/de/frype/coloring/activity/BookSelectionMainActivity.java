package de.frype.coloring.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import java.io.File;

import de.frype.coloring.Library;
import de.frype.coloring.R;
import de.frype.coloring.adapter.BookSelectionAdapter;

public class BookSelectionMainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selection_main);

        // test if error log exists
        File errorLog = getFileStreamPath(getString(R.string.error_log_file));
        if (errorLog.exists()) {
            Intent intent = new Intent(this, SendLogActivity.class);
            startActivity(intent);
        }

        ImageButton imageButton = (ImageButton) findViewById(R.id.backButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageButton = (ImageButton) findViewById(R.id.aboutButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookSelectionMainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        GridView gridView = (GridView) findViewById(R.id.categorySelectionGridView);
        gridView.setAdapter(new BookSelectionAdapter(this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BookSelectionMainActivity.this, PageSelectionActivity.class);
                Library.getInstance().setCurrentBook(position);
                startActivity(intent);
            }
        });

        // test exception
        // throw new IllegalArgumentException("test exception");
    }
}
