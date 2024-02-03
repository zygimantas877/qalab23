package com.example.coursesystem.webControllers;

import com.example.coursesystem.appClasses.Database;
import com.example.coursesystem.dataStructures.Course;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Properties;

@Controller
public class WebCourseController {
    @RequestMapping(value = "/course/allCourses", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ResponseEntity getAllCourses() {
        Gson gson = new Gson();
        try {
            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(Database.getInstance().getAllCourses()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve courses");
        }
    }

    @RequestMapping(value = "/course/enrolledCourses", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ResponseEntity getEnrolledCourses(@RequestParam(name = "user_id") int user_id) {
        Gson gson = new Gson();
        try {
            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(Database.getInstance().getEnrolledCourses(user_id)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve courses");
        }
    }

    @RequestMapping(value = "/course/managedCourses", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ResponseEntity getManagedCourses(@RequestParam(name = "user_id") int user_id) {
        Gson gson = new Gson();
        try {
            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(Database.getInstance().getManagedCourses(user_id)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve courses");
        }
    }

    @RequestMapping(value = "/course/insertCourse", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ResponseEntity insertCourse(@RequestBody String request) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);
        Course course = new Course();
        try {
            course.setCreator_id(Integer.parseInt(properties.getProperty("creator_id")));
            course.setCourseName(properties.getProperty("name"));
            course.setDescription(properties.getProperty("description"));
            Database.getInstance().insertCourse(course);
            return ResponseEntity.status(HttpStatus.OK).body("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create course");
        }
    }

    @RequestMapping(value = "/course/deleteCourse", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ResponseEntity deleteCourse(@RequestParam(name = "course_id") int course_id) {
        try {
            Database.getInstance().deleteCourse(course_id);
            return ResponseEntity.status(HttpStatus.OK).body("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete course");
        }
    }

    @RequestMapping(value = "course/leaveCourse", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ResponseEntity leaveCourse(@RequestParam(name = "user_id") int user_id, @RequestParam(name = "course_id") int course_id) {
        try {
            Database.getInstance().removeUserFromCourse(user_id,course_id);
            return ResponseEntity.status(HttpStatus.OK).body("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete course");
        }
    }
}
