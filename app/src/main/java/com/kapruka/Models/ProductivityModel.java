package com.kapruka.Models;

public class ProductivityModel {
    String Months, ProductCout;
    String TreeID, UserID;


    public ProductivityModel() {
    }

    public ProductivityModel(String months, String productCout, String treeID, String userID) {
        Months = months;
        ProductCout = productCout;
        TreeID = treeID;
        UserID = userID;
    }

    public String getMonths() {
        return Months;
    }

    public void setMonths(String months) {
        Months = months;
    }

    public String getProductCout() {
        return ProductCout;
    }

    public void setProductCout(String productCout) {
        ProductCout = productCout;
    }

    public String getTreeID() {
        return TreeID;
    }

    public void setTreeID(String treeID) {
        TreeID = treeID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }
}
