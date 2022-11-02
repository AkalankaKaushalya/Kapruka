package com.kapruka.Models;

public class YtVideoModel {
    String VideoID, UserID, VideoUrl, VideoTitle, VideoImge, ViewCount;

    public YtVideoModel() {
    }

    public YtVideoModel(String videoID, String userID, String videoUrl, String videoTitle, String videoImge, String viewCount) {
        VideoID = videoID;
        UserID = userID;
        VideoUrl = videoUrl;
        VideoTitle = videoTitle;
        VideoImge = videoImge;
        ViewCount = viewCount;
    }

    public String getVideoID() {
        return VideoID;
    }

    public void setVideoID(String videoID) {
        VideoID = videoID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getVideoUrl() {
        return VideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        VideoUrl = videoUrl;
    }

    public String getVideoTitle() {
        return VideoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        VideoTitle = videoTitle;
    }

    public String getVideoImge() {
        return VideoImge;
    }

    public void setVideoImge(String videoImge) {
        VideoImge = videoImge;
    }

    public String getViewCount() {
        return ViewCount;
    }

    public void setViewCount(String viewCount) {
        ViewCount = viewCount;
    }
}
