package com.example.coursesystem.webControllers;

import com.example.coursesystem.appClasses.Database;
import com.example.coursesystem.dataStructures.File;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Properties;

@Controller
public class WebFileController {
    @RequestMapping(value = "/file/getFilesForFolder", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ResponseEntity getFilesForFolder(@RequestParam(name = "folder_id") int folder_id) {
        Gson gson = new Gson();
        try {
            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(Database.getInstance().getFilesForFolder(folder_id)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve file data");
        }
    }

    @RequestMapping(value = "/file/deleteFile", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ResponseEntity deleteFile(@RequestParam(name = "file_id") int file_id) {
        try {
            Database.getInstance().deleteFile(file_id);
            return ResponseEntity.status(HttpStatus.OK).body("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete file");
        }
    }

    @RequestMapping(value = "/file/insertFile", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ResponseEntity insertFile(@RequestBody String request) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);
        File file = new File();
        try {
            file.setFolder_id(Integer.parseInt(properties.getProperty("folder_id")));
            file.setFileName(properties.getProperty("name"));
            file.setFileSize(Integer.parseInt(properties.getProperty("size")));
            Database.getInstance().insertFileGetId(file);
            return ResponseEntity.status(HttpStatus.OK).body("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create file");
        }
    }
}
