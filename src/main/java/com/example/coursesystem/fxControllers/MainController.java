package com.example.coursesystem.fxControllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    public TabPane tpMain;
    @FXML
    public Tab tabCourses;
    @FXML
    public Tab tabEnrolledCourses;
    @FXML
    public Tab tabManagedCourses;

    public MainController() {
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void initData() {
    }
}
