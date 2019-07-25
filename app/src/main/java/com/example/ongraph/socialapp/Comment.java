package com.example.ongraph.socialapp;

public class Comment {
    private String commentname, commentId, comment, pic;

    public Comment(String commentname, String comment, String commentId, String pic) {
        this.commentname = commentname;
        this.commentId = commentId;
        this.comment = comment;
        this.pic = pic;
    }

    public String getCommentname() {
        return commentname;
    }

    public void setCommentname(String commentname) {
        this.commentname = commentname;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
