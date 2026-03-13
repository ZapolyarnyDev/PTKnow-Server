package ptknow.api.file;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import ptknow.exception.file.FileAccessDeniedException;
import ptknow.model.auth.Auth;
import ptknow.service.file.FileAccessService;
import ptknow.service.file.FileService;
import ptknow.service.file.FileWriteService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    FileAccessService accessService;
    FileWriteService fileWriteService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('GUEST', 'STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<byte[]> getFile(
            @PathVariable UUID id,
            @AuthenticationPrincipal Auth user
            ) throws IOException {
        if(!accessService.canRead(id, user))
            throw new FileAccessDeniedException("You don't have permissions to view this file");

        byte[] data = fileService.getFile(id);
        String contentType = fileService.getContentType(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + id + "\"")
                .contentType(MediaType.parseMediaType(contentType != null ? contentType : MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .body(data);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('GUEST', 'STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<Void> deleteFile(
            @PathVariable UUID id,
            @AuthenticationPrincipal Auth user
    ) throws IOException {
        fileWriteService.deleteOwnedFile(id, user);
        return ResponseEntity.noContent().build();
    }
}

