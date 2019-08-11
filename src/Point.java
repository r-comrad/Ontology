public class Point {
    public double mX;
    public double mY;

    public Point() {
        mX = 0;
        mY = 0;
    }

    public Point(double aX, double aY) {
        mX = aX;
        mY = aY;
    }

    public Point(Point other) {
        mY = other.mY;
        mX = other.mX;
    }

    public void reset(double aX, double aY) {
        mX = aX;
        mY = aY;
    }

    public double getX() {
        return mX;
    }

    public double getY() {
        return mY;
    }
}