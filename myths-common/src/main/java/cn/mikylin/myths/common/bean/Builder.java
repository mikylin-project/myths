package cn.mikylin.myths.common.bean;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * java bean common builder.
 *
 * @author mikylin
 * @date 20200314
 */
public final class Builder<T> {

    private final T value;

    private Builder(Supplier<T> instantiator) {
        value = instantiator.get();
    }

    public static <T> Builder<T> of(Supplier<T> instantiator) {
        return new Builder<>(instantiator);
    }

    public <P> Builder<T> with(BeanFuncInvoke<T,P> consumer, P p) {
        consumer.accept(value,p);
        return this;
    }

    public <P> Builder<T> withNotNull(BeanFuncInvoke<T,P> consumer,P p) {
        consumer.accept(value,Optional.of(p).get());
        return this;
    }

    public T buid() {
        return value;
    }

}

