module com.example.coursesystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires spring.context;
    requires spring.web;
    requires mysql.connector.java;
    requires spring.core;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires commons.dbcp;
    requires com.google.gson;

    opens com.example.coursesystem to javafx.fxml;
    exports com.example.coursesystem;
    exports com.example.coursesystem.fxControllers;
    opens com.example.coursesystem.fxControllers to javafx.fxml;
    exports com.example.coursesystem.dataStructures;
    opens com.example.coursesystem.dataStructures to javafx.fxml;
    exports com.example.coursesystem.appClasses;
    opens com.example.coursesystem.appClasses to javafx.fxml;
}