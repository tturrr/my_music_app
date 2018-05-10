package com.example.user.music;

import android.net.Uri;

public class board_List_Item {
    private String content;

    private String title;

    private Uri bod_img;

    public board_List_Item(String title, String content, Uri bod_img) {
        this.title = title;
        this.content = content;
        this.bod_img = bod_img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Uri getBod_img() {
        return bod_img;
    }

    public void setBod_img(Uri bod_img) {
        this.bod_img = bod_img;
    }

}
