package com.example.coursesystem.fxControllers;

import com.example.coursesystem.appClasses.*;
import com.example.coursesystem.dataStructures.Course;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AllCoursesController implements Initializable {
    @FXML
    public TreeTableColumn colCourse;
    @FXML
    public TreeTableView twAllCourses;
    @FXML
    public TextArea txtDescription;
    @FXML
    public Label lblSelectedCourse;
    //@FXML
    //public TextField test;
    @FXML
    public TextField txtUsername;
    @FXML
    public TextField txtFolderCount;
    @FXML
    public TextField txtCourseSize;
    @FXML
    public TextField txtCourseLenght;

    private CourseTree allCourses;
    private Course selectedCourse;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadAllCoursesTab();
    }

    private void loadAllCoursesTab() {
        allCourses = new CourseTree(twAllCourses);
        try {
            allCourses.loadTree(Database.getInstance().getAllCourses());
        } catch (SQLException e) {
            Messages.showErrorMessage("Error", "Error in database");
            e.printStackTrace();
        }
        colCourse.setCellValueFactory((Callback<TreeTableColumn.CellDataFeatures, ObservableValue>) param -> ((TreeDisplayItem) param.getValue().getValue()).nameProperty());
        twAllCourses.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            TreeItem<TreeDisplayItem> selectedItem = (TreeItem<TreeDisplayItem>) newValue;
            if (selectedItem != null) {
                if (allCourses.getTreeItemHashtable().get(selectedItem) instanceof Course course) {
                    selectedCourse = course;
                    lblSelectedCourse.setText(course.getCourseName());
                    txtDescription.setText(course.getDescription());
                    txtUsername.setText(course.getUsername());
                    txtFolderCount.setText(String.valueOf(selectedItem.getValue().getSubfolderCount()));
                    txtCourseSize.setText(String.valueOf(selectedItem.getValue().getFilesize()));
                    txtCourseLenght.setText((course.getCourseLength()));
                }
            }
        });
        // test.setText(String.valueOf(CurrentUser.getUserId()));
    }

    public void enrollInCourse(ActionEvent actionEvent) {
        if (selectedCourse != null) {
            try {
                if (!Database.getInstance().checkIfUserIsInCourse(CurrentUser.getUserId(), selectedCourse.getCourse_id())) {
                    Database.getInstance().linkUserAndCourse(CurrentUser.getUserId(), selectedCourse.getCourse_id());
                    Messages.showInfoMessage("Success", "Successfully enrolled in course: " + selectedCourse.getCourseName());
                } else {
                    Messages.showErrorMessage("Error", "You are already enrolled in this course.");
                }
            } catch (SQLException e) {
                Messages.showErrorMessage("Error", "Error in database");
                e.printStackTrace();
            }
        }
    }

    public void refreshAllCourses(ActionEvent actionEvent) {
        allCourses.clearTree();
        try {
            allCourses.loadTree(Database.getInstance().getAllCourses());
        } catch (SQLException e) {
            Messages.showErrorMessage("Error", "Error in database");
            e.printStackTrace();
        }
    }
}

