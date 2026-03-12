package ptknow.exception.file;

import ptknow.model.file.attachment.resource.ResourceType;

import java.util.UUID;

public class FileAttachmentNotFoundException extends RuntimeException {
    public FileAttachmentNotFoundException(Long id) {
        super("File attachment with id " + id + " not found");
    }

    public FileAttachmentNotFoundException(UUID fileId) {
        super("File attachment for file id " + fileId + " not found");
    }

    public FileAttachmentNotFoundException(ResourceType type, String resourceId) {
        super("File attachment for " + type.name() + " with id " + resourceId + " not found");
    }
}
