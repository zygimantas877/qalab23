package com.example.coursesystem.fxControllers;

import com.example.coursesystem.Program;
import com.example.coursesystem.appClasses.*;
import com.example.coursesystem.dataStructures.Course;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ManagedCoursesController implements Initializable {
    @FXML
    public TreeTableView twManagedCourses;
    @FXML
    public TreeTableColumn colCourse;
    @FXML
    public TextField txtEnrolledCount;
    @FXML
    public TextField txtEditRightCount;
    @FXML
    public Button btnEditRights;
    @FXML
    public Button btnRemoveCourse;
    @FXML
    public Button btnEditCourse;

    private CourseTree managedCourses;
    private Course selectedCourse;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadManagedCourses();
    }

    public void loadManagedCourses() {
        managedCourses = new CourseTree(twManagedCourses);
        Array users = null;
        try {
            managedCourses.loadTree(Database.getInstance().getManagedCourses(CurrentUser.getUserId()));
        } catch (SQLException e) {
            Messages.showErrorMessage("Error", "Error in database");
            e.printStackTrace();
        }
        colCourse.setCellValueFactory((Callback<TreeTableColumn.CellDataFeatures, ObservableValue>) param -> ((TreeDisplayItem) param.getValue().getValue()).nameProperty());
        twManagedCourses.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            TreeItem<TreeDisplayItem> selectedItem = (TreeItem<TreeDisplayItem>) newValue;
            if (selectedItem != null) {
                if (managedCourses.getTreeItemHashtable().get(selectedItem) instanceof Course course) {
                    selectedCourse = course;
                    try {
                        txtEnrolledCount.setText(String.valueOf(Database.getInstance().getEnrolledCount(course.getCourse_id())));
                        txtEditRightCount.setText(String.valueOf(Database.getInstance().getEditRightCount(course.getCourse_id())));
                    } catch (SQLException e) {
                        Messages.showErrorMessage("Error", "Error in database");
                        e.printStackTrace();
                    }
                    toggleButtons();
                }
            }
        });
    }
    private void toggleButtons() {
        if (selectedCourse != null) {
            btnEditCourse.setDisable(false);
            try {
                if (Database.getInstance().checkIfUserIsCreator(CurrentUser.getUserId(), selectedCourse.getCourse_id())) {
                    btnEditRights.setDisable(false);
                    btnRemoveCourse.setDisable(false);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            btnEditCourse.setDisable(true);
        }
    }

    public void refreshManagedCourses(ActionEvent actionEvent) {
        managedCourses.clearTree();
        try {
            managedCourses.loadTree(Database.getInstance().getManagedCourses(CurrentUser.getUserId()));
        } catch (SQLException e) {
            Messages.showErrorMessage("Error", "Error in database");
            e.printStackTrace();
        }
        selectedCourse = null;
        btnEditCourse.setDisable(true);
    }

    public void editUserRights(ActionEvent actionEvent) throws IOException {
        if (selectedCourse != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(Program.class.getResource("editUserRights-view.fxml"));
            Parent root = fxmlLoader.load();
            ((EditUserRightsController) fxmlLoader.getController()).initData(selectedCourse);
            Stage stage = new Stage();
            stage.setTitle("Edit user rights");
            stage.setResizable(true);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            refreshManagedCourses(null);
        }
    }

    public void removeCourse(ActionEvent actionEvent) {
        if (selectedCourse != null) {
            if (Messages.showConfirmationDialog("Confirm",
                    "Are you sure you want to delete course \"" + selectedCourse.getCourseName() + "\" and all its contents?")) {
                try {
                    Database.getInstance().deleteCourse(selectedCourse.getCourse_id());
                    refreshManagedCourses(null);
                } catch (SQLException e) {
                    Messages.showErrorMessage("Error", "Error in database");
                    e.printStackTrace();
                }
            }
        }
    }

    public void editCourse(ActionEvent actionEvent) throws IOException {
        if (selectedCourse != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(Program.class.getResource("editCourse-view.fxml"));
            Parent root = fxmlLoader.load();
            ((EditCourseController) fxmlLoader.getController()).initData(selectedCourse);
            Stage stage = new Stage();
            stage.setTitle("Edit course");
            stage.setResizable(true);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            refreshManagedCourses(null);
        }
    }
    public void createCourse(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Program.class.getResource("createCourse-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Create course");
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        refreshManagedCourses(null);
    }
    public void refreshCourses(ActionEvent actionEvent) {
        managedCourses.clearTree();
        try {
            managedCourses.loadTree(Database.getInstance().getManagedCourses(CurrentUser.getUserId()));
        } catch (SQLException e) {
            Messages.showErrorMessage("Error", "Error in database");
            e.printStackTrace();
        }
    }
}
