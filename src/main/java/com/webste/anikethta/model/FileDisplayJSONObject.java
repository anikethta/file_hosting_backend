package com.webste.anikethta.model;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.UUID;

public class FileDisplayJSONObject {
    private final String fname;
    private final Info info;

    @JsonCreator
    public FileDisplayJSONObject(String fname, Info info) {
        this.fname = fname;
        this.info = info;
    }

    public String getFname() {
        return fname;
    }

    public Info getInfo() {
        return info;
    }

    public static class Info {
        private final UUID uuid;
        private final String description;
        private final float file_size;

        @JsonCreator
        public Info(UUID uuid, String description, float file_size) {
            this.uuid = uuid;
            this.description = description;
            this.file_size = file_size;
        }

        public UUID getUUID() {
            return uuid;
        }

        public String getDescription() {
            return description;
        }

        public float getFile_size() {
            return file_size;
        }
    }
}
