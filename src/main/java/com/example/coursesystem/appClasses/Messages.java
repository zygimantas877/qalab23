package com.example.coursesystem.appClasses;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public final class Messages {

        private Messages() {}
        public static void showErrorMessage(String header, String message) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(header);
            alert.setContentText(message);
            alert.showAndWait();
        }
        public static void showInfoMessage(String header, String message) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setHeaderText(header);
            alert.setContentText(message);
            alert.showAndWait();
        }
        public static boolean showConfirmationDialog(String header, String message) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm");
            alert.setHeaderText(header);
            alert.setContentText(message);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                return true;
            } else {
                return false;
            }
        }
}
