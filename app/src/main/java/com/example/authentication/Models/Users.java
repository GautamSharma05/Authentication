package com.example.authentication.Models;

public class Users {
    private String uid,FullName,MobileNumber,Email,Bio,ProfileImage,RelationshipStatus;

    public Users() {
    }

    public Users(String uid, String fullName, String mobileNumber, String email, String bio, String profileImage, String relationshipStatus) {
        this.uid = uid;
        this.FullName = fullName;
        this.MobileNumber = mobileNumber;
        this.Email = email;
        this.Bio = bio;
        this.ProfileImage = profileImage;
        this.RelationshipStatus = relationshipStatus;
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

    public String getRelationshipStatus() {
        return RelationshipStatus;
    }

    public void setRelationshipStatus(String relationshipStatus) {
        RelationshipStatus = relationshipStatus;
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
}
