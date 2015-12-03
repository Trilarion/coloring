package de.frype.coloring.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import de.frype.coloring.R;
import de.frype.coloring.adapter.CategorySelectionAdapter;
import de.frype.util.Utils;

public class CategorySelectionMainActivity extends Activity {

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
                Intent intent = new Intent(CategorySelectionMainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        String json = null;
        try {
            InputStream is = getAssets().open("categories.json");
            json = Utils.readText(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        GridView gridView = (GridView) findViewById(R.id.categorySelectionGridView);
        gridView.setAdapter(new CategorySelectionAdapter(this, jsonArray));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject jsonObject = (JSONObject) parent.getItemAtPosition(position);
                Intent intent = new Intent(CategorySelectionMainActivity.this, OutlineSelectionActivity.class);
                intent.putExtra("category", jsonObject.toString());
                startActivity(intent);
            }
        });

        // test exception
        // throw new IllegalArgumentException("test exception");
    }
}
