package de.frype.coloring.coloring;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import de.frype.coloring.library.Library;
import de.frype.algorithm.FloodFill;
import de.frype.util.Vector2D;

import java.util.Arrays;

/**
 * The view in the coloring activity that performs the coloring of a bitmap that is mostly black and white (a page in a coloring book).
 */
public class ColoringView extends View {

    /**
     * The bitmap holding the colored image. Initially a bitmap is loaded and transferred to here via setBitmap() where
     * it is scaled and copied.
     */
    private Bitmap bitmap;
    private Vector2D offset;
    private byte[] fill_mask;
    private int[] bitmap_pixels;
    /**
     * Initially the bitmap is not modified, but as soon as a single fill operation has been performed it counts as
     * modified.
     */
    private boolean modified = false;

    public ColoringView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);

        // until we have a bitmap
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, offset.x, offset.y, null);
        }
    }

    /**
     * Called right after the ColoringView has been laid out and knows its size. Inputs the current coloring page
     * and expects it to be drawn with a reasonable position and scale.
     *
     * @param bitmap The current coloring page.
     */
    public void setBitmap(Bitmap bitmap) {
        // scale so that aspect ratio is kept and canvas is filled

        // TODO different modes: stretch, minimal scale, something in between

        // compute desired scale
        float width_scale_factor = (float) bitmap.getWidth() / getWidth();
        float height_scale_factor = (float) bitmap.getHeight() / getHeight();

        // maximal scale factor (everything is inside)
        float scale_factor = Math.max(width_scale_factor, height_scale_factor);

        // TODO limit not scaling more than

        // scale with a single scale factor (keeps aspect ratio)
        int width = (int) Math.floor(bitmap.getWidth() / scale_factor);
        int height = (int) Math.floor(bitmap.getHeight() / scale_factor);

        // scale bitmap
        this.bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);

        // compute offset
        offset = new Vector2D((int) Math.floor((getWidth() - width) / 2), (int) Math.floor((getHeight() - height) / 2));

        // get pixel information from bitmap in int array (a copy is made)
        int n = width * height;
        bitmap_pixels = new int[n];
        this.bitmap.getPixels(bitmap_pixels, 0, width, 0, 0, width, height);

        // create fill_mask from bitmap_pixels (== 0 for non-white, != 0 (=1) for white (which can be filled))
        fill_mask = new byte[n];
        for (int i = 0; i < n; i++) {
            // just test for the second byte (is faster)
            if (((bitmap_pixels[i] >> 16) & 0xff) == 255) {
                fill_mask[i] = 1;
            }
        }
    }

    /**
     * Called at the end of the coloring process. Contains the initially scaled and colored bitmap.
     * @return
     */
    public Bitmap getBitmap() {
        return this.bitmap;
    }

    @Override
    public boolean onTouchEvent (MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // get event position and correct for bitmap offsets
            Vector2D p = new Vector2D((int) event.getX() - offset.x,(int) event.getY() - offset.y);
            // test if within bitmap
            if (p.x >= 0 && p.x < bitmap.getWidth() && p.y >= 0 && p.y < bitmap.getHeight()) {
                // go for the coloring
                color(p);
            }
            // because of lint (accessibility in custom views)
            performClick();
        }
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    /**
     * Performs a fill operation with starting position (x,y). Position is guaranteed to be within the bitmap.
     *
     * @param p start position in the bitmap
     */
    private void color(Vector2D p) {
        // get selected coloring color
        int color = Library.getInstance().getSelectedColor();

        // get size of bitmap
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // test if there is some white area in the fill_mask and the bitmap_pixels has not yet that color
        if (fill_mask[p.x + p.y * width] != 0 && bitmap_pixels[p.x + p.y * width] != color) {
            // copy fill_mask to temporary fill_mask
            byte[] temporary_mask = Arrays.copyOf(fill_mask, fill_mask.length);

            // perform fill
            long t0 = System.nanoTime();
            FloodFill.advanced_fill(p, temporary_mask, bitmap_pixels, width, height, color);
            // FloodFill.simple_fill(p, temporary_mask, bitmap_pixels, width, height, color);
            long t1 = System.nanoTime();

            // update bitmap
            long t2 = System.nanoTime();
            bitmap.setPixels(bitmap_pixels, 0, width, 0, 0, width, height);
            long t3 = System.nanoTime();

            Log.v("COL", String.format("fill algorithm: %.4fms", (t1 - t0) / 1e6));
            Log.v("COL", String.format("copy pixels:    %.4fms", (t3 - t2) / 1e6));
            Log.v("COL", String.format("width %d, height %d", width, height));

            // set modified flag
            this.modified = true;

            // invalidate
            invalidate();
        }
    }

    /**
     * Has any fill operation taken place?
     *
     * @return True if yes.
     */
    public boolean isModified() {
        return this.modified;
    }
}