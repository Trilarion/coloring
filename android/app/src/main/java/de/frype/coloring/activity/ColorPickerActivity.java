package de.frype.coloring.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
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
                float button_space_px = context.getResources().getDimension(R.dimen.color_selection_button_size) + 2 * context.getResources().getDimension(R.dimen.color_selection_button_margin);

                int width_px = layout.getWidth();
                int height_px = layout.getHeight();

                int columns = (int) Math.floor(width_px / button_space_px);
                int rows = (int) Math.floor(height_px / button_space_px);

                layout.setColumns(columns);
                layout.setRows(rows);

                LayoutInflater inflater = LayoutInflater.from(context);
                float[] hsv = new float[3];
                for (int row = 0; row < rows; row++) {
                    for (int column = 0; column < columns; column++) {
                        View view = inflater.inflate(R.layout.element_color_selection_grid, layout, false);
                        layout.addView(view);
                        float value = (float) row / rows; // [0,1)
                        if (column == 0) {
                            // grey level
                            hsv[0] = 0; // hue = 0
                            hsv[1] = 0; // saturation = 0
                            hsv[2] = value * 0.8f + 0.1f; // ramp with offset
                        } else {
                            // rainbow
                            hsv[0] = (float) (column - 1) / (columns - 1) * 360; // [0,360)
                            hsv[1] = (0.2f - 1) * value + 1 + value * (1 - value); // saturation
                            hsv[2] = (1 - 0.2f) * value + 0.2f + value * (1 - value); // value
                        }

                        final int color = Color.HSVToColor(hsv);
                        GradientDrawable drawable = (GradientDrawable) view.getBackground();
                        drawable.mutate();

                        float v = hsv[2];
                        int[] gradientColors = new int[2];
                        // darken
                        hsv[2] *= 0.8f;
                        gradientColors[0] = Color.HSVToColor(hsv);
                        // lighten
                        hsv[2] = 1 - 0.8f * (1 - v);
                        gradientColors[1] = Color.HSVToColor(hsv);

                        drawable.setColors(gradientColors);

                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ColorPickerActivity.this.colorSelected(color);
                            }
                        });
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