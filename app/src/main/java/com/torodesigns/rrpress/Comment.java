package com.torodesigns.rrpress;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Comment implements Serializable { //implements the serializable interface to enable obejct to be passed in intent
    private String name;
    private String timeElapsed;
    private int numOfLikes;
    private String message;
    private List<Comment> replies;
    private boolean isLiked;

    //constructor
    public Comment(String name,String timeElapsed,int numOfLikes,String message){
        this.name = name;
        this.timeElapsed = timeElapsed;
        this.numOfLikes = numOfLikes;
        this.message = message;
        this.isLiked = false;
        replies = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setTimeElapsed(String timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    public String getTimeElapsed() {
        return timeElapsed;
    }

    public void setNumOfLikes(int numOfLikes) {
        this.numOfLikes = numOfLikes;
    }

    public int getNumOfLikes() {
        return numOfLikes;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public int getNumberOfReplies(){
        return replies.size();
    }
}
