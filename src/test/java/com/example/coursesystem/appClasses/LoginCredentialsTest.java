package com.example.coursesystem.appClasses;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginCredentialsTest {

    private Database database;
    @BeforeEach
    void setUp() {
        database = Database.getDatabaseInstance();
    }
    @Test
    void checkLoginInfo() {

        String[] set1 = {"realname", "realpass"};
        String[] set2 = {"realname", "fakepass"};
        String[] set3 = {"fakename", "realpass"};
        String[] set4 = {"fakename", "fakepass"};

        assertDoesNotThrow(() -> assertTrue(database.checkLoginInfo(set1[0], set1[1])));
        assertDoesNotThrow(() -> assertFalse(database.checkLoginInfo(set2[0], set2[1])));
        assertDoesNotThrow(() -> assertFalse(database.checkLoginInfo(set3[0], set3[1])));
        assertDoesNotThrow(() -> assertFalse(database.checkLoginInfo(set4[0], set4[1])));
    }
}