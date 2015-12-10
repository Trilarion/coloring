package de.frype.coloring.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;

import de.frype.coloring.Library;
import de.frype.coloring.R;
import de.frype.coloring.widget.ColorPickerView;
import de.frype.coloring.widget.ColoringView;

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

        final ColorPickerView colorPickerView = (ColorPickerView) findViewById(R.id.colorPickerView);
        final ViewTreeObserver vto = colorPickerView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    colorPickerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    colorPickerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                colorPickerView.createBitmap();
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