package de.frype.fill;

import android.graphics.Point;
import android.graphics.Rect;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by Jan on 08.01.2016.
 */
public class Fill {

    private Fill() {}

    public static Rect fill(Point2D position, byte[] mask, int[] data, int width, int height, int value) {

        // left, top and right, bottom position
        Point2D lt = new Point2D(position);
        Point2D rb = new Point2D(position);

        // create queue and add initial position
        Queue<Point2D> queue = new ArrayDeque<>();
        queue.add(position);

        // the heap loop
        while (!queue.isEmpty()) {
            // get next point
            Point2D p = queue.remove();
            int i = p.x + p.y * width;

            if (mask[i] == 1) {
                // mark as processed and set data to value
                mask[i] = 2;
                data[i] = value;

                // update lt, rb
                if (p.x < lt.x) {
                    lt.x = p.x;
                }
                if (p.x > rb.x) {
                    rb.x = p.x;
                }
                if (p.y < lt.y) {
                    lt.y = p.y;
                }
                if (p.y > rb.y) {
                    rb.y = p.y;
                }

                // up
                if (p.y > 0) {
                    queue.add(new Point2D(p.x, p.y - 1));
                }
                // down
                if (p.y < height - 1) {
                    queue.add(new Point2D(p.x, p.y + 1));
                }
                // left
                if (p.x > 0) {
                    queue.add(new Point2D(p.x - 1, p.y));
                }
                // right
                if (p.x < width - 1) {
                    queue.add(new Point2D(p.x + 1, p.y));
                }
            }
        }

        // reset mask to 1 where it is 2
        for (int y = lt.y; y <= rb.y; y++) {
            for (int x = lt.x; x <= rb.x; x++) {
                int i = x + y * width;
                if (mask[i] == 2) {
                    mask[i] = 1;
                }
            }
        }

        // bounding box
        Rect box = new Rect(lt.x, lt.y, rb.x - lt.x + 1, rb.y - lt.y + 1);
        return box;
    }
}
