package de.frype.util;

/**
 * Immutable Point class.
 */
public class Vector2D implements Comparable<Vector2D> {

    public static final Vector2D ZERO = new Vector2D(0, 0);
    public final int x;
    public final int y;

    public Vector2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Vector2D)) return false;

        final Vector2D other = (Vector2D) obj;

        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return x * 29 + y;
    }

    public double norm() {
        return Math.sqrt(x * x + y * y);
    }

    public static Vector2D add(Vector2D a, Vector2D b) {
        return new Vector2D(a.x + b.x, a.y + b.y);
    }

    public static Vector2D subtract(Vector2D a, Vector2D b) {
        return new Vector2D(a.x - b.x, a.y - b.y);
    }

    public int compareTo(Vector2D o) {
        if (o.y != y) return y - o.y;
        else return x - o.x;
    }

}
