package ptknow.repository.file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptknow.model.file.attachment.FileAttachment;
import ptknow.model.file.attachment.resource.Purpose;
import ptknow.model.file.attachment.resource.ResourceType;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface FileAttachmentRepository extends JpaRepository<FileAttachment, Long> {
    Set<FileAttachment> findAllByResourceTypeAndResource_Id(ResourceType resourceType, String resourceId);

    Set<FileAttachment> findAllByFile_IdAndResourceTypeAndResource_IdAndPurpose(
            UUID fileId,
            ResourceType resourceType,
            String resourceId,
            Purpose purpose
    );

    Set<FileAttachment> findAllByOwner_Id(UUID ownerId);

    boolean existsByIdAndOwner_Id(Long id, UUID ownerId);

    void deleteByFile_Id(UUID fileId);
}
