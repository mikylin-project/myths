package cn.mikylin.myths.common.lang;
import java.io.Serializable;
import java.util.Objects;

/**
 * range filter.
 *
 * @author mikylin
 * @date 20200224
 */
public abstract class RangeFilter<T extends Comparable>
        implements Serializable {

    private T min;
    private T max;

    private RangeFilter() {}

    protected RangeFilter(T min, T max) {
        ObjectUtils.requireNotNull(min,max);

        if (min.compareTo(max) >= 0)
            throw new RuntimeException("min most be smaller than max.");

        this.min = min;
        this.max = max;
    }

    public boolean contain(T x) {
        return Objects.nonNull(x)
                && x.compareTo(min) >= 0
                && x.compareTo(max) <= 0;
    }

    public static RangeFilter<Long> longFilter(long min,long max) {
        return new LongRangeFileter(min,max);
    }

    public static DoubleRangeFileter doubleFilter(double min,double max) {
        return new DoubleRangeFileter(min,max);
    }



    private static class LongRangeFileter extends RangeFilter<Long> {
        public LongRangeFileter(Long min, Long max) {
            super(min, max);
        }
    }

    private static class DoubleRangeFileter extends RangeFilter<Double> {
        public DoubleRangeFileter(Double min, Double max) {
            super(min, max);
        }
    }
}
