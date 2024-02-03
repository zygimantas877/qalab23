package com.example.coursesystem.appClasses;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class UserDisplayItem {
    private int userId;
    private SimpleStringProperty name;
    private SimpleStringProperty surname;
    private SimpleStringProperty email;
    private SimpleStringProperty companyName;
    private SimpleBooleanProperty hasRights;

    public UserDisplayItem() {
        this.name = new SimpleStringProperty();
        this.surname = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.companyName  = new SimpleStringProperty();
        this.hasRights = new SimpleBooleanProperty();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getSurname() {
        return surname.get();
    }

    public SimpleStringProperty surnameProperty() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getCompanyName() {
        return companyName.get();
    }

    public SimpleStringProperty companyNameProperty() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName.set(companyName);
    }

    public boolean hasRights() {
        return hasRights.get();
    }

    public SimpleBooleanProperty hasRightsProperty() {
        return hasRights;
    }

    public void setHasRights(int hasRights) {
        if(hasRights > 0) this.hasRights.set(true);
        else this.hasRights.set(false);
    }
}
