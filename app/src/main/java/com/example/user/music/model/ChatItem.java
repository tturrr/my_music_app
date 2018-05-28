package com.example.user.music.model;

public class ChatItem {

    private String comment;
    private String uid;

    public ChatItem(){}

    public ChatItem(String comment, String uid) {
        this.comment = comment;
        this.uid = uid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
