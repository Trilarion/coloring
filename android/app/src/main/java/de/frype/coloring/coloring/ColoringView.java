package de.frype.coloring.coloring;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.lang.reflect.Array;
import java.util.Arrays;

import de.frype.coloring.library.Library;
import de.frype.fill.Fill;
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
        bitmap.getPixels(data, 0, width, 0, 0, width, height);

        // create mask (0 for non-white, 1 for white)
        mask = new byte[n];
        for (int i = 0; i < n; i++) {
            if (((data[i] >> 16) & 0xff) > 20) {
                mask[i] = 1;
            }
        }
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
        // test if there is some white area
        if (mask[x + y * width] == 1) {
            // start filling
            Fill.fill(new Point2D(x, y), mask, data, width, height, color);

            // update bitmap
            bitmap.setPixels(data, 0, width, 0, 0, width, height);

            // invalidate
            invalidate();
        }
    }
}
