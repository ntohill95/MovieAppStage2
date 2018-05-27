package com.example.niamhtohill.movieappudacity;

public class VideoObject {

    private String videoID;
    private String videoName;
    private String videoKey;
    private String videoSite;
    private String videoType;

    public VideoObject(String videoID, String videoName, String videoKey, String videoSite, String videoType){
        this.videoID=videoID;
        this.videoName=videoName;
        this.videoKey=videoKey;
        this.videoSite =videoSite;
        this.videoType=videoType;
    }

    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoKey() {
        return videoKey;
    }

    public void setVideoKey(String videoKey) {
        this.videoKey = videoKey;
    }

    public String getVideoSite() {
        return videoSite;
    }

    public void setVideoSite(String videoSite) {
        this.videoSite = videoSite;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }
}
