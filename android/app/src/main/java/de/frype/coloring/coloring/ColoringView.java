package de.frype.coloring.coloring;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TimingLogger;
import android.view.MotionEvent;
import android.view.View;

import de.frype.coloring.library.Library;
import de.frype.fill.FloodFill;
import de.frype.fill.Point2D;

/**
 * Created by Jan on 19.11.2015.
 */
public class ColoringView extends View {

    private Bitmap bitmap;
    private int offset_width = 0;
    private int offset_height = 0;
    private int width;
    private int height;
    private byte[] mask;
    private byte[] temporary_mask;
    private int[] data;

    public ColoringView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);

        if (bitmap != null) {
            canvas.drawBitmap(bitmap, offset_width, offset_height, null); // TODO provide a custom Paint?
        }
    }

    public void setBitmap(Bitmap bitmap) {
        // scale so that aspect ratio is kept and canvas is filled

        // compute optimal scale as maximal scaling factor
        float width_scale_factor = (float) bitmap.getWidth() / getWidth();
        float height_scale_factor = (float) bitmap.getHeight() / getHeight();
        float scale_factor = Math.max(width_scale_factor, height_scale_factor);

        // scale with a single scale factor (keeps aspect ratio
        width = (int) Math.floor(bitmap.getWidth() / scale_factor);
        height = (int) Math.floor(bitmap.getHeight() / scale_factor);

        // compute offset
        offset_width = (int) Math.floor((getWidth() - width) / 2);
        offset_height = (int) Math.floor((getHeight() - height) / 2);

        // scale bitmap
        this.bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);

        // get pixels
        int n = width * height;
        data = new int[n];
        this.bitmap.getPixels(data, 0, width, 0, 0, width, height);

        // create mask (== 0 for non-white, != 0 (=1) for white/fill)
        mask = new byte[n];
        for (int i = 0; i < n; i++) {
            if (((data[i] >> 16) & 0xff) == 255) {
                mask[i] = 1;
            }
        }
        temporary_mask = new byte[n];
    }

    @Override
    public boolean onTouchEvent (MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // get event position and correct for bitmap offsets
            int x = (int) event.getX() - offset_width;
            int y = (int) event.getY() - offset_height;
            // test if within bitmap
            if (x >= 0 && x < bitmap.getWidth() && y >= 0 && y < bitmap.getHeight()) {
                // go for the coloring
                color(x, y);
            }
        }
        return true;
    }

    private void color(int x, int y) {
        // get actual color
        int color = Library.getInstance().getSelectedColor();
        // test if there is some white area in the mask and the data has not yet that color
        if (mask[x + y * width] != 0 && data[x + y * width] != color) {
            // fill

            // copy mask to temporary mask
            System.arraycopy(mask, 0, temporary_mask, 0, width * height);

            long t0 = System.nanoTime();
            FloodFill.advanced_fill(new Point2D(x, y), temporary_mask, data, width, height, color);
            // FloodFill.simple_fill(new Point2D(x, y), temporary_mask, data, width, height, color);
            long t1 = System.nanoTime();

            // update bitmap
            long t2 = System.nanoTime();
            bitmap.setPixels(data, 0, width, 0, 0, width, height);
            long t3 = System.nanoTime();
            Log.v("COL", "test");
            Log.v("COL", String.format("fill algorithm: %.4fms", (t1 - t0) / 1e6));
            Log.v("COL", String.format("copy pixels:    %.4fms", (t3 - t2) / 1e6));
            Log.v("COL", String.format("width %d, height %d", width, height));

            // invalidate
            invalidate();
        }
    }
}