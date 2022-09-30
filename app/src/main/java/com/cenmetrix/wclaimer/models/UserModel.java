package com.cenmetrix.wclaimer.models;

public class UserModel {

    String user_FirstName = "";
    String user_LastName = "";
    String user_Email = "";
    String user_Role = "";

    public UserModel() {
    }

    public UserModel(String user_FirstName, String user_LastName, String user_Email) {
        this.user_FirstName = user_FirstName;
        this.user_LastName = user_LastName;
        this.user_Email = user_Email;
    }

    public String getUser_FirstName() {
        return user_FirstName;
    }

    public void setUser_FirstName(String user_FirstName) {
        this.user_FirstName = user_FirstName;
    }

    public String getUser_LastName() {
        return user_LastName;
    }

    public void setUser_LastName(String user_LastName) {
        this.user_LastName = user_LastName;
    }

    public String getUser_Email() {
        return user_Email;
    }

    public void setUser_Email(String user_Email) {
        this.user_Email = user_Email;
    }

    public String getUser_Role() {
        return user_Role;
    }

    public void setUser_Role(String user_Role) {
        this.user_Role = user_Role;
    }
}
