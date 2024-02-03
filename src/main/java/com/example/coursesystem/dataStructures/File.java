package com.example.coursesystem.dataStructures;

public class File {
    private int file_id;
    private int folder_id;
    private String name;
    private int size;

    public int getFile_id() {
        return file_id;
    }

    public void setFile_id(int file_id) {
        this.file_id = file_id;
    }

    public int getFolder_id() {
        return folder_id;
    }

    public void setFolder_id(int folder_id) {
        this.folder_id = folder_id;
    }

    public String getFileName() {
        return name;
    }

    public void setFileName(String name) {
        this.name = name;
    }

    public int getFileSize() {
        return size;
    }

    public void setFileSize(int size) {
        this.size = size;
    }
}