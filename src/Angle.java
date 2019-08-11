public class Angle implements Cloneable {
    private double mValue;

    public Angle(double aInit) {

        mValue = aInit;
    }

    public Angle(Angle aAngle) {

        mValue = aAngle.mValue;
    }

    public double getSin() {

        return Math.sin(mValue / 180 * 3.14);
    }

    public double getCos() {

        return Math.cos(mValue / 180 * 3.14);
    }

    public void add(double aAngle) {
        mValue += aAngle;
        if (mValue > 360) mValue %= 360;
        else if (mValue < 0) mValue += 360;
    }

    public Angle clone() throws CloneNotSupportedException {
        return (Angle) super.clone();
    }
}