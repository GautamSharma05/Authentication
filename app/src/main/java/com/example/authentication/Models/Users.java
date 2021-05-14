package com.example.authentication.Models;

public class Users {
    private String uid,FullName,MobileNumber,Email,Bio,ProfileImage,RelationShipStatus,username;

    public Users() {
    }

    public Users(String fullName, String mobileNumber, String email, String bio, String profileImage, String relationShipStatus, String username) {
        this.FullName = fullName;
        this.MobileNumber = mobileNumber;
        this.Email = email;
        this.Bio = bio;
        this.ProfileImage = profileImage;
        this.RelationShipStatus = relationShipStatus;
        this.username = username;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }

    public String getRelationShipStatus() {
        return RelationShipStatus;
    }

    public void setRelationShipStatus(String relationShipStatus) {
        RelationShipStatus = relationShipStatus;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
