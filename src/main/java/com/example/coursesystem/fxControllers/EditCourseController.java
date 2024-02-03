package com.example.coursesystem.fxControllers;

import com.example.coursesystem.appClasses.CourseTree;
import com.example.coursesystem.appClasses.Database;
import com.example.coursesystem.dataStructures.Course;
import com.example.coursesystem.dataStructures.File;
import com.example.coursesystem.dataStructures.Folder;
import com.example.coursesystem.appClasses.Messages;
import com.example.coursesystem.appClasses.TreeDisplayItem;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EditCourseController {
    @FXML
    public TreeTableView twEditedCourse;
    @FXML
    public TreeTableColumn colCourse;
    @FXML
    public TextArea txtDescription;
    @FXML
    public Label lblSelectedCourse;
    @FXML
    public TextField txtCourseName;
    @FXML
    public Label lblSelectedFolder;
    @FXML
    public Label lblSelectedFile;
    @FXML
    public Button btnSaveCourse;
    @FXML
    public TextField txtFolderName;
    @FXML
    public Button btnInsertFolder;
    @FXML
    public Button btnUpdateFolder;
    @FXML
    public Button btnDeleteFolder;
    @FXML
    public Button btnDeleteFile;
    @FXML
    public Button btnUploadFile;
    @FXML
    public TextField txtFileName;
    @FXML
    public TextField txtFileSize;
    @FXML
    public Button btnInsertFile;

    private FileChooser fileChooser = new FileChooser();
    private CourseTree courseTree;
    private Course currentCourse;
    private Folder selectedFolder;
    private File selectedFile;
    private TreeItem<TreeDisplayItem> selectedTreeItem;

    public void initData(Course course) {
        this.currentCourse = course;
        loadEditedCourse();
        initChangeListeners();
        lblSelectedCourse.setText(course.getCourseName());
        txtCourseName.setText(course.getCourseName());
        txtDescription.setText(course.getDescription());
        btnSaveCourse.setDisable(true);
    }

    private void loadEditedCourse() {
        courseTree = new CourseTree(twEditedCourse);
        try {
            List<Course> courseSingle = new ArrayList<>();
            courseSingle.add(currentCourse);
            courseTree.loadTree(courseSingle);
        } catch (SQLException e) {
            Messages.showErrorMessage("Error", "Error in database");
            e.printStackTrace();
        }
        colCourse.setCellValueFactory((Callback<TreeTableColumn.CellDataFeatures, ObservableValue>) param -> ((TreeDisplayItem) param.getValue().getValue()).nameProperty());
        twEditedCourse.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            TreeItem<TreeDisplayItem> selectedItem = (TreeItem<TreeDisplayItem>) newValue;
            selectedTreeItem = selectedItem;
            if (selectedItem != null) {
                if (courseTree.getTreeItemHashtable().get(selectedItem) instanceof Course) {
                    courseSelected();
                } else if (courseTree.getTreeItemHashtable().get(selectedItem) instanceof Folder folder) {
                    folderSelected(folder);
                } else if (courseTree.getTreeItemHashtable().get(selectedItem) instanceof com.example.coursesystem.dataStructures.File file) {
                    fileSelected(file);
                }
            } else {
                resetSelectedFolder();
                resetSelectedFile();
            }
        });
    }

    private void courseSelected() {
        resetSelectedFolder();
        resetSelectedFile();
        btnUploadFile.setDisable(true);
        btnUploadFile.setDisable(true);
        txtFolderName.setDisable(false);
    }

    private void folderSelected(Folder folder) {
        resetSelectedFile();
        selectedFolder = folder;
        txtFolderName.setText(folder.getFolderName());
        txtFolderName.setDisable(false);
        btnDeleteFolder.setDisable(false);
        btnUpdateFolder.setDisable(true);
        btnUploadFile.setDisable(false);
        txtFileName.setDisable(false);
        txtFileSize.setDisable(false);
    }

    private void fileSelected(File file) {
        resetSelectedFolder();
        selectedFile = file;
        txtFileName.setText(file.getFileName());
        txtFileSize.setText(String.valueOf(file.getFileSize()));
        btnDeleteFile.setDisable(false);
        btnUploadFile.setDisable(true);
        btnInsertFile.setDisable(true);
    }

    public void initChangeListeners() {
        txtCourseName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != "") {
                btnSaveCourse.setDisable(false);
            } else {
                btnSaveCourse.setDisable(true);
            }
        });
        txtDescription.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != "") {
                btnSaveCourse.setDisable(false);
            } else {
                btnSaveCourse.setDisable(true);
            }
        });
        txtFolderName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != "") {
                if (selectedFolder != null) {
                    btnUpdateFolder.setDisable(false);
                }
                btnInsertFolder.setDisable(false);
            } else {
                btnUpdateFolder.setDisable(true);
                btnInsertFolder.setDisable(true);
            }
        });
    }

    private void resetSelectedFolder() {
        selectedFolder = null;
        txtFolderName.setText("");
        txtFolderName.setDisable(true);
        btnUpdateFolder.setDisable(true);
        btnDeleteFolder.setDisable(true);
        btnInsertFolder.setDisable(true);
    }

    private void resetSelectedFile() {
        selectedFile = null;
        txtFileName.setText("");
        txtFileSize.setText("");
        txtFileName.setDisable(true);
        txtFileSize.setDisable(true);
        btnDeleteFile.setDisable(true);
        btnInsertFile.setDisable(true);
    }

    public void saveCourseInfo(ActionEvent actionEvent) {
        if (txtCourseName.getText().trim() != null && txtDescription.getText().trim() != null &&
                txtCourseName.getText().trim() != "" && txtDescription.getText().trim() != "" &&
                txtCourseName.getText().trim().length() <= 45 && txtDescription.getText().trim().length() <= 400) {
            try {
                txtCourseName.setText(txtCourseName.getText().trim());
                txtDescription.setText(txtDescription.getText().trim());
                Database.getInstance().updateCourseInfo(currentCourse.getCourse_id(), txtCourseName.getText(), txtDescription.getText());
                currentCourse.setCourseName(txtCourseName.getText());
                currentCourse.setDescription(txtDescription.getText());
                if (!twEditedCourse.getRoot().getChildren().isEmpty()) {
                    ((TreeItem<TreeDisplayItem>) twEditedCourse.getRoot().getChildren().get(0)).getValue().setName(txtCourseName.getText());
                }
                btnSaveCourse.setDisable(true);
            } catch (SQLException e) {
                Messages.showErrorMessage("Error", "Error in database");
                e.printStackTrace();
            }
        } else {
            Messages.showErrorMessage("Error", "Course name or description is empty or too long.");
        }
    }

    public void insertFolder(ActionEvent actionEvent) {
        if (txtFolderName.getText().trim() != null && txtFolderName.getText().trim() != "" && txtFolderName.getText().trim().length() <= 45) {
            try {
                txtFolderName.setText(txtFolderName.getText().trim());
                Folder folder = new Folder();
                folder.setCourse_id(currentCourse.getCourse_id());
                folder.setFolderName(txtFolderName.getText());
                if (selectedFolder != null) {
                    folder.setParent_id(selectedFolder.getFolder_id());
                } else {
                    folder.setParent_id(0); //weirderrorish cannot be null in db
                }
                Database.getInstance().insertFolderGetId(folder);
                TreeItem<TreeDisplayItem> treeItem = new TreeItem<>();
                TreeDisplayItem displayItem = new TreeDisplayItem();
                displayItem.setName(folder.getFolderName());
                displayItem.addSubfolderCount(0);
                displayItem.addFilesize(0);
                treeItem.setValue(displayItem);
                courseTree.getTreeItemHashtable().put(treeItem, folder);
                selectedTreeItem.getChildren().add(treeItem);
                selectedTreeItem.getValue().addSubfolderCount(1);
                selectedTreeItem.setExpanded(true);
            } catch (SQLException e) {
                Messages.showErrorMessage("Error", "Error in database");
                e.printStackTrace();
            }
        } else {
            Messages.showErrorMessage("Error", "Folder name is empty or too long.");
        }
    }

    public void updateFolderInfo(ActionEvent actionEvent) {
        if (txtFolderName.getText().trim() != null && txtFolderName.getText().trim() != "" && txtFolderName.getText().trim().length() <= 45) {
            try {
                txtFolderName.setText(txtFolderName.getText().trim());
                Database.getInstance().updateFolderInfo(selectedFolder.getFolder_id(), txtFolderName.getText());
                selectedTreeItem.getValue().setName(txtFolderName.getText());
                selectedFolder.setFolderName(txtFolderName.getText());
            } catch (SQLException e) {
                Messages.showErrorMessage("Error", "Error in database");
                e.printStackTrace();
            }
        } else {
            Messages.showErrorMessage("Error", "Folder name is empty or too long.");
        }
    }

    public void deleteFolder(ActionEvent actionEvent) {
        if (Messages.showConfirmationDialog("Confirm", "Are you sure you want to delete folder \"" + selectedFolder.getFolderName() + "\" and all its contents?")) {
            try {
                Database.getInstance().deleteFolder(selectedFolder.getFolder_id());
                selectedTreeItem.getParent().getValue().addSubfolderCount(-1);
                selectedTreeItem.getParent().getValue().addFilesize(-selectedTreeItem.getValue().getFilesize());
                courseTree.getTreeItemHashtable().remove(selectedTreeItem);
                selectedTreeItem.getParent().getChildren().remove(selectedTreeItem);
                twEditedCourse.getSelectionModel().select(selectedTreeItem.getParent());
                selectedFolder = null;
            } catch (SQLException e) {
                Messages.showErrorMessage("Error", "Error in database");
                e.printStackTrace();
            }
        }
    }
    public void deleteFile(ActionEvent actionEvent) {
        if (Messages.showConfirmationDialog("Confirm", "Are you sure you want to delete file \"" + selectedFile.getFileName() + "\"?")) {
            try {
                Database.getInstance().deleteFile(selectedFile.getFile_id());
                selectedTreeItem.getParent().getValue().addFilesize(-selectedTreeItem.getValue().getFilesize());
                courseTree.getTreeItemHashtable().remove(selectedTreeItem);
                selectedTreeItem.getParent().getChildren().remove(selectedTreeItem);
                twEditedCourse.getSelectionModel().select(selectedTreeItem.getParent());
                selectedFile = null;
            } catch (SQLException e) {
                Messages.showErrorMessage("Error", "Error in database");
                e.printStackTrace();
            }
        }
    }

    public void uploadFile(ActionEvent actionEvent) {
        java.io.File file = fileChooser.showOpenDialog(twEditedCourse.getScene().getWindow());
        if (file != null) {
            txtFileName.setText(file.getName());
            txtFileSize.setText(String.valueOf(file.length() / 1000));
            btnInsertFile.setDisable(false);
        }
    }

    public void insertFile(ActionEvent actionEvent) {
        if (txtFileName.getText().trim() != null && txtFileName.getText().trim() != "" && txtFileName.getText().trim().length() <= 260) {
            try {
                File file = new File();
                file.setFolder_id(selectedFolder.getFolder_id());
                file.setFileName(txtFileName.getText());
                file.setFileSize(Integer.parseInt(txtFileSize.getText()));
                Database.getInstance().insertFileGetId(file);
                TreeItem<TreeDisplayItem> treeItem = new TreeItem<>();
                TreeDisplayItem displayItem = new TreeDisplayItem();
                displayItem.setName(file.getFileName());
                displayItem.addFilesize(file.getFileSize());
                treeItem.setValue(displayItem);
                courseTree.getTreeItemHashtable().put(treeItem, file);
                selectedTreeItem.getChildren().add(treeItem);
                selectedTreeItem.getValue().addFilesize(file.getFileSize());
                selectedTreeItem.setExpanded(true);
            } catch (SQLException e) {
                Messages.showErrorMessage("Error", "Error in database");
                e.printStackTrace();
            }
        } else {
            Messages.showErrorMessage("Error", "File name is empty or too long.");
        }
    }
}