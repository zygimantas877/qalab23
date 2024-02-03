package com.example.coursesystem.appClasses;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class UserDisplayItem {
    private int userId;
    private final SimpleStringProperty name;
    private final SimpleStringProperty surname;
    private final SimpleStringProperty companyName;
    private final SimpleBooleanProperty hasRights;

    public UserDisplayItem() {
        this.name = new SimpleStringProperty();
        this.surname = new SimpleStringProperty();
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

    public void setName(String name) {
        this.name.set(name);
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public void setCompanyName(String companyName) {
        this.companyName.set(companyName);
    }

    public boolean hasRights() {
        return hasRights.get();
    }

    public void setHasRights(int hasRights) {
        this.hasRights.set(hasRights > 0);
    }
}
