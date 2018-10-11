package de.frype.coloring.picture_gallery;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

import de.frype.coloring.R;
import de.frype.coloring.library.Library;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;

/**
 * A picture gallery (book specific). Sorted by page and date. Ability to show stored images, delete images and
 * share images.
 *
 * The gallery would now be conveniently be implemented by a ViewPager, but for compatibility an extended ImageSwitcher
 * is fine as well.
 */
public class PictureGalleryActivity extends Activity {

    private static final int MIN_DISTANCE_FLING = 100; // 100 px
    private static final int MIN_VELOCITY_FLING = 100; // 100 px/s
    private int currentPicture = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_gallery);

        // gallery only works if the external storage is available
        boolean externalStorageAvailable = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (!externalStorageAvailable) {
            Toast toast = Toast.makeText(this, getString(R.string.toast_external_storage_unavailable), Toast.LENGTH_SHORT);
            toast.show();
            finish();
            return;
        }

        // get pictures
        File externalStorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File base_directory = new File(externalStorageDir, Library.getInstance().getStringFromCurrentBook("name"));
        // nothing to see here yet
        if (!base_directory.exists()) {
            Toast toast = Toast.makeText(this, getString(R.string.toast_gallery_no_pictures), Toast.LENGTH_SHORT);
            toast.show();
            finish();
            return;
        }
        final File[] pictures = base_directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return !pathname.isDirectory() && pathname.getName().endsWith(".png");
            }
        });
        // nothing to see here yet
        if (pictures.length == 0) {
            Toast toast = Toast.makeText(this, getString(R.string.toast_gallery_no_pictures), Toast.LENGTH_SHORT);
            toast.show();
            finish();
            return;
        }
        // sort by last modified date in descending order
        Arrays.sort(pictures, new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                // later modified (lastModified() larger) shall compare as being smaller
                return (int) f2.lastModified() - (int) f1.lastModified();
            }
        });

        // back button action: go back
        ImageButton imageButton = findViewById(R.id.backButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // if the settings say so, remove the share and delete button from the gallery view
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        // share button action
        imageButton = findViewById(R.id.shareButton);
        if (!sharedPref.getBoolean("setting_sharing_allowed", true)) {
            ((RelativeLayout) imageButton.getParent()).removeView(imageButton);
        } else {
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO share the currently selected image
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("image/png");
                    // sharingIntent.putExtra(Intent.EXTRA_STREAM, _newImageUri);
                    // startActivity(Intent.createChooser(sharingIntent, getString( R.string.dialog_share )));
                }
            });
        }

        // delete button action: delete the currently selected image
        imageButton = findViewById(R.id.deleteButton);
        if (!sharedPref.getBoolean("setting_deletion_allowed", true)) {
            ((RelativeLayout) imageButton.getParent()).removeView(imageButton);
        } else {
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO not yet implemented
                }
            });
        }

        TextView imageDescription = findViewById(R.id.galleryImageDescriptionTextView);

        // load animations
        final Animation leftIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left);
        final Animation leftOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_to_left);
        final Animation rightIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right);
        final Animation rightOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_to_right);


        // image switcher
        final ImageSwitcher imageSwitcher = findViewById(R.id.galleryImageSwitcher);
        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float dx = e1.getX() - e2.getX();
                float dy = e1.getY() - e2.getY();
                // only if movement along x is larger than movement along y and larger than minimal value
                if (Math.abs(dx) > Math.abs(dy) && Math.abs(dx) > MIN_DISTANCE_FLING) {
                    // only if velocity in x is larger than minimal value
                    if (Math.abs(velocityX) > MIN_VELOCITY_FLING) {
                        // left or right
                        if (dx < 0) {
                            if (currentPicture > 0) {
                                currentPicture--;
                                imageSwitcher.setOutAnimation(rightOut);
                                imageSwitcher.setInAnimation(leftIn);
                                imageSwitcher.setImageURI(Uri.fromFile(pictures[currentPicture]));
                            }
                        } else {
                            if (currentPicture < pictures.length-1) {
                                currentPicture++;
                                imageSwitcher.setOutAnimation(leftOut);
                                imageSwitcher.setInAnimation(rightIn);
                                imageSwitcher.setImageURI(Uri.fromFile(pictures[currentPicture]));
                            }
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        // view animator OnTouchListener, delegates to gesture detector
        imageSwitcher.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });


        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            public View makeView() {
                // Create a new ImageView and set it's properties
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ImageSwitcher.LayoutParams.MATCH_PARENT, ImageSwitcher.LayoutParams.MATCH_PARENT));
                return imageView;
            }
        });

        // set actual image
        imageSwitcher.setImageURI(Uri.fromFile(pictures[currentPicture]));
        imageDescription.setText(String.format("%d/%d", currentPicture, pictures.length));
    }
}
