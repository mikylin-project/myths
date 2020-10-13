package cn.mikylin.myths.common.bean;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * simple optional bean utils.
 *
 * @author mikylin
 * @date 20201013
 */
public final class OptionalBean<T> {

    /**
     * static empty object
     */
    private final static OptionalBean empty = new OptionalBean();

    /**
     * value
     */
    private final T value;


    private final boolean isEmpty;

    /**
     * get value
     */
    public T get() {
        return value;
    }

    private OptionalBean() {
        this(null);
    }

    private OptionalBean(T t) {
        value = t;
        if(t == null)
            isEmpty = true;
        else
            isEmpty = false;
    }

    /**
     * get a optional bean require not null.
     */
    public static <T> OptionalBean<T> of(T t) {
        Objects.requireNonNull(t);
        return new OptionalBean<>(t);
    }

    /**
     * get a optional bean can be null.
     */
    public static <T> OptionalBean<T> ofNullable(T t) {
        return Objects.isNull(t) ? empty : new OptionalBean<>(t);
    }

    /**
     * get a optional bean with it's param and require not null.
     */
    public <R> OptionalBean<R> get(Function<? super T,? extends R> fn) {
        return of(fn.apply(value));
    }

    /**
     * get a optional bean with it's param and can be null.
     */
    public <R> OptionalBean<R> getNullable(Function<? super T,? extends R> fn) {
        return ofNullable(fn.apply(value));
    }

    public T orElse(T other) {
        return isEmpty ? other : value;
    }

    public T orElseGet(Supplier<T> other) {
        return isEmpty ? other.get() : value;
    }

    public T orElseThrow(Throwable t) throws Throwable {
        if(isEmpty)
            throw t;
        return value;
    }

    public boolean isPresent() {
        return !isEmpty;
    }


}
