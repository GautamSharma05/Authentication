package com.example.authentication;

import java.sql.Timestamp;

public class Posts {
    private  String CreatedBy,Captions,PostImage,uid,profileImage;
    private long timeStamp;

    public Posts() {

    }

    public Posts(String createdBy, String captions, String postImage, String profileImage, long timeStamp) {
        this.CreatedBy = createdBy;
        this.Captions = captions;
        this.PostImage = postImage;
        this.profileImage = profileImage;
        this.timeStamp = timeStamp;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getCaptions() {
        return Captions;
    }

    public void setCaptions(String captions) {
        Captions = captions;
    }

    public String getPostImage() {
        return PostImage;
    }

    public void setPostImage(String postImage) {
        PostImage = postImage;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
