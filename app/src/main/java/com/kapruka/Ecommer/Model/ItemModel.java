package com.kapruka.Ecommer.Model;

public class ItemModel {
    String  ItmeID, UserID, ItmeTitle, ItmePrice, ItmeCity, ItmeDescription, DiseaseImage, ViewCount;

    public ItemModel() {
    }

    public ItemModel(String itmeID, String userID, String itmeTitle, String itmePrice, String itmeCity, String itmeDescription, String diseaseImage, String viewCount) {
        ItmeID = itmeID;
        UserID = userID;
        ItmeTitle = itmeTitle;
        ItmePrice = itmePrice;
        ItmeCity = itmeCity;
        ItmeDescription = itmeDescription;
        DiseaseImage = diseaseImage;
        ViewCount = viewCount;
    }

    public String getItmeID() {
        return ItmeID;
    }

    public void setItmeID(String itmeID) {
        ItmeID = itmeID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getItmeTitle() {
        return ItmeTitle;
    }

    public void setItmeTitle(String itmeTitle) {
        ItmeTitle = itmeTitle;
    }

    public String getItmePrice() {
        return ItmePrice;
    }

    public void setItmePrice(String itmePrice) {
        ItmePrice = itmePrice;
    }

    public String getItmeCity() {
        return ItmeCity;
    }

    public void setItmeCity(String itmeCity) {
        ItmeCity = itmeCity;
    }

    public String getItmeDescription() {
        return ItmeDescription;
    }

    public void setItmeDescription(String itmeDescription) {
        ItmeDescription = itmeDescription;
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
