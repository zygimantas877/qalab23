package com.example.coursesystem.appClasses;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class TreeDisplayItem {
    private SimpleStringProperty name;
    private SimpleIntegerProperty subfolderCount;
    private SimpleLongProperty filesize;

    public TreeDisplayItem() {
        this.name = new SimpleStringProperty();
        this.subfolderCount = new SimpleIntegerProperty();
        this.filesize = new SimpleLongProperty();
    }

    public TreeDisplayItem(String name, int subfolders, long filesize) {
        this.name = new SimpleStringProperty(name);
        this.subfolderCount = new SimpleIntegerProperty(subfolders);
        this.filesize = new SimpleLongProperty(filesize);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getSubfolderCount() {
        return subfolderCount.get();
    }

    public SimpleIntegerProperty subfolderCountProperty() {
        return subfolderCount;
    }

    public void addSubfolderCount(int subfolders) {
        this.subfolderCount.set(this.subfolderCount.get() + subfolders);
    }

    public long getFilesize() {
        return filesize.get();
    }

    public SimpleLongProperty filesizeProperty() {
        return filesize;
    }

    public void addFilesize(long filesize) {
        this.filesize.set(this.filesize.get() + filesize);
    }
}
