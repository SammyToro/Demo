package com.torodesigns.rrpress;

import java.util.ArrayList;
import java.util.List;

public class Cartoona {

    private String headline;
    private String dateCreated;
    private int cartoonaImageId;
    private int numOfLikes;
    private boolean isLiked;
    private List<Comment> comments;

    public static List<Cartoona> cartoonas;

    //Constructor
    public Cartoona(String headline,String dateCreated,int cartoonaImageId,int numOfLikes){
        this.headline = headline;
        this.dateCreated = dateCreated;
        this.cartoonaImageId = cartoonaImageId;
        this.numOfLikes = numOfLikes;
        this.isLiked = false;
        this.comments = new ArrayList<>();
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setCartoonaImageId(int cartoonaImageId) {
        this.cartoonaImageId = cartoonaImageId;
    }

    public void setNumOfLikes(int numOfLikes) {
        this.numOfLikes = numOfLikes;
    }


    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public String getHeadline() {
        return headline;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public int getCartoonaImageId() {
        return cartoonaImageId;
    }

    public int getNumOfLikes() {
        return numOfLikes;
    }

    public int getNumOfComments() {
        return comments.size();
    }

    public List<Comment> getComments() {
        return comments;
    }

    public boolean isLiked() {
        return isLiked;
    }
}
