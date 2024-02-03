package com.example.coursesystem.appClasses;

import com.example.coursesystem.dataStructures.Course;
import com.example.coursesystem.dataStructures.File;
import com.example.coursesystem.dataStructures.Folder;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;

public class CourseTree {

    private TreeTableView treeTableView;
    private Hashtable<TreeItem<TreeDisplayItem>, Object> treeItemHashtable = new Hashtable<>();

    public CourseTree(TreeTableView treeTableView) {
        this.treeTableView = treeTableView;
    }

    public Hashtable<TreeItem<TreeDisplayItem>, Object> getTreeItemHashtable() {
        return treeItemHashtable;
    }

    public void loadTree(List<Course> courses) throws SQLException {
        TreeItem<TreeDisplayItem> rootItem = new TreeItem<>();
        rootItem.setValue(new TreeDisplayItem("root", 0, 0));
        treeTableView.setRoot(rootItem);
        treeTableView.setShowRoot(false);

        for (Course course:
                courses) {
            TreeItem<TreeDisplayItem> treeItem = new TreeItem<>();
            TreeDisplayItem displayItem = new TreeDisplayItem();
            displayItem.setName(course.getCourseName());
            treeItem.setValue(displayItem);
            treeItemHashtable.put(treeItem, course);
            rootItem.getChildren().add(treeItem);
            loadFoldersForParent(treeItem, course.getCourse_id(), 0);
        }
    }

    public void clearTree() {
        treeItemHashtable.clear();
        treeTableView.setRoot(null);
    }

    private void loadFoldersForParent(TreeItem<TreeDisplayItem> parentItem, int course_id, int parent_id) throws SQLException {
        for (Folder folder:
                Database.getInstance().getFoldersForParent(course_id, parent_id)) {
            TreeItem<TreeDisplayItem> treeItem = new TreeItem<>();
            TreeDisplayItem displayItem = new TreeDisplayItem();
            displayItem.setName(folder.getFolderName());
            treeItem.setValue(displayItem);
            treeItemHashtable.put(treeItem, folder);
            parentItem.getChildren().add(treeItem);
            loadFilesForFolder(treeItem, folder.getFolder_id());
            loadFoldersForParent(treeItem, course_id, folder.getFolder_id());
            parentItem.getValue().addFilesize(treeItem.getValue().getFilesize());
            parentItem.getValue().addSubfolderCount(treeItem.getValue().getSubfolderCount()+1);
        }
    }

    private void loadFilesForFolder(TreeItem<TreeDisplayItem> parentItem, int folder_id) throws SQLException {
        for (File file:
                Database.getInstance().getFilesForFolder(folder_id)) {
            TreeItem<TreeDisplayItem> treeItem = new TreeItem<>();
            TreeDisplayItem displayItem = new TreeDisplayItem();
            displayItem.setName(file.getFileName());
            displayItem.addFilesize(file.getFileSize());
            parentItem.getValue().addFilesize(file.getFileSize());
            treeItem.setValue(displayItem);
            treeItemHashtable.put(treeItem, file);
            parentItem.getChildren().add(treeItem);
        }
    }
}

