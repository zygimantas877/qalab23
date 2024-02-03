package com.example.coursesystem.fxControllers;

import com.example.coursesystem.appClasses.CurrentUser;
import com.example.coursesystem.appClasses.Database;
import com.example.coursesystem.appClasses.Messages;
import com.example.coursesystem.dataStructures.Course;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.SQLException;

public class CreateCourseController {
    @FXML
    public TextField txtName;
    @FXML
    public TextArea txtDescription;
    @FXML
    public Label lblName;
    @FXML
    public TextField txtCourseLength;

    private final int nameMaxLenght = 45;

    public void createCourse(ActionEvent actionEvent) {
        if(txtName.getText().trim() != "" && txtName.getText().trim().length() <= nameMaxLenght) {
            try {
                if(!Database.getInstance().checkIfCourseExists(txtName.getText().trim())) {
                    Course course = new Course();
                    course.setCreator_id(CurrentUser.getUserId());
                    course.setCourseName(txtName.getText().trim());
                    course.setDescription(txtDescription.getText().trim());
                    course.setCourseLength(txtCourseLength.getText().trim());
                    Database.getInstance().insertCourse(course);
                    Messages.showInfoMessage("Success", "Course \"" + course.getCourseName() + "\" created successfully.");
                    Stage stage = (Stage) txtName.getScene().getWindow();
                    stage.close();
                } else {
                    lblName.setText("Name - course already exists");
                    lblName.setTextFill(Color.color(1, 0, 0));
                }
            } catch (SQLException e) {
                Messages.showErrorMessage("Error", "Error in database");
                e.printStackTrace();
            }
        } else {
            lblName.setText("Name - mandatory, max length " + nameMaxLenght);
            lblName.setTextFill(Color.color(1, 0, 0));
        }
    }
}
