package com.example.authentication.Models;

public class Reels {
    String ReelsCreatedBy,reelsProfileImageUri,reelsId,reelsCaptions,reelsUri,uid;
    private long timeStamp;

    public Reels() {
    }

    public Reels(String reelsCreatedBy, String reelsProfileImageUri, String reelsId, String reelsCaptions, String reelsUri, long timeStamp) {
        this.ReelsCreatedBy = reelsCreatedBy;
        this.reelsProfileImageUri = reelsProfileImageUri;
        this.reelsId = reelsId;
        this.reelsCaptions = reelsCaptions;
        this.reelsUri = reelsUri;
        this.timeStamp = timeStamp;
    }

    public String getReelsCreatedBy() {
        return ReelsCreatedBy;
    }

    public void setReelsCreatedBy(String reelsCreatedBy) {
        ReelsCreatedBy = reelsCreatedBy;
    }

    public String getReelsProfileImageUri() {
        return reelsProfileImageUri;
    }

    public void setReelsProfileImageUri(String reelsProfileImageUri) {
        this.reelsProfileImageUri = reelsProfileImageUri;
    }

    public String getReelsId() {
        return reelsId;
    }

    public void setReelsId(String reelsId) {
        this.reelsId = reelsId;
    }

    public String getReelsCaptions() {
        return reelsCaptions;
    }

    public void setReelsCaptions(String reelsCaptions) {
        this.reelsCaptions = reelsCaptions;
    }

    public String getReelsUri() {
        return reelsUri;
    }

    public void setReelsUri(String reelsUri) {
        this.reelsUri = reelsUri;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
