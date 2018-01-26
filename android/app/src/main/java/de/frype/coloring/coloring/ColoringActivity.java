package de.frype.coloring.coloring;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import de.frype.coloring.ColoringUtils;
import de.frype.coloring.library.Library;
import de.frype.coloring.R;
import de.frype.coloring.color_picker.ColorPickerActivity;
import de.frype.util.Utils;

/**
 * This is the main activity of the app, controlling the coloring of a picture (page in a book).
 */
public class ColoringActivity extends Activity {

    private static final int PICK_COLOR_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coloring);

        // color picker button action: show color picker activity
        View colorPickerButton = findViewById(R.id.colorPickerButton);
        colorPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ColoringActivity.this, ColorPickerActivity.class);
                startActivityForResult(intent, PICK_COLOR_REQUEST);
            }
        });

        // coloring view
        final ColoringView coloringView = findViewById(R.id.coloringView);
        ViewTreeObserver vto = coloringView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Utils.removeOnGlobalLayoutListener(coloringView, this);

                // load the bitmap when we see it the first time
                Bitmap bitmap = Library.getInstance().loadCurrentPageBitmap();

                // TODO should maybe also have a nice border

                // bitmap.setHasAlpha(false); // API level 12
                coloringView.setBitmap(bitmap);
                coloringView.invalidate();
            }
        });

        // back button action
        final ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!coloringView.isModified()) {
                    // not modified just finish
                    finish();
                } else {
                    // modified, show alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(ColoringActivity.this);
                    builder.setTitle(R.string.coloring_end_dialog_title);
                    builder.setNegativeButton(R.string.coloring_end_dialog_negative, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
                    builder.setNeutralButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                    builder.setPositiveButton(R.string.coloring_end_dialog_positive, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Context context = ColoringActivity.this;
                            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                            File file = new File(path, "DemoPicture.png");
                            // getExternalStorageState
                            OutputStream out = null;
                            try {
                                out = new FileOutputStream(file);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            Bitmap bitmap = coloringView.getBitmap();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

                            MediaScannerConnection.scanFile(context, new String[] { file.toString() }, null, null);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        // update color picker button with current active color
        updateColorOfColorPickerButton();
    }

    private void updateColorOfColorPickerButton() {
        View view = findViewById(R.id.colorPickerButton);

        int color = Library.getInstance().getSelectedColor();
        int[] gradientColors = ColoringUtils.colorSelectionButtonBackgroundGradient(color);

        if (Build.VERSION.SDK_INT < 16) {
            GradientDrawable newGradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, gradientColors);
            newGradientDrawable.setStroke(1, Color.parseColor("#bbbbbb"));
            newGradientDrawable.setCornerRadius(5);
            //noinspection deprecation
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
            int color = data.getIntExtra("color", 0);
            Library.getInstance().setSelectedColor(color);
            updateColorOfColorPickerButton();
        }
    }
}
