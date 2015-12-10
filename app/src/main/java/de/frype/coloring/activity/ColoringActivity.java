package de.frype.coloring.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import de.frype.coloring.Library;
import de.frype.coloring.R;
import de.frype.coloring.widget.ColoringView;

public class ColoringActivity extends Activity {

    private static final int PICK_COLOR_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coloring);

        ImageButton imageButton = (ImageButton) findViewById(R.id.backButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageButton = (ImageButton) findViewById(R.id.colorPickerButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
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

        final LinearLayout favoriteColorsLayout = (LinearLayout) findViewById(R.id.favoriteColorsLayout);
        vto = favoriteColorsLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    favoriteColorsLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    favoriteColorsLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int height_px = favoriteColorsLayout.getHeight();
                // convert to dp
                float height_dp = height_px / ColoringActivity.this.getResources().getDisplayMetrics().density;
                View.inflate(ColoringActivity.this, R.layout.element_color_button, favoriteColorsLayout);
                View.inflate(ColoringActivity.this, R.layout.element_color_button, favoriteColorsLayout);
                View.inflate(ColoringActivity.this, R.layout.element_color_button, favoriteColorsLayout);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_COLOR_REQUEST && resultCode == RESULT_OK) {
                int color = data.getIntExtra("color", 0);
        }
    }
}
