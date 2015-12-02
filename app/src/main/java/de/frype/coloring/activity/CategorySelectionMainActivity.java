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

import java.io.IOException;
import java.io.InputStream;

import de.frype.coloring.R;
import de.frype.coloring.adapter.CategoryViewAdapter;
import de.frype.util.Utils;

public class CategorySelectionMainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selection_main);

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
            json = Utils.loadJSON(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONArray jsonObject;
        try {
            jsonObject = new JSONArray(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        GridView gridView = (GridView) findViewById(R.id.categorySelectionGridView);
        gridView.setAdapter(new CategoryViewAdapter(this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // parent.getItemAtPosition(position);
            }
        });

        // test exception
        throw new IllegalArgumentException("test exception");
    }
}
