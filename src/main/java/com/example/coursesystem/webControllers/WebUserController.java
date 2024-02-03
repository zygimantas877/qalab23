package com.example.coursesystem.webControllers;

import com.example.coursesystem.appClasses.Database;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.coursesystem.dataStructures.*;

import java.util.Properties;

@Controller
    public class WebUserController {
    @RequestMapping(value = "/user/allUsers", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ResponseEntity getAllUsers() {
        Gson gson = new Gson();
        try {
            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(Database.getInstance().getAllUsers()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve users");
        }
    }

    @RequestMapping(value = "/user/createUser", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ResponseEntity createUser(@RequestBody String request) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);
        User user = new User();
        try {
            user.setUserName(properties.getProperty("name"));
            user.setLastName(properties.getProperty("last_name"));
            user.setEmail(properties.getProperty("email"));
            user.setLogin(properties.getProperty("username"));
            user.setCompanyName(properties.getProperty("company_name"));
            user.setUserType(Integer.parseInt(properties.getProperty("user_type")));
            Database.getInstance().createUser(user, properties.getProperty("password"));
            return ResponseEntity.status(HttpStatus.OK).body("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create user");
        }
    }

    @RequestMapping(value = "/user/setUserRights", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ResponseEntity setUserRights(@RequestParam(name = "user_id") int user_id, @RequestParam(name = "course_id") int course_id) {
        try {
            Database.getInstance().setUserPrivileges(user_id, course_id);
            return ResponseEntity.status(HttpStatus.OK).body("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create user");
        }
    }

    @RequestMapping(value = "/user/deleteUserRights", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ResponseEntity deleteUserRights(@RequestParam(name = "user_id") int user_id, @RequestParam(name = "course_id") int course_id) {
        try {
            Database.getInstance().deleteUserPrivileges(user_id, course_id);
            return ResponseEntity.status(HttpStatus.OK).body("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create user");
        }
    }

    @RequestMapping(value = "/user/checkLoginInfoAndGetUserId", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ResponseEntity checkLoginInfoAndGetUserId(@RequestBody String request) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);
        try {
            String username = properties.getProperty("username");
            String password = properties.getProperty("password");
            if(Database.getInstance().checkLoginInfo(username, password)) {
                int user_id = Database.getInstance().getUserId(username,password);
                String json = "{\"user_id\":\"" + user_id + "\"}";
                return ResponseEntity.status(HttpStatus.OK).body(json);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong credentials");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve users");
        }
    }

    @RequestMapping(value = "/user/linkUserAndCourse", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ResponseEntity linkUserAndCourse(@RequestParam(name = "user_id") int user_id, @RequestParam(name = "course_id") int course_id) {
        try {
            Database.getInstance().linkUserAndCourse(user_id, course_id);
            return ResponseEntity.status(HttpStatus.OK).body("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to enroll user");
        }
    }
}
