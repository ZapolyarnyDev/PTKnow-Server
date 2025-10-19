package io.github.zapolyarnydev.ptknow.repository.file;

import io.github.zapolyarnydev.ptknow.entity.file.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, UUID> {
}
