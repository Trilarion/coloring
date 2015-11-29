package de.frype.coloring.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.frype.coloring.R;
import de.frype.coloring.ui.adapter.CategoryViewAdapter;
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
            json = Utils.loadJSON("file:///android_asset/categories.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
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
    }
}
