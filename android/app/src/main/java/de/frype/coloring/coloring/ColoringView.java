package de.frype.coloring.coloring;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import de.frype.coloring.Library;

/**
 * Created by Jan on 19.11.2015.
 */
public class ColoringView extends View {

    private Bitmap bitmap;

    public ColoringView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);

        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0, 0, null); // TODO provide a custom Paint?
        }
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = Bitmap.createScaledBitmap(bitmap, getWidth(), getHeight(), false);
        invalidate();
    }

    @Override
    public boolean onTouchEvent (MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            color((int) event.getX(), (int) event.getY());
        }
        return true;
    }

    private void color(int x, int y) {

    }
}
