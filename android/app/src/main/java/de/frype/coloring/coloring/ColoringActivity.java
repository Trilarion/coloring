package de.frype.coloring.coloring;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
                startActivityForResult(intent, ColorPickerActivity.PICK_COLOR_REQUEST);
            }
        });

        // coloring view
        final ColoringView coloringView = findViewById(R.id.coloringView);
        ViewTreeObserver vto = coloringView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Utils.removeOnGlobalLayoutListener(coloringView, this);

                // load and set the coloring page bitmap after the ColoringView has been laid out and knows its size
                Bitmap bitmap = Library.getInstance().loadCurrentPageBitmap();
                coloringView.setBitmap(bitmap);
                coloringView.invalidate();
            }
        });

        // back button action: check if modified (if so, ask for desired action)
        final ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!coloringView.isModified()) {
                    // not modified just finish
                    finish();
                } else {
                    // modified, show alert dialog asking for "Finish?"
                    AlertDialog.Builder builder = new AlertDialog.Builder(ColoringActivity.this);
                    builder.setTitle(R.string.coloring_end_dialog_title);
                    // negative answer is cancel, which just doesn't do anything
                    builder.setNegativeButton(R.string.dialog_cancel, null);
                    // neutral answer is to finish the dialog without saving
                    builder.setNeutralButton(R.string.coloring_end_dialog_neutral, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
                    // positive answer is to save and exit
                    builder.setPositiveButton(R.string.coloring_end_dialog_positive, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Context context = ColoringActivity.this;
                            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                            File file = new File(path, "DemoPicture.png");
                            // getExternalStorageState
                            // TODO this does crash
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

    /**
     * Updates the color of the color picker selected button with the actual color (a gradient from it).
     */
    private void updateColorOfColorPickerButton() {
        View view = findViewById(R.id.colorPickerButton);

        // takes the actually selected color
        int color = Library.getInstance().getSelectedColor();
        int[] gradientColors = ColoringUtils.colorSelectionButtonBackgroundGradient(color);

        if (Build.VERSION.SDK_INT < 16) {
            GradientDrawable newGradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, gradientColors);
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
        // handling of color picker activity result
        if (requestCode == ColorPickerActivity.PICK_COLOR_REQUEST && resultCode == RESULT_OK) {
            int color = data.getIntExtra("color", 0);
            // set color in library
            Library.getInstance().setSelectedColor(color);
            // updaste color of button
            updateColorOfColorPickerButton();
        }
    }
}
