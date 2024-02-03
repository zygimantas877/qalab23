package com.example.coursesystem.fxControllers;

import com.example.coursesystem.appClasses.CurrentUser;
import com.example.coursesystem.appClasses.Database;
import com.example.coursesystem.dataStructures.Course;
import com.example.coursesystem.appClasses.Messages;
import com.example.coursesystem.appClasses.UserDisplayItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class EditUserRightsController {
    @FXML
    public TableView tblUserRights;
    @FXML
    public Label lblCourseName;
    @FXML
    public TextField txtName;
    @FXML
    public TextField txtSurname;
    @FXML
    public TextField txtEmail;
    @FXML
    public TextField txtCompanyName;
    @FXML
    public TableColumn colName;
    @FXML
    public TableColumn colSurname;
    @FXML
    public TableColumn colCompanyName;
    @FXML
    public TableColumn colHasRights;
    @FXML
    public Button btnRevokeRights;
    @FXML
    public Button btnGrantRights;

    private Course currentCourse;
    private ObservableList<UserDisplayItem> usersObservable = FXCollections.observableArrayList();
    private UserDisplayItem selectedUser;

    public void initData(Course course) {
        this.currentCourse = course;
        lblCourseName.setText(course.getCourseName());
        colName.setCellValueFactory(new PropertyValueFactory<UserDisplayItem, String>("name"));
        colSurname.setCellValueFactory(new PropertyValueFactory<UserDisplayItem, String>("surname"));
        colCompanyName.setCellValueFactory(new PropertyValueFactory<UserDisplayItem, String>("companyName"));
        colHasRights.setCellValueFactory(new PropertyValueFactory<UserDisplayItem, Boolean>("hasRights"));
        tblUserRights.setItems(usersObservable);
        tblUserRights.getItems().clear();
        try {
            usersObservable.addAll(Database.getInstance().getUserPrivileges(currentCourse.getCourse_id(), CurrentUser.getUserId()));
            btnGrantRights.setDisable(true);
            btnRevokeRights.setDisable(true);
        } catch (SQLException e) {
            Messages.showErrorMessage("Error", "Error in database");
            e.printStackTrace();
        }
        tblUserRights.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedUser = (UserDisplayItem) newValue;
            if(selectedUser != null && selectedUser.hasRights()) {
                btnRevokeRights.setDisable(false);
                btnGrantRights.setDisable(true);
            } else {
                btnRevokeRights.setDisable(true);
                btnGrantRights.setDisable(false);
            }
        });
    }
    public void revokeRights(ActionEvent actionEvent) {
        if(selectedUser != null) {
            try {
                Database.getInstance().deleteUserPrivileges(selectedUser.getUserId(), currentCourse.getCourse_id());
                selectedUser.setHasRights(0);
                btnRevokeRights.setDisable(true);
                btnGrantRights.setDisable(false);
            } catch (SQLException e) {
                Messages.showErrorMessage("Error", "Error in database");
                e.printStackTrace();
            }
        }
    }

    public void grantRights(ActionEvent actionEvent) {
        if(selectedUser != null) {
            try {
                Database.getInstance().setUserPrivileges(selectedUser.getUserId(), currentCourse.getCourse_id());
                selectedUser.setHasRights(1);
                btnRevokeRights.setDisable(false);
                btnGrantRights.setDisable(true);
            } catch (SQLException e) {
                Messages.showErrorMessage("Error", "Error in database");
                e.printStackTrace();
            }
        }
    }
}
