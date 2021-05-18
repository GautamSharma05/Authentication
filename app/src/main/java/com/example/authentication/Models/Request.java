package com.example.authentication.Models;

public class Request {
    String senderUid;

    public Request(String senderUid) {
        this.senderUid = senderUid;
    }

    public Request() {
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }
}
