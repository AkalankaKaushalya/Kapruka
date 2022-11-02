package com.kapruka.Models;

public class DiseaseModel {
    String DiseaseID, UserID, DiseaseTitle, DiseaseDescription, DiseaseImage, ViewCount;

    public DiseaseModel() {
    }

    public DiseaseModel(String diseaseID, String userID, String diseaseTitle, String diseaseDescription, String diseaseImage, String viewCount) {
        DiseaseID = diseaseID;
        UserID = userID;
        DiseaseTitle = diseaseTitle;
        DiseaseDescription = diseaseDescription;
        DiseaseImage = diseaseImage;
        ViewCount = viewCount;
    }

    public String getDiseaseID() {
        return DiseaseID;
    }

    public void setDiseaseID(String diseaseID) {
        DiseaseID = diseaseID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getDiseaseTitle() {
        return DiseaseTitle;
    }

    public void setDiseaseTitle(String diseaseTitle) {
        DiseaseTitle = diseaseTitle;
    }

    public String getDiseaseDescription() {
        return DiseaseDescription;
    }

    public void setDiseaseDescription(String diseaseDescription) {
        DiseaseDescription = diseaseDescription;
    }

    public String getDiseaseImage() {
        return DiseaseImage;
    }

    public void setDiseaseImage(String diseaseImage) {
        DiseaseImage = diseaseImage;
    }

    public String getViewCount() {
        return ViewCount;
    }

    public void setViewCount(String viewCount) {
        ViewCount = viewCount;
    }
}
