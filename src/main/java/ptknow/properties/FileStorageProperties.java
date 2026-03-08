package ptknow.properties;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.file")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileStorageProperties {

    @NotBlank
    String uploadDir;
}
