package com.webste.anikethta.dao;

import com.webste.anikethta.model.File;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FileDaoInterface{
    int insertFile(UUID id, File file, byte[] multipartFile, String description);
    int deleteFileById(UUID uuid);
    List<File> selectAllFiles();
    Optional<File> selectFileByID(UUID uuid);

    default int insertFile(File file) {
        UUID id = UUID.randomUUID();
        return insertFile(id, file, file.getFileData(), file.getDescription());
    }
}