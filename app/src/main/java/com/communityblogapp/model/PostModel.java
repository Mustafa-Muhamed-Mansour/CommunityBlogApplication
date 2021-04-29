package com.communityblogapp.model;

public class PostModel
{
    String post_title;
    String post_description;
    String post_key;
    String post_picture;
    String photo_profile;
    Object post_time;


    public PostModel()
    {
    }

    public PostModel(String post_title, String post_description, String post_key, String post_picture, String photo_profile)
    {
        this.post_title = post_title;
        this.post_description = post_description;
        this.post_key = post_key;
        this.post_picture = post_picture;
        this.photo_profile = photo_profile;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getPost_description() {
        return post_description;
    }

    public void setPost_description(String post_description) {
        this.post_description = post_description;
    }

    public String getPost_key() {
        return post_key;
    }

    public void setPost_key(String post_key) {
        this.post_key = post_key;
    }

    public String getPost_picture() {
        return post_picture;
    }

    public void setPost_picture(String post_picture) {
        this.post_picture = post_picture;
    }

    public String getPhoto_profile() {
        return photo_profile;
    }

    public void setPhoto_profile(String photo_profile) {
        this.photo_profile = photo_profile;
    }


    public Object getPost_time() {
        return post_time;
    }

    public void setPost_time(Object post_time) {
        this.post_time = post_time;
    }
}
