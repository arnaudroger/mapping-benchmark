package results;

import java.util.concurrent.TimeUnit;

/**
 * Created by aroger on 25/10/2015.
 */
public enum Unit {



    ops_per_m(Mode.thrpt, TimeUnit.MINUTES),
    ops_per_s(Mode.thrpt, TimeUnit.SECONDS),
    ops_per_ms(Mode.thrpt, TimeUnit.MILLISECONDS),
    ops_per_us(Mode.thrpt, TimeUnit.MICROSECONDS),
    ops_per_ns(Mode.thrpt, TimeUnit.NANOSECONDS),
    percent_from_ref_thrpt(Mode.thrpt, null) {
        @Override
        public boolean isPercent() {
            return true;
        }
    },
    m_per_op(Mode.avgt, TimeUnit.MINUTES),
    s_per_op(Mode.avgt, TimeUnit.SECONDS),
    ms_per_op(Mode.avgt, TimeUnit.MILLISECONDS),
    us_per_op(Mode.avgt, TimeUnit.MICROSECONDS),
    ns_per_op(Mode.avgt, TimeUnit.NANOSECONDS),
    percent_from_ref_avgt(Mode.avgt, null) {
        @Override
        public boolean isPercent() {
            return true;
        }
    };

    private final Mode mode;
    private final TimeUnit timeUnit;

    Unit(Mode mode, TimeUnit timeUnit) {
        this.mode = mode;
        this.timeUnit = timeUnit;
    }

    public Mode getMode() {
        return mode;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public static Unit fromString(String value) {
        return Enum.valueOf(Unit.class, value.replace("/", "_per_"));
    }

    public boolean isThroughOuput() {
        return mode == Mode.thrpt;
    }

    public boolean isLatency() {
        return !isThroughOuput();
    }

    public Unit invert() {
        switch (this) {
            case m_per_op:return ops_per_m;
            case s_per_op:return ops_per_s;
            case ms_per_op:return ops_per_ms;
            case us_per_op:return ops_per_us;
            case ns_per_op:return ops_per_ns;
            case ops_per_m:return ms_per_op;
            case ops_per_s:return s_per_op;
            case ops_per_ms:return ms_per_op;
            case ops_per_us:return us_per_op;
            case ops_per_ns:return ns_per_op;
            default:throw new IllegalArgumentException();
        }
    }

    public Unit toTimeUnit(TimeUnit tu) {
        switch (mode) {
            case avgt:
                switch (tu) {
                    case MINUTES: return m_per_op;
                    case SECONDS: return s_per_op;
                    case MILLISECONDS:return ms_per_op;
                    case MICROSECONDS:return us_per_op;
                    case NANOSECONDS: return ns_per_op;
                }
                break;
            case thrpt:
                switch (tu) {
                    case MINUTES: return ops_per_m;
                    case SECONDS: return ops_per_s;
                    case MILLISECONDS:return ops_per_ms;
                    case MICROSECONDS:return ops_per_us;
                    case NANOSECONDS: return ops_per_ns;
                }
        }
        throw new IllegalArgumentException();
    }

    public boolean isPercent() {
        return false;
    }
}
