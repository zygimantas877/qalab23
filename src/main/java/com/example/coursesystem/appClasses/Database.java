package com.example.coursesystem.appClasses;

import com.example.coursesystem.dataStructures.*;
import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private static Database databaseInstance;

    public Database() {
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/coursesystem");
        dataSource.setUsername("root");
        dataSource.setMinIdle(5);
        dataSource.setMaxIdle(10);
        dataSource.setMaxOpenPreparedStatements(100);
    }

    public static Database getDatabaseInstance() {
        if (databaseInstance == null) {
            databaseInstance = new Database();
        }
        return databaseInstance;
    }
    private final BasicDataSource dataSource = new BasicDataSource();

    public void closeConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    public boolean checkLoginInfo(String username, String password) throws SQLException {
        Connection connection = dataSource.getConnection();
        try {
            ResultSet resultSet;
            try (PreparedStatement statement = connection.prepareStatement("SELECT 1 FROM users WHERE username = ? AND password = ?")) {
                statement.setString(1, username);
                statement.setString(2, password);
                resultSet = statement.executeQuery();
            }
            return resultSet.next();
        } finally {
            closeConnection(connection);
        }
    }
    public boolean userExists(String username) throws SQLException {
        Connection con = dataSource.getConnection();
        try {
            ResultSet rs;
            try (PreparedStatement stmt = con.prepareStatement("select 1 from users where username = ?")) {
                stmt.setString(1, username);
                rs = stmt.executeQuery();
            }
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } finally {
            closeConnection(con);
        }
    }

    public void createUser(User user, String password) throws SQLException {
        Connection connection = dataSource.getConnection();
        try {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO users(username, password, name, last_name, email, company_name, user_type) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?)")) {
                statement.setString(1, user.getLogin());
                statement.setString(2, password);
                statement.setString(3, user.getUserName());
                statement.setString(4, user.getLastName());
                statement.setString(5, user.getEmail());
                statement.setString(6, user.getCompanyName());
                statement.setInt(7, user.getUserType());
                statement.execute();
            }
        } finally {
            closeConnection(connection);
        }
    }
    public List<User> getAllUsers() throws SQLException {
        Connection connection = dataSource.getConnection();
        try {
            List<User> users = new ArrayList<>();

            String sql = "SELECT user_id, name, last_name, email FROM users";

            ResultSet resultSet;
            try (PreparedStatement statement = connection.prepareStatement(sql)) {

                resultSet = statement.executeQuery();
            }
            while (resultSet.next()) {
                User user = new User();
                user.setUser_id(resultSet.getInt(1));
                user.setUserName(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setEmail(resultSet.getString(4));
                users.add(user);
            }

            return users;
        } finally {
            closeConnection(connection);
        }
    }

    public List<UserDisplayItem> getUserPrivileges(int course_id, int user_id) throws SQLException {
        Connection connection = dataSource.getConnection();
        try {
            List<UserDisplayItem> userItems = new ArrayList<>();

            String sql = "SELECT u.user_id, name, last_name, company_name, IFNULL(umc.course_id, 0) " +
                    "FROM users u " +
                    "LEFT JOIN user_managed_courses umc ON u.user_id = umc.user_id AND umc.course_id = ? " +
                    "WHERE u.user_id != ?";

            ResultSet resultSet;
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, course_id);
                stmt.setInt(2, user_id);

                resultSet = stmt.executeQuery();
            }
            while (resultSet.next()) {
                UserDisplayItem userItem = new UserDisplayItem();
                userItem.setUserId(resultSet.getInt(1));
                userItem.setName(resultSet.getString(2));
                userItem.setSurname(resultSet.getString(3));
                userItem.setCompanyName(resultSet.getString(4));
                if (resultSet.getInt(5) > 0) {
                    userItem.setHasRights(1);
                } else {
                    userItem.setHasRights(0);
                }
                userItems.add(userItem);
            }

            return userItems;
        } finally {
            closeConnection(connection);
        }
    }
    public int getUserId(String login, String password) throws SQLException {
        Connection connection = dataSource.getConnection();
        try {
            int userId = -1;

            ResultSet resultSet;
            try (PreparedStatement stmt = connection.prepareStatement("SELECT user_id FROM users WHERE username = ? AND password = ?")) {
                stmt.setString(1, login);
                stmt.setString(2, password);
                resultSet = stmt.executeQuery();
            }
            if (resultSet.next()) {
                userId = resultSet.getInt(1);
            }

            return userId;
        } finally {
            closeConnection(connection);
        }
    }
    public List<Course> getAllCourses() throws SQLException {

        Connection con = dataSource.getConnection();
        try {
            List<Course> courses = new ArrayList<>();

            ResultSet rs;
            try (PreparedStatement stmt = con.prepareStatement("SELECT course_id, creator_id, name, description, " +
                    "IFNULL(course_length,'Not mentioned'), (select u.username from users u where u.user_id=creator_id )" +
                    "as username FROM courses")) {
                rs = stmt.executeQuery();
            }
            while (rs.next()) {
                Course course = new Course();
                course.setCourse_id(rs.getInt(1));
                course.setCreator_id(rs.getInt(2));
                course.setCourseName(rs.getString(3));
                course.setDescription(rs.getString(4));
                course.setCourseLength(rs.getString(5));
                course.setUsername(rs.getString(6));
                courses.add(course);
            }

            return courses;
        } finally {
            closeConnection(con);
        }
    }

    public List<Course> getEnrolledCourses(int user_id) throws SQLException {
        Connection con = dataSource.getConnection();
        try {
            List<Course> courses = new ArrayList<Course>();

            ResultSet rs;
            try (PreparedStatement stmt = con.prepareStatement("SELECT distinct c.course_id, c.creator_id, c.name, c.description" +
                    ", (select u.username from users u where u.user_id=c.creator_id ) as username, IFNULL(course_length,'Not mentioned')" +
                    "FROM courses c, user_enrolled_courses uc " +
                    "WHERE uc.course_id = c.course_id " +
                    "AND uc.user_id = ?")) {
                stmt.setInt(1, user_id);
                rs = stmt.executeQuery();
            }
            while (rs.next()) {
                Course course = new Course();
                course.setCourse_id(rs.getInt(1));
                course.setCreator_id(rs.getInt(2));
                course.setCourseName(rs.getString(3));
                course.setDescription(rs.getString(4));
                course.setUsername(rs.getString(5));
                course.setCourseLength(rs.getString(6));
                courses.add(course);
            }

            return courses;
        } finally {
            closeConnection(con);
        }
    }

    public List<Course> getManagedCourses(int user_id) throws SQLException {
        Connection con = dataSource.getConnection();
        try {
            List<Course> courses = new ArrayList<Course>();

            ResultSet rs;
            try (PreparedStatement stmt = con.prepareStatement("SELECT DISTINCT c.course_id, c.creator_id, c.name, c.description " +
                    "FROM courses c, user_managed_courses uc " +
                    "WHERE ((uc.course_id = c.course_id AND uc.user_id = ?) OR c.creator_id = ?);")) {
                stmt.setInt(1, user_id);
                stmt.setInt(2, user_id);
                rs = stmt.executeQuery();
            }
            while (rs.next()) {
                Course course = new Course();
                course.setCourse_id(rs.getInt(1));
                course.setCreator_id(rs.getInt(2));
                course.setCourseName(rs.getString(3));
                course.setDescription(rs.getString(4));
                courses.add(course);
            }

            return courses;
        } finally {
            closeConnection(con);
        }
    }

    public List<Folder> getFoldersForParent(int course_id, int parent_id) throws SQLException {
        Connection con = dataSource.getConnection();
        try {
            List<Folder> folders = new ArrayList<Folder>();

            String sql = "SELECT folder_id, course_id, name, parent_id " +
                    "FROM folders " +
                    "WHERE course_id = ? ";
            if (parent_id == 0) {
                sql += "AND parent_id is null";
            } else {
                sql += "AND parent_id = ?";
            }
            ResultSet rs;
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, course_id);
                if (parent_id != 0) {
                    stmt.setInt(2, parent_id);
                }
                rs = stmt.executeQuery();
            }
            while (rs.next()) {
                Folder folder = new Folder();
                folder.setFolder_id(rs.getInt(1));
                folder.setCourse_id(rs.getInt(2));
                folder.setFolderName(rs.getString(3));
                folder.setParent_id(rs.getInt(4));
                folders.add(folder);
            }

            return folders;
        } finally {
            closeConnection(con);
        }
    }

    public List<File> getFilesForFolder(int folder_id) throws SQLException {
        Connection con = dataSource.getConnection();
        try {
            List<File> files = new ArrayList<File>();

            ResultSet rs;
            try (PreparedStatement stmt = con.prepareStatement("SELECT file_id, folder_id, name, size " +
                    "FROM files " +
                    "WHERE folder_id = ?")) {
                stmt.setInt(1, folder_id);
                rs = stmt.executeQuery();
            }
            while (rs.next()) {
                File file = new File();
                file.setFile_id(rs.getInt(1));
                file.setFolder_id(rs.getInt(2));
                file.setFileName(rs.getString(3));
                file.setFileSize(rs.getInt(4));
                files.add(file);
            }

            return files;
        } finally {
            closeConnection(con);
        }
    }

    public void linkUserAndCourse(int user_id, int course_id) throws SQLException {
        Connection con = dataSource.getConnection();
        try {
            try (PreparedStatement stmt = con.prepareStatement("INSERT INTO user_enrolled_courses(user_id, course_id) VALUES(?, ?)")) {
                stmt.setInt(1, user_id);
                stmt.setInt(2, course_id);
                stmt.execute();
            }
        } finally {
            closeConnection(con);
        }
    }

    public void removeUserFromCourse(int user_id, int course_id) throws SQLException {
        Connection con = dataSource.getConnection();
        try {
            try (PreparedStatement stmt = con.prepareStatement("DELETE FROM user_enrolled_courses WHERE user_id = ? AND course_id = ?")) {
                stmt.setInt(1, user_id);
                stmt.setInt(2, course_id);
                stmt.execute();
            }
        } finally {
            closeConnection(con);
        }
    }

    public boolean checkIfUserIsInCourse(int user_id, int course_id) throws SQLException {
        Connection con = dataSource.getConnection();
        try {
            ResultSet rs;
            try (PreparedStatement stmt = con.prepareStatement("SELECT 1 FROM user_enrolled_courses WHERE user_id = ? AND course_id = ?")) {
                stmt.setInt(1, user_id);
                stmt.setInt(2, course_id);
                rs = stmt.executeQuery();
            }
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } finally {
            closeConnection(con);
        }
    }

    public boolean checkIfUserIsCreator(int user_id, int course_id) throws SQLException {
        Connection con = dataSource.getConnection();
        try {
            ResultSet rs;
            try (PreparedStatement stmt = con.prepareStatement("SELECT 1 FROM courses WHERE creator_id = ? AND course_id = ?")) {
                stmt.setInt(1, user_id);
                stmt.setInt(2, course_id);
                rs = stmt.executeQuery();
            }
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } finally {
            closeConnection(con);
        }
    }

    public int getEnrolledCount(int course_id) throws SQLException {
        Connection con = dataSource.getConnection();
        try {
            ResultSet rs;
            try (PreparedStatement stmt = con.prepareStatement("SELECT count(user_id) FROM user_enrolled_courses WHERE course_id = ?")) {
                stmt.setInt(1, course_id);
                rs = stmt.executeQuery();
            }
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } finally {
            closeConnection(con);
        }
    }

    public int getEditRightCount(int course_id) throws SQLException {
        Connection con = dataSource.getConnection();
        try {
            ResultSet rs;
            try (PreparedStatement stmt = con.prepareStatement("SELECT count(user_id) FROM user_managed_courses WHERE course_id = ?")) {
                stmt.setInt(1, course_id);
                rs = stmt.executeQuery();
            }
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } finally {
            closeConnection(con);
        }
    }

    public void updateCourseInfo(int course_id, String name, String description) throws SQLException {
        Connection con = dataSource.getConnection();
        try {
            try (PreparedStatement stmt = con.prepareStatement("UPDATE courses SET name = ?, description = ? WHERE course_id = ?")) {
                stmt.setString(1, name);
                stmt.setString(2, description);
                stmt.setInt(3, course_id);
                stmt.execute();
            }
        } finally {
            closeConnection(con);
        }
    }

    public void updateFolderInfo(int folder_id, String name) throws SQLException {
        Connection con = dataSource.getConnection();
        try {
            try (PreparedStatement stmt = con.prepareStatement("UPDATE folders SET name = ? WHERE folder_id = ?")) {
                stmt.setString(1, name);
                stmt.setInt(2, folder_id);
                stmt.execute();
            }
        } finally {
            closeConnection(con);
        }
    }

    public void insertFolderGetId(Folder folder) throws SQLException {
        Connection con = dataSource.getConnection();
        try {
            ResultSet rs;
            try (PreparedStatement stmt = con.prepareStatement("INSERT INTO folders(course_id, name, parent_id) VALUES (?, ?, ?)")) {
                stmt.setInt(1, folder.getCourse_id());
                stmt.setString(2, folder.getFolderName());
                if (folder.getParent_id() == 0) {
                    stmt.setString(3, null);
                } else {
                    stmt.setInt(3, folder.getParent_id());
                }
                stmt.execute();
                stmt = con.prepareStatement("SELECT LAST_INSERT_ID()");
                rs = stmt.executeQuery();
            }
            if (rs.next()) {
                folder.setFolder_id(rs.getInt(1));
            }
        } finally {
            closeConnection(con);
        }
    }

    public void deleteFolder(int folder_id) throws SQLException {
        Connection con = dataSource.getConnection();
        try {
            try (PreparedStatement stmt = con.prepareStatement("DELETE FROM folders WHERE folder_id = ?")) {
                stmt.setInt(1, folder_id);
                stmt.execute();
            }
        } finally {
            closeConnection(con);
        }
    }

    public void insertFileGetId(File file) throws SQLException {
        Connection con = dataSource.getConnection();
        try {
            ResultSet rs;
            try (PreparedStatement stmt = con.prepareStatement("INSERT INTO files(folder_id, name, size) VALUES (?, ?, ?)")) {
                stmt.setInt(1, file.getFolder_id());
                stmt.setString(2, file.getFileName());
                stmt.setInt(3, file.getFileSize());
                stmt.execute();
                stmt = con.prepareStatement("SELECT LAST_INSERT_ID()");
                rs = stmt.executeQuery();
            }
            if (rs.next()) {
                file.setFile_id(rs.getInt(1));
            }
        } finally {
            closeConnection(con);
        }
    }

    public void deleteFile(int file_id) throws SQLException {
        Connection con = dataSource.getConnection();
        try {
            try (PreparedStatement stmt = con.prepareStatement("DELETE FROM files WHERE file_id = ?")) {
                stmt.setInt(1, file_id);
                stmt.execute();
            }
        } finally {
            closeConnection(con);
        }
    }

    public boolean checkIfCourseExists(String name) throws SQLException {
        Connection con = dataSource.getConnection();
        try {
            ResultSet rs;
            try (PreparedStatement stmt = con.prepareStatement("SELECT 1 FROM courses WHERE name = ?")) {
                stmt.setString(1, name);
                rs = stmt.executeQuery();
            }
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } finally {
            closeConnection(con);
        }
    }

    public void insertCourse(Course course) throws SQLException {
        Connection con = dataSource.getConnection();
        try {
            try (PreparedStatement stmt = con.prepareStatement("INSERT INTO courses(creator_id, name, description," +
                    " course_length) VALUES (?, ?, ?, ?)")) {
                stmt.setInt(1, course.getCreator_id());
                stmt.setString(2, course.getCourseName());
                stmt.setString(3, course.getDescription());
                stmt.setString(4, course.getCourseLength());
                stmt.execute();
            }
        } finally {
            closeConnection(con);
        }
    }

    public void deleteCourse(int course_id) throws SQLException {
        Connection con = dataSource.getConnection();
        try {
            PreparedStatement stmt = con.prepareStatement("DELETE FROM courses WHERE course_id = ?");
            stmt.setInt(1, course_id);
            stmt.execute();
        } finally {
            closeConnection(con);
        }
    }

    public void setUserPrivileges(int user_id, int course_id) throws SQLException {
        Connection con = dataSource.getConnection();
        try {
            try (PreparedStatement stmt = con.prepareStatement("INSERT INTO user_managed_courses(user_id, course_id) VALUES(?, ?)")) {
                stmt.setInt(1, user_id);
                stmt.setInt(2, course_id);
                stmt.execute();
            }
        } finally {
            closeConnection(con);
        }
    }

    public void deleteUserPrivileges(int user_id, int course_id) throws SQLException {
        Connection con = dataSource.getConnection();
        try {
            try (PreparedStatement stmt = con.prepareStatement("DELETE FROM user_managed_courses WHERE user_id = ? AND course_id = ?")) {
                stmt.setInt(1, user_id);
                stmt.setInt(2, course_id);
                stmt.execute();
            }
        } finally {
            closeConnection(con);
        }
    }

}

