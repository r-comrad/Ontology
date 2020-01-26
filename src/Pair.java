public class Pair<T1, T2> {
    public T1 mX;
    public T2 mY;

    public Pair(T1 aX, T2 aY) {
        mX = aX;
        mY = aY;
    }

    public Pair(Pair<T1, T2> other) {
        mX = other.getX();
        mY = other.mY;
    }

    public void reset(T1 aX, T2 aY) {
        mX = aX;
        mY = aY;
    }

    public T1 getX() {
        return mX;
    }

    public T1 getFirst() {
        return mX;
    }

    public T2 getY() {
        return mY;
    }

    public T2 getSecond() {
        return mY;
    }
}