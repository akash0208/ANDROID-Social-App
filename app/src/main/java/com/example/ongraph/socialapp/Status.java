package com.example.ongraph.socialapp;

import java.util.Date;

public class Status {
    private String statusId;
    private String name;
    private String status;
    private Date date;
    private String img;
    private int like;
    private boolean isLike;

    public Status(String name, String status, Date date, String statusId, String img, int like, boolean isLike) {
        this.name = name;
        this.status = status;
        this.date = date;
        this.statusId=statusId;
        this.img=img;
        this.like = like;
        this.isLike = isLike;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getLike() {
        return like;
    }

    public void setLike(Boolean like) {
        isLike = like;
    }

    public void setLike(int like) {
        this.like = like;
    }
}