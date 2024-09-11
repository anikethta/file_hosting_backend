package com.webste.anikethta.service;

import com.webste.anikethta.dao.FileDaoInterface;
import com.webste.anikethta.model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileService {

    private final FileDaoInterface fileDao;

    @Autowired
    public FileService(@Qualifier("postgres") FileDaoInterface fileDao) {
        this.fileDao = fileDao;
    }

    public int addFile(File file) {
        return fileDao.insertFile(file);
    }

    public List<File> getAllFiles() {
        return fileDao.selectAllFiles();
    }

    public Optional<File> getFileById(UUID uuid) {
        return fileDao.selectFileByID(uuid);
    }

    public int deleteFile(UUID uuid) {
        return fileDao.deleteFileById(uuid);
    }
}
