package com.kapruka.Models;

public class AskAdminModel {
    String AQusetionID, UserID, City, AQusetionTitle, AQusetionImage, AQusetionVideo;

    public AskAdminModel() {
    }

    public AskAdminModel(String AQusetionID, String userID, String city, String AQusetionTitle, String AQusetionImage, String AQusetionVideo) {
        this.AQusetionID = AQusetionID;
        UserID = userID;
        City = city;
        this.AQusetionTitle = AQusetionTitle;
        this.AQusetionImage = AQusetionImage;
        this.AQusetionVideo = AQusetionVideo;
    }

    public String getAQusetionID() {
        return AQusetionID;
    }

    public void setAQusetionID(String AQusetionID) {
        this.AQusetionID = AQusetionID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getAQusetionTitle() {
        return AQusetionTitle;
    }

    public void setAQusetionTitle(String AQusetionTitle) {
        this.AQusetionTitle = AQusetionTitle;
    }

    public String getAQusetionImage() {
        return AQusetionImage;
    }

    public void setAQusetionImage(String AQusetionImage) {
        this.AQusetionImage = AQusetionImage;
    }

    public String getAQusetionVideo() {
        return AQusetionVideo;
    }

    public void setAQusetionVideo(String AQusetionVideo) {
        this.AQusetionVideo = AQusetionVideo;
    }
}
