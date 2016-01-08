package de.frype.coloring.coloring;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;

import de.frype.coloring.ColoringUtils;
import de.frype.coloring.Library;
import de.frype.coloring.R;
import de.frype.coloring.color_picker.ColorPickerActivity;

public class ColoringActivity extends Activity {

    private static final int PICK_COLOR_REQUEST = 1;
    private int selectedColor = Color.BLUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coloring);

        final ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton selectColorButton = (ImageButton) findViewById(R.id.colorPickerButton);
        selectColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ColoringActivity.this, ColorPickerActivity.class);
                startActivityForResult(intent, PICK_COLOR_REQUEST);
            }
        });

        final ColoringView coloringView = (ColoringView) findViewById(R.id.coloringView);
        ViewTreeObserver vto = coloringView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    coloringView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    coloringView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                Bitmap bitmap = Library.getInstance().loadCurrentPageBitmap(getAssets());
                coloringView.setBitmap(bitmap);
            }
        });

        updateSelectedColorButton();
    }

    private void updateSelectedColorButton() {
        View view = findViewById(R.id.selectedColorView);

        int[] gradientColors = ColoringUtils.colorSelectionButtonBackgroundGradient(selectedColor);

        if (Build.VERSION.SDK_INT < 16) {
            GradientDrawable newGradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, gradientColors);
            newGradientDrawable.setStroke(1, Color.parseColor("#bbbbbb"));
            newGradientDrawable.setCornerRadius(5);
            view.setBackgroundDrawable(newGradientDrawable);
        } else {
            GradientDrawable drawable = (GradientDrawable) view.getBackground();
            drawable.mutate();
            drawable.setColors(gradientColors);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_COLOR_REQUEST && resultCode == RESULT_OK) {
                selectedColor = data.getIntExtra("color", 0);
                updateSelectedColorButton();
        }
    }
}
