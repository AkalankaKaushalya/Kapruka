package com.kapruka.Models;

public class QusetionModel {
    String QusetionID, UserID, City, QusetionTitle, QusetionImage, QusetionVideo;

    public QusetionModel() {
    }

    public QusetionModel(String qusetionID, String userID, String city, String qusetionTitle, String qusetionImage, String qusetionVideo) {
        QusetionID = qusetionID;
        UserID = userID;
        City = city;
        QusetionTitle = qusetionTitle;
        QusetionImage = qusetionImage;
        QusetionVideo = qusetionVideo;
    }

    public String getQusetionID() {
        return QusetionID;
    }

    public void setQusetionID(String qusetionID) {
        QusetionID = qusetionID;
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

    public String getQusetionTitle() {
        return QusetionTitle;
    }

    public void setQusetionTitle(String qusetionTitle) {
        QusetionTitle = qusetionTitle;
    }

    public String getQusetionImage() {
        return QusetionImage;
    }

    public void setQusetionImage(String qusetionImage) {
        QusetionImage = qusetionImage;
    }

    public String getQusetionVideo() {
        return QusetionVideo;
    }

    public void setQusetionVideo(String qusetionVideo) {
        QusetionVideo = qusetionVideo;
    }
}
