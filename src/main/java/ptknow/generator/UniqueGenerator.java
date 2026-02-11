package ptknow.generator;

import java.util.function.Function;

@FunctionalInterface
public interface UniqueGenerator<T> {

    T generate(Function<T, Boolean> existChecking);
}
