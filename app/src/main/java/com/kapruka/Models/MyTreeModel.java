package com.kapruka.Models;

public class MyTreeModel {
    String TreeID, UserID, TreeHeight, TreeAge;

    public MyTreeModel() {
    }

    public MyTreeModel(String treeID, String userID, String treeHeight, String treeAge) {
        TreeID = treeID;
        UserID = userID;
        TreeHeight = treeHeight;
        TreeAge = treeAge;
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

    public String getTreeHeight() {
        return TreeHeight;
    }

    public void setTreeHeight(String treeHeight) {
        TreeHeight = treeHeight;
    }

    public String getTreeAge() {
        return TreeAge;
    }

    public void setTreeAge(String treeAge) {
        TreeAge = treeAge;
    }
}
