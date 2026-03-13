package ptknow.service.file;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ptknow.exception.file.FileAccessDeniedException;
import ptknow.model.auth.Auth;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileWriteService {

    FileAccessService fileAccessService;
    FileAttachmentService fileAttachmentService;
    FileService fileService;

    @Transactional(rollbackFor = Exception.class)
    public void deleteOwnedFile(UUID fileId, Auth initiator) throws IOException {
        if (!fileAccessService.canDelete(fileId, initiator))
            throw new FileAccessDeniedException("You don't have permissions to delete this file");

        deleteLinkedFile(fileId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteLinkedFile(UUID fileId) throws IOException {
        fileAttachmentService.deleteAllByFileId(fileId);
        fileService.deleteFile(fileId);
    }
}
