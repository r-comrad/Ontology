public class Point {
    public int x;
    public int y;

    public Point() {
        x = 0;
        y = 0;
    }

    public Point(int aX, int aY) {
        x = aX;
        y = aY;
    }

    public Point(Point other) {
        x = other.x;
        y = other.y;
    }
}