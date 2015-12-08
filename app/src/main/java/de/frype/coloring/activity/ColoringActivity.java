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
import de.frype.coloring.widget.ColoringView;

public class ColoringActivity extends Activity {

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
                startActivity(intent);
            }
        });

        final ColoringView coloringView = (ColoringView) findViewById(R.id.coloringView);
        final ViewTreeObserver vto = coloringView.getViewTreeObserver();
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
    }
}
