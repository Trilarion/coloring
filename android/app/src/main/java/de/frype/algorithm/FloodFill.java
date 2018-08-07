package de.frype.algorithm;

import de.frype.util.Vector2D;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

/**
 * Floodfill algorithm adapted for coloring, i.e. all values in a 2D data array matching a given value are marked in
 * a mask starting from a given starting position.
 */
public class FloodFill {

    private FloodFill() {}

    /**
     * Simple version (testing one by one), which is very slow. Only for testing purposes.
     *
     * @param position
     * @param mask
     * @param data
     * @param width
     * @param height
     * @param value
     */
    public static void simple_fill(Vector2D position, byte[] mask, int[] data, int width, int height, int value) {

        // create queue and add initial position
        Queue<Vector2D> queue = new ArrayDeque<>();
        queue.add(position);

        // unit vectors in x and y direction
        final Vector2D ex = new Vector2D(1, 0);
        final Vector2D ey = new Vector2D(0, 1);

        // the heap loop
        while (!queue.isEmpty()) {
            // get next point
            Vector2D p = queue.remove();
            int i = p.x + p.y * width;

            if (mask[i] != 0) {
                // mark as processed and set data to value
                mask[i] = 0;
                data[i] = value;

                // up
                if (p.y > 0) {
                    queue.add(Vector2D.subtract(p, ey));
                }
                // down
                if (p.y < height - 1) {
                    queue.add(Vector2D.add(p, ey));
                }
                // left
                if (p.x > 0) {
                    queue.add(Vector2D.subtract(p, ex));
                }
                // right
                if (p.x < width - 1) {
                    queue.add(Vector2D.add(p, ex));
                }
            }
        }
    }

    /**
     * nonzero areas of mask should be filled, zero areas should be avoided.
     *
     * @param position initial position
     * @param mask map of possible positions
     * @param data actual data
     * @param width width of array
     * @param height height of array
     * @param value matching value
     */
    public static void advanced_fill(Vector2D position, byte[] mask, int[] data, int width, int height, int value) {

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
            // xl now points to a valid position in the line segment or to l.xr+1 and there is no valid position at all

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

    /**
     * Immutable line segment. Consisting of left and right x position and y position
     */
    private static class LineSegment {
        final int xl;
        final int xr;
        final int y;

        LineSegment(int xl, int xr, int y) {
            this.xl = xl;
            this.xr = xr;
            this.y = y;
        }

        LineSegment(Vector2D p) {
            xl = p.x;
            xr = p.x;
            y = p.y;
        }
    }
}
