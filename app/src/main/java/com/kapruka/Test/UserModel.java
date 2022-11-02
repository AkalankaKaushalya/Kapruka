package com.kapruka.Test;

public class UserModel {
    String UserID, Name, Email, City, UserType, Image, timesTamp;

    public UserModel() {
    }

    public UserModel(String userID, String name, String email, String city, String userType, String image, String timesTamp) {
        UserID = userID;
        Name = name;
        Email = email;
        City = city;
        UserType = userType;
        Image = image;
        this.timesTamp = timesTamp;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getTimesTamp() {
        return timesTamp;
    }

    public void setTimesTamp(String timesTamp) {
        this.timesTamp = timesTamp;
    }
}
