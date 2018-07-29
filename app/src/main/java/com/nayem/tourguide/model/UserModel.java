package com.nayem.tourguide.model;

public class UserModel {
    private int uID;
    private String userID;
    private String password;
    private String fullName;
    private String email;
    private String mobile;
    private String address;

    public UserModel(int uID, String userID, String password, String fullName, String email, String mobile, String address) {
        this.uID = uID;
        this.userID = userID;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
    }

    public UserModel(String userID, String password) {
        this.userID = userID;
        this.password = password;
    }

    public UserModel(String fullName, String email, String mobile, String address) {
        this.fullName = fullName;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
    }

    public UserModel(int uID, String fullName, String email, String mobile, String address) {
        this.uID = uID;
        this.fullName = fullName;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
    }

    public UserModel() {
    }

    public int getuID() {
        return uID;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAddress() {
        return address;
    }
}
