package com.beyzanur.expiration_date_reminder;

public class User {

    private String userName, userMail, userId;

    public User(String userName, String userMail, String userId) {
        this.userName = userName;
        this.userMail = userMail;
        this.userId = userId;
    }

    public User(){}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
