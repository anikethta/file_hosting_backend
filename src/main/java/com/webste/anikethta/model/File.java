package com.webste.anikethta.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "website_files")
public class File {
    @Id
    private final UUID uuid;
    private final String fname;
    private final String description;
    private final byte[] multipartFile;
    private final float file_size;
    private final boolean visibility;

    public File(UUID uuid, String fname, String description, byte[] multipartFile, Optional<Boolean> visibility) {
        this.uuid = uuid;
        this.fname = fname;
        this.multipartFile = multipartFile;
        this.description = description;
        this.file_size = (multipartFile.length / 1024f) / 1024f;

        if (visibility.isPresent()) {
            this.visibility = visibility.get();
        } else {
            this.visibility = false; // Default -- file is not visible
        }
    }

    public UUID getUuid() {
        return uuid;
    }
    public String getFname() {
        return fname;
    }
    public String getDescription() { return description; }
    public byte[] getFileData() { return multipartFile; }
    public float getFile_size() { return file_size; }
    public boolean getVisibility() { return visibility; }
}