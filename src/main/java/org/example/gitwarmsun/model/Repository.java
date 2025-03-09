package org.example.gitwarmsun.model;

import java.io.File;
import java.security.PrivateKey;

public class Repository {
    private int id;
    private String name;
    private String description;
    private int ownerId;
    private String path;
    File[] files;
    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public File[] getFiles() {
        return files;
    }
    public void setFiles(File[] files) {
        this.files = files;
    }
}