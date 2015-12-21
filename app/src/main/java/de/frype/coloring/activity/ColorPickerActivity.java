package de.frype.coloring.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;

import de.frype.coloring.R;
import de.frype.coloring.layout.RegularGridLayout;
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

        final RegularGridLayout layout = (RegularGridLayout) findViewById(R.id.colorSelectionGridLayout);
        layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Utils.removeOnGlobalLayoutListener(layout, this);
                Context context = ColorPickerActivity.this;
                int button_space = (int)(context.getResources().getDimension(R.dimen.color_selection_button_size) + context.getResources().getDimension(R.dimen.color_selection_button_margin));

                int width_px = layout.getWidth();
                int height_px = layout.getHeight();

                int columns = (int) Math.floor(width_px / button_space);
                int rows = (int) Math.floor(height_px / button_space);

                layout.setColumns(columns);
                layout.setRows(rows);

                for (int row = 0; row < rows; row++) {
                    for (int column = 0; column < columns; column++) {
                        View view = View.inflate(context, R.layout.element_color_selection_grid, layout);
                    }
                }
                layout.requestLayout();
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