package ptknow.model.file.attachment;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ptknow.model.auth.Auth;
import ptknow.model.file.File;
import ptknow.model.file.attachment.resource.Purpose;
import ptknow.model.file.attachment.resource.ResourceType;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "file_attachments",
    uniqueConstraints = @UniqueConstraint(columnNames = {"resource_type", "purpose", "file_id", "resource_id"}))
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_attachment_id_generator")
    @SequenceGenerator(name = "file_attachment_id_generator", sequenceName = "file_attachment_sequence", allocationSize = 1)
    @EqualsAndHashCode.Include
    Long id;

    @Column(nullable = false, updatable = false)
    String resourceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    ResourceType resourceType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    Purpose purpose;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    @Setter
    FileVisibility fileVisibility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false, updatable = false)
    File file;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false, updatable = false)
    Auth owner;
}
