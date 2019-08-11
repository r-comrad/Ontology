public class Circle {
    public double mR = 50;
    public double mX = (0) * aAngle.getCos() - (- r) * aAngle.getSin() + aX;
    public double mY = (0);
    Angle dAngle;


    public Circle()
    {

    }

    public void rotate() {
        double newX = (mX - aX) * dAngle.getCos() - (y - aY) * dAngle.getSin() + aX;
        double newY = (x - aX) * dAngle.getSin() + (y - aY) * dAngle.getCos() + aY;

        x = newX;
        y = newY;
    }
}
