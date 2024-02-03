package com.example.coursesystem.fxControllers;

import com.example.coursesystem.appClasses.Database;
import com.example.coursesystem.dataStructures.User;
import com.example.coursesystem.appClasses.Messages;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
    @FXML
    public TextField txtUsername;
    @FXML
    public PasswordField txtPassword;
    @FXML
    public PasswordField txtConfirmPassword;
    @FXML
    public TextField txtName;
    @FXML
    public TextField txtLastName;
    @FXML
    public TextField txtEmail;
    @FXML
    public TextField txtCompanyName;
    @FXML
    public RadioButton selPersonal;
    @FXML
    public RadioButton selCompany;
    @FXML
    public ToggleGroup toggleGroup;

    public RegisterController() {
    }
    public void initialize(URL location, ResourceBundle resources) {
        selPersonal.setToggleGroup(toggleGroup);
        selCompany.setToggleGroup(toggleGroup);
        toggleGroup.selectedToggleProperty().addListener(this::radioButtonChanged);
        txtName.setPromptText("Name");
        txtLastName.setPromptText("Last name");
        txtCompanyName.setVisible(false);
        txtUsername.setFocusTraversable(false);
        txtPassword.setFocusTraversable(false);
        txtConfirmPassword.setFocusTraversable(false);
        txtName.setFocusTraversable(false);
        txtLastName.setFocusTraversable(false);
        txtEmail.setFocusTraversable(false);

    }
    private void radioButtonChanged(ObservableValue<? extends Toggle> ov, Toggle oldToggle, Toggle newToggle) {
        if (toggleGroup.getSelectedToggle() == selPersonal) {
             txtCompanyName.setVisible(false);
        } else if (toggleGroup.getSelectedToggle() == selCompany) {
            txtCompanyName.setVisible(true);
        }
    }
    private void createUser(){
        User user = new User();
        if(user != null){
            user.setLogin(txtUsername.getText().trim());
            user.setUserName(txtName.getText().trim());
            user.setLastName(txtLastName.getText().trim());
            user.setEmail(txtEmail.getText().trim());
            user.setCompanyName(txtCompanyName.getText().trim());

            if(toggleGroup.getSelectedToggle() == selPersonal) user.setUserType(0);
            else user.setUserType(1);

            try {
                Database.getInstance().createUser(user, txtPassword.getText());
                Messages.showInfoMessage("Success", "User created successfully.");
                Stage stage = (Stage) txtUsername.getScene().getWindow();
                stage.close();
            } catch (SQLException e) {
                Messages.showErrorMessage("Error", "Error in database");
                e.printStackTrace();
            }
        }
    }

    public void validateAndSignup(ActionEvent actionEvent) {
        if (isInputValid()) {
            try {
                if(Database.getInstance().userExists(txtUsername.getText().trim())){
                    Messages.showErrorMessage("Bad username input","User already exists!");
                } else {
                    createUser();
                }
            } catch (SQLException e) {
                Messages.showErrorMessage("Error", "Error in database");
                e.printStackTrace();
            }
        }
    }

    private boolean isInputValid() {
        boolean result = true;
        if (txtUsername.getText().isEmpty() || txtUsername.getText().trim().length() > 50) {
            Messages.showErrorMessage("Bad username input","Username is empty!");
            txtUsername.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222");
            result = false;
        } else if (txtPassword.getText().isEmpty() || txtPassword.getText().trim().length() > 40) {
            Messages.showErrorMessage("Password missmatch","Password is empty!");
            txtPassword.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222");
            result = false;
        } else if (txtPassword.getText().contains(" ")) {
            Messages.showErrorMessage("Password missmatch","Password cannot contain spaces!");
            txtUsername.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222");
            result = false;
        } /*else if (txtPassword.getText().trim() != txtConfirmPassword.getText().trim()){      -- No solution found :(
            Messages.showErrorMessage("Password missmatch","Passwords do not match!");
            txtPassword.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222");
            txtConfirmPassword.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222");
            result = false;
        }*/
        else if (txtName.getText().isEmpty() || txtName.getText().trim().length() > 20) {
            Messages.showErrorMessage("Bad name input","Name is empty!");
            txtName.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222");
            result = false;
        }
        else if (txtLastName.getText().isEmpty() || txtLastName.getText().trim().length() > 30) {
            Messages.showErrorMessage("Bad last name input","Last name is empty!");
            txtLastName.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222");
            result = false;
        }
        else if (txtEmail.getText().isEmpty() || txtEmail.getText().trim().length() > 40) {
            Messages.showErrorMessage("Bad email input","Email is empty!");
            txtEmail.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222");
            result = false;
        }
        else if (toggleGroup.getSelectedToggle() == selCompany &&
                (txtCompanyName.getText().isEmpty() || txtCompanyName.getText().trim().length() > 30)) {
            Messages.showErrorMessage("Bad company name input","Company name is empty!");
            txtCompanyName.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222");
            result = false;
        }
        return result;
    }
}