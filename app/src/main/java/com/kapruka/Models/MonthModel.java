package com.kapruka.Models;

public class MonthModel {
    String Month, UserID;

    public MonthModel() {
    }

    public MonthModel(String month, String userID) {
        Month = month;
        UserID = userID;
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }
}
