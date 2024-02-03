package com.example.coursesystem.webControllers;

import com.example.coursesystem.appClasses.Database;
import com.example.coursesystem.dataStructures.Folder;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Properties;

@Controller
public class WebFolderController {
    @RequestMapping(value = "/folder/getFoldersForParent", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ResponseEntity getFoldersForParent(@RequestParam(name = "course_id") int course_id, @RequestParam(name = "parent_id", required = false) Integer parent_id) {
        Gson gson = new Gson();
        try {
            int parent;
            if(parent_id != null) {
                parent = parent_id;
            } else {
                parent = 0;
            }
            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(Database.getInstance().getFoldersForParent(course_id, parent)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve folder data");
        }
    }

    @RequestMapping(value = "/folder/insertFolder", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ResponseEntity insertFolder(@RequestBody String request) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);
        Folder folder = new Folder();
        try {
            folder.setCourse_id(Integer.parseInt(properties.getProperty("course_id")));
            folder.setFolderName(properties.getProperty("name"));
            if(properties.getProperty("parent_id") != null) {
                folder.setParent_id(Integer.parseInt(properties.getProperty("parent_id")));
            } else {
                folder.setParent_id(0);
            }
            Database.getInstance().insertFolderGetId(folder);
            return ResponseEntity.status(HttpStatus.OK).body("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create folder");
        }
    }

    @RequestMapping(value = "/folder/deleteFolder", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ResponseEntity deleteFolder(@RequestParam(name = "folder_id") int folder_id) {
        try {
            Database.getInstance().deleteFolder(folder_id);
            return ResponseEntity.status(HttpStatus.OK).body("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete folder");
        }
    }
}
