package ptknow.api.file;

import ptknow.service.file.FileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/v0/files")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileController {

    FileService fileService;

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable UUID id) throws IOException {
        byte[] data = fileService.getFile(id);
        String contentType = fileService.getContentType(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + id + "\"")
                .contentType(MediaType.parseMediaType(contentType != null ? contentType : MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .body(data);
    }
}

