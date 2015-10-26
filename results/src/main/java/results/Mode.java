package results;

/**
 * Created by aroger on 25/10/2015.
 */
public enum Mode {
    avgt, thrpt;

    public Mode invert() {
        switch (this) {
            case avgt: return thrpt;
            case thrpt: return avgt;
            default:throw new UnsupportedOperationException();
        }
    }
}
