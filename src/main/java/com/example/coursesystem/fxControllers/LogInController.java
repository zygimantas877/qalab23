package com.example.coursesystem.fxControllers;

import com.example.coursesystem.appClasses.CurrentUser;
import com.example.coursesystem.appClasses.Database;
import com.example.coursesystem.appClasses.Messages;
import com.example.coursesystem.Program;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LogInController implements Initializable {
    @FXML
    public PasswordField txtPassword;
    @FXML
    public TextField txtName;
    @FXML
    public AnchorPane AnPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtName.setFocusTraversable(false);
        txtPassword.setFocusTraversable(false);
    }

    private boolean infoValidation() {
        boolean result = true;
        if (this.txtName.getText().isEmpty()) {
            txtName.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222");
            result = false;
        }
        if (this.txtPassword.getText().isEmpty()) {
            txtPassword.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222");
            result = false;
        }
        if (result == true) {
            try {
                result = Database.getInstance().checkLoginInfo(txtName.getText().trim(), txtPassword.getText());
                if (result == false) {
                    Messages.showErrorMessage("Failed to login", "Incorrect login information.");
                }
            } catch (SQLException e) {
                Messages.showErrorMessage("Error", "Error in database");
                e.printStackTrace();
            }
        }
        return result;
    }
    @FXML
    public void validationForMainForm(ActionEvent actionEvent) throws IOException{
        if (this.infoValidation()) {
            try {
                int userId = Database.getInstance().getUserId(txtName.getText().trim(), txtPassword.getText());
                if (userId > 0) {
                    CurrentUser.setUserId(userId);
                    openMainForm();
                }
            } catch (SQLException e) {
                Messages.showErrorMessage("Error", "Error in database");
                e.printStackTrace();
            }
        }
    }
    private void openMainForm() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Program.class.getResource("main-view.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        ((MainController)fxmlLoader.getController()).initData();
        Stage stage = new Stage();
        stage.setTitle("Course System");
        stage.setResizable(true);
        stage.setScene(new Scene(root));
        ((Stage)this.txtName.getScene().getWindow()).close();
        stage.show();
    }
    @FXML
    public void openRegisterForm(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Program.class.getResource("register-view.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Sign up for Course System");
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        ((Node)actionEvent.getSource()).getScene().getWindow().hide();
        stage.showAndWait();
        ((Stage)((Node)actionEvent.getSource()).getScene().getWindow()).show();
    }

}