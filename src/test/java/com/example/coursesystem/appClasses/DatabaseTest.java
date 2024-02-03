package com.example.coursesystem.appClasses;

import com.example.coursesystem.dataStructures.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {
    private Database database;
    @BeforeEach
    void setUp() {
        database = Database.getDatabaseInstance();
    }
    @Test
    void testGetAllUsersWithNewUser() throws SQLException {
        int initialUserCount = getUsersCountFromDatabase();
        User newUser = new User();
        newUser.setUserName("NewUser");
        newUser.setLastName("LastName");
        newUser.setEmail("newuser@example.com");
        newUser.setCompanyName("New Company");
        newUser.setUserType(1);
        assertDoesNotThrow(() -> database.createUser(newUser, "password"));
        List<User> updatedUsers = assertDoesNotThrow(() -> database.getAllUsers());
        int finalUserCount = getUsersCountFromDatabase();
        assertEquals(initialUserCount + 1, finalUserCount, "Number of users should increase by 1");
        assertTrue(updatedUsers.contains(newUser), "New user should be in the updated user list");
    }
    private int getUsersCountFromDatabase() throws SQLException {
        List<User> users = database.getAllUsers();
        return users.size();
    }
}