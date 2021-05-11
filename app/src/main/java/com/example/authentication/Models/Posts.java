package com.example.authentication.Models;

import java.sql.Timestamp;

public class Posts {
    private  String CreatedBy,Captions,PostImage,uid,ProfileImageUri;
    private long timeStamp;

    public Posts() {

    }

    public Posts(String createdBy, String captions, String postImage, String profileImageUri, long timeStamp) {
        this.CreatedBy = createdBy;
        this.Captions = captions;
        this.PostImage = postImage;
        this.ProfileImageUri = profileImageUri;
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

    public String getProfileImageUri() {
        return ProfileImageUri;
    }

    public void setProfileImageUri(String profileImageUri) {
        ProfileImageUri = profileImageUri;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
