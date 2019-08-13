public class CircleManager {
    double mR = 75;

    private Pair<Double, Double>  mCenter;
    private Pair<Double, Double>  mCirclePoint;

    private Angle mMainAngle;
    private Angle mAngleChange;

    public CircleManager(double aX, double aY) {
        //double r = 50;

        mCenter = new Pair(aX, aY);
        mMainAngle = new Angle(0);

        double x = aX;
        double y = aY - mR;
        mCirclePoint = new Pair(x, y);
    }

    public CircleManager(CircleManager aOther) {
        //double r = 50;

        mMainAngle = new Angle(aOther.mMainAngle);
        mMainAngle.add(-90);
        mCenter = aOther.mCirclePoint;

        double x = (0) * mMainAngle.getCos() - (-mR) * mMainAngle.getSin() + mCenter.getX();
        double y = (0) * mMainAngle.getSin() + (-mR) * mMainAngle.getCos() + mCenter.getY();
        mCirclePoint = new Pair(x, y);
    }

    public void rotatePoint() {
        double newX = (mCirclePoint.getX() - mCenter.getX()) * mAngleChange.getCos()
                - (mCirclePoint.getY() - mCenter.getY()) * mAngleChange.getSin() + mCenter.getX();
        double newY = (mCirclePoint.getX() - mCenter.getX()) * mAngleChange.getSin()
                + (mCirclePoint.getY() - mCenter.getY()) * mAngleChange.getCos() + mCenter.getY();
        mCirclePoint.reset(newX, newY);
        //angle.add(dAngle);
    }

    public void recalculateAngleChange(int aConnectionCount) {
        mAngleChange = new Angle(aConnectionCount == 1 ? 0
                : 180 / (aConnectionCount - 1));

        if(aConnectionCount == 1)
        {
            mMainAngle.add(90);
            double x = (0) * mMainAngle.getCos() - (-mR) * mMainAngle.getSin() + mCenter.getX();
            double y = (0) * mMainAngle.getSin() + (-mR) * mMainAngle.getCos() + mCenter.getY();
            mCirclePoint = new Pair(x, y);
        }
    }

    public Pair getCenter() {
        return mCenter;
    }

}