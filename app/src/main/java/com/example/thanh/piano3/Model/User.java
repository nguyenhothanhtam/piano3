package com.example.thanh.piano3.Model;

public class User {
    private String username;
    private String password;
    private String email;
    private String gender;
    private String birthdate;

    public User(){}

    public User(String date, String gender, String email, String password, String username){
        this.username = username;
        this.password = password;
        this.email = email;
        this.gender = gender;
        this.birthdate = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }
}
