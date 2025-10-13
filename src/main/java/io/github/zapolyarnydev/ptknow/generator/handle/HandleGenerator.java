package io.github.zapolyarnydev.ptknow.generator.handle;

import io.github.zapolyarnydev.ptknow.generator.UniqueGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Component
public class HandleGenerator implements UniqueGenerator<String> {

    private final Supplier<String> handleGenerationProvider;

    @Override
    public String generate(Function<String, Boolean> existChecking) {
        while (true) {
            String generated = handleGenerationProvider.get();
            if(!existChecking.apply(generated))
                return generated;
        }
    }
}
