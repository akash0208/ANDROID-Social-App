package com.example.ongraph.socialapp;

public class Search {
    private String name, id;
    private boolean friend;
    Search(String name, String id, boolean friend) {
        this.name = name;
        this.id = id;
        this.friend = friend;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isFriend() {
        return friend;
    }

    public void setFriend(boolean friend) {
        this.friend = friend;
    }
}
