package ptknow.config;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import ptknow.generator.GenerationConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Supplier;

@Configuration
public class GeneratorConfig {

    @Bean
    public Supplier<String> handleGenerationProvider() {
        char[] generationSymbols = GenerationConstants.NUMBERS_ALPHABET.toCharArray();
        return () -> NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, generationSymbols, 8);
    }
}

