package de.frype.coloring.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.GridView;
import android.widget.ImageButton;

import de.frype.coloring.R;
import de.frype.coloring.adapter.BookSelectionAdapter;
import de.frype.coloring.adapter.ColorSelectionAdapter;
import de.frype.util.Utils;

public class ColorPickerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);

        ImageButton imageButton = (ImageButton) findViewById(R.id.backButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        final GridView gridView = (GridView) findViewById(R.id.colorSelectionGridView);
        ViewTreeObserver vto = gridView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Utils.removeOnGlobalLayoutListener(gridView, this);
                Context context = ColorPickerActivity.this;
                float density = context.getResources().getDisplayMetrics().density;

                float color_button_size_dp = context.getResources().getDimension(R.dimen.color_button_size) / density + 4;

                int width_px = gridView.getWidth();
                float width_dp = width_px / density;
                int columns = (int) Math.floor(width_dp / color_button_size_dp);
                gridView.setNumColumns(columns - 3);

                int height_px = gridView.getHeight();
                float height_dp = height_px / density;
                int rows = (int) Math.floor(height_dp / color_button_size_dp);

                gridView.setAdapter(new ColorSelectionAdapter(context, columns - 3, rows - 2));
           }
        });

    }

    public void colorSelected(int color) {
        Intent data = new Intent();
        data.putExtra("color", color);
        setResult(RESULT_OK, data);
        finish();
    }
}