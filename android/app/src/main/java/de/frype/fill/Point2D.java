package de.frype.fill;

/**
 * Immutable Point class.
 */
public class Point2D {

    final int x;
    final int y;

    public Point2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point2D(Point2D other) {
        x = other.x;
        y = other.y;
    }

    public Point2D plus(Point2D other) {
        return new Point2D(x + other.x, y + other.y);
    }

    public Point2D minus(Point2D other) {
        return new Point2D(x - other.x, y - other.y);
    }
}
