package de.frype.fill;

/**
 * Created by Jan on 08.01.2016.
 */
public class Point2D {

    int x, y;

    public Point2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point2D(Point2D other) {
        x = other.x;
        y = other.y;
    }
}
