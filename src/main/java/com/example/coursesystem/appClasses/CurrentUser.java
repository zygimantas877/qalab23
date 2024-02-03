package com.example.coursesystem.appClasses;

public class CurrentUser {

    private CurrentUser() {}

    private static int user_id;

    public static int getUserId(){
        return user_id;
    }

    public static void setUserId(int newUser){
        user_id = newUser;
    }
}
