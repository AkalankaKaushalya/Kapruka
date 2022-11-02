package com.kapruka.Models;

public class PostCommnetsModel {
    String CommnetID, PostID, UserID, Comment, TimesTamp;

    public PostCommnetsModel() {
    }

    public PostCommnetsModel(String commnetID, String postID, String userID, String comment, String timesTamp) {
        CommnetID = commnetID;
        PostID = postID;
        UserID = userID;
        Comment = comment;
        TimesTamp = timesTamp;
    }

    public String getCommnetID() {
        return CommnetID;
    }

    public void setCommnetID(String commnetID) {
        CommnetID = commnetID;
    }

    public String getPostID() {
        return PostID;
    }

    public void setPostID(String postID) {
        PostID = postID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getTimesTamp() {
        return TimesTamp;
    }

    public void setTimesTamp(String timesTamp) {
        TimesTamp = timesTamp;
    }
}
