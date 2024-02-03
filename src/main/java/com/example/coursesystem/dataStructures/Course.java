package com.example.coursesystem.dataStructures;

public class Course {
    private int course_id;
    private int creator_id;
    private String course_length;
    private String name;
    private String description;
    private String username;

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public int getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }

    public String getCourseLength() {
        return course_length;
    }

    public void setCourseLength(String course_length) {
        this.course_length = course_length;
    }

    public String getCourseName() {
        return name;
    }

    public void setCourseName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
