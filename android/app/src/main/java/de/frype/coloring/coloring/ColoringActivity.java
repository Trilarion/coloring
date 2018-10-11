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
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.widget.Toast;
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
                    final Context context = ColoringActivity.this;

                    boolean externalStorageAvailable = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
                    if (!externalStorageAvailable) {
                        Toast toast = Toast.makeText(context, getString(R.string.toast_external_storage_unavailable), Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    DialogInterface.OnClickListener finishListener = new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    };

                    DialogInterface.OnClickListener saveImageListener = new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // we checked before that the external storage is available

                            File externalStorageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                            File base_directory = new File(externalStorageDir, Library.getInstance().getStringFromCurrentBook("name"));
                            // make sure it exists
                            base_directory.mkdirs();

                            // TODO encode date and time in file name
                            String name = Library.getInstance().getStringFromCurrentPage("file");
                            name = name.substring(0, name.length()-4);
                            SimpleDateFormat sdf = new SimpleDateFormat("_yyyy-MM-dd_HH-mm");
                            name += sdf.format(new Date());
                            name += ".png";
                            Log.v("COL", name);
                            File file = new File(base_directory, name);

                            // we overwrite everything that exists already
                            if (file.exists()) {
                                file.delete();
                            }

                            OutputStream out;
                            try {
                                out = new FileOutputStream(file);
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }

                            Bitmap bitmap = coloringView.getBitmap();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

                            // tell the media scanner about it
                            MediaScannerConnection.scanFile(context, new String[]{file.toString()}, null, null);

                            // finish this activity
                            finish();
                        }
                    };

                    // show alert dialog asking for "Finish?"
                    AlertDialog.Builder builder = new AlertDialog.Builder(ColoringActivity.this);
                    builder.setTitle(R.string.coloring_end_dialog_title);

                    // negative answer is cancel, which just doesn't do anything
                    builder.setNegativeButton(R.string.dialog_cancel, null);

                    if (externalStorageAvailable) {
                        // neutral answer is to finish the dialog without saving
                        builder.setNeutralButton(R.string.coloring_end_dialog_neutral, finishListener);
                        // positive answer is to save and exit
                        builder.setPositiveButton(R.string.coloring_end_dialog_positive, saveImageListener);
                    } else {
                        // positive answer is to finish the dialog without saving
                        builder.setPositiveButton(R.string.coloring_end_dialog_neutral, finishListener);
                    }

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
            newGradientDrawable.setStroke(1, Color.parseColor("#bbbbbb"));
            newGradientDrawable.setCornerRadius(ColoringActivity.this.getResources().getDimension(R.dimen.color_selection_button_corner_radius));
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
