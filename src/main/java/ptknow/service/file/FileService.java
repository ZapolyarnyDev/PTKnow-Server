package ptknow.service.file;

import ptknow.model.file.File;
import ptknow.exception.file.FileNotFoundException;
import ptknow.repository.file.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    private final Path root = Paths.get("uploads");

    public File saveFile(MultipartFile file) throws IOException {
        if (!Files.exists(root)) {
            Files.createDirectories(root);
        }

        UUID fileId = UUID.randomUUID();
        Path filePath = root.resolve(fileId.toString());
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        File entity = File.builder()
                .id(fileId)
                .originalFilename(file.getOriginalFilename())
                .contentType(file.getContentType())
                .storagePath(filePath.toString())
                .uploadedAt(Instant.now())
                .build();

        return fileRepository.save(entity);
    }

    public byte[] getFile(UUID id) throws IOException {
        File fileEntity = fileRepository.findById(id)
                .orElseThrow(() -> new FileNotFoundException("File not found"));

        Path path = Paths.get(fileEntity.getStoragePath());
        return Files.readAllBytes(path);
    }

    public void deleteFile(UUID id) throws IOException {
        File fileEntity = fileRepository.findById(id)
                .orElseThrow(() -> new FileNotFoundException("File not found"));

        Path path = Paths.get(fileEntity.getStoragePath());
        if (Files.exists(path)) {
            Files.delete(path);
        }

        fileRepository.delete(fileEntity);
    }

    public String getContentType(UUID id) {
        File fileEntity = fileRepository.findById(id)
                .orElseThrow(() -> new FileNotFoundException("File not found"));
        return fileEntity.getContentType();
    }
}

