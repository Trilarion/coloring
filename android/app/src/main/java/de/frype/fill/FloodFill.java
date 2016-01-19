package de.frype.fill;

import android.graphics.Point;
import android.graphics.Rect;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Jan on 08.01.2016.
 */
public class FloodFill {

    private FloodFill() {}

    public static void simple_fill(Point2D position, byte[] mask, int[] data, int width, int height, int value) {

        // create queue and add initial position
        Queue<Point2D> queue = new ArrayDeque<>();
        queue.add(position);

        // the heap loop
        while (!queue.isEmpty()) {
            // get next point
            Point2D p = queue.remove();
            int i = p.x + p.y * width;

            if (mask[i] != 0) {
                // mark as processed and set data to value
                mask[i] = 0;
                data[i] = value;

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
    }

    /**
     * nonzero areas of mask should be filled, zero areas should be avoided.
     *
     * @param position
     * @param mask
     * @param data
     * @param width
     * @param height
     * @param value
     */
    public static void advanced_fill(Point2D position, byte[] mask, int[] data, int width, int height, int value) {

        // create queue and add initial position
        Queue<LineSegment> queue = new ArrayDeque<>();
        queue.add(new LineSegment(position));

        // the heap loop
        while (!queue.isEmpty()) {
            // get next point
            LineSegment l = queue.remove();

            int o = l.y * width; // line offset

            // starting from xl search right for first appearance
            int xl = l.xl;
            while (xl <= l.xr && mask[o + xl] == 0) {
                xl++;
            }
            // xl now points to a valid position in the line sigment or to l.xr+1 and there is no valid position at all

            // if it points to a valid position
            if (xl <= l.xr) {

                // store xl to xr
                int xr = xl;

                // search left with xl once (to catch anything left of l.xl)
                while (xl > 0 && mask[o + xl - 1] != 0) {
                    xl--;
                }
                // xl is now a left valid position

                // is this already the last new line segment
                while (xl <= l.xr) {

                    // extend xr to the right as much as possible
                    while (xr < width - 1 && mask[o + xr + 1] != 0) {
                        xr++;
                    }

                    // [xl, xr] is new valid line segment

                    // fill it
                    Arrays.fill(mask, o + xl, o + xr + 1, (byte) 0);
                    Arrays.fill(data, o + xl, o + xr + 1, value);

                    // add new intervals if necessary
                    if (l.y > 0) {
                        queue.add(new LineSegment(xl, xr, l.y - 1));
                    }
                    if (l.y < height - 1) {
                        queue.add(new LineSegment(xl, xr, l.y + 1));
                    }

                    // set xl to xr + 1
                    xl = xr + 1;
                    while (xl <= l.xr && mask[o + xl] == 0) {
                        xl++;
                    }
                    // xl is now the next valid position or l.xr+1

                    // set xr to xl
                    xr = xl;
                }
            }
        }
    }

    private static class LineSegment {
        int xl, xr;
        int y;

        LineSegment(int xl, int xr, int y) {
            this.xl = xl;
            this.xr = xr;
            this.y = y;
        }

        LineSegment(Point2D p) {
            xl = p.x;
            xr = p.x;
            y = p.y;
        }
    }
}
