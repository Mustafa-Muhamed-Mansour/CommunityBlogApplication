package com.communityblogapp.model;

public class CommentModel
{
    String comment_content;
    String comment_name;
    String comment_image;
    String comment_id;

    public CommentModel()
    {
    }

    public CommentModel(String comment_content, String comment_name, String comment_image, String comment_id)
    {
        this.comment_content = comment_content;
        this.comment_name = comment_name;
        this.comment_image = comment_image;
        this.comment_id = comment_id;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public String getComment_name() {
        return comment_name;
    }

    public void setComment_name(String comment_name) {
        this.comment_name = comment_name;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment_image() {
        return comment_image;
    }

    public void setComment_image(String comment_image) {
        this.comment_image = comment_image;
    }

}
