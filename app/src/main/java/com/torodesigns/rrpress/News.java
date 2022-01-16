package com.torodesigns.rrpress;

import java.util.ArrayList;

public class News {
    private static int newsId = 0;
    private String newsHeadLine;
    private String newsDetails;
    private int newsPicId;
    private String newsCategory;
    private int likes;
    private int views;
    private String timeElapsed;
    public static ArrayList<News> newsList;

    public News(String newsHeadLine,int newsPicId,String newsCategory,int likes,int views,
                String timeElapsed){
        this.newsId = ++newsId;
        this.newsHeadLine = newsHeadLine;
        this.newsPicId = newsPicId;
        this.newsCategory = newsCategory;
        this.likes = likes;
        this.views = views;
        this.timeElapsed = timeElapsed;

    }

    public static int getNewsId() {
        return newsId;
    }

    public void setNewsHeadLine(String newsHeadLine) {
        this.newsHeadLine = newsHeadLine;
    }

    public void setNewsDetails(String newsDetails) {
        this.newsDetails = newsDetails;
    }

    public void setNewsCategory(String newsCategory) {
        this.newsCategory = newsCategory;
    }

    public void setNewsPicId(int newsPicId) {
        this.newsPicId = newsPicId;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setTimeElapsed(String timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getNewsPicId() {
        return newsPicId;
    }

    public String getNewsDetails() {
        return newsDetails;
    }

    public String getNewsCategory() {
        return newsCategory;
    }

    public String getNewsHeadLine() {
        return newsHeadLine;
    }

    public String getLikes() {
        return String.valueOf(likes);
    }

    public String getViews() {
        return String.valueOf(views);
    }

    public String getTimeElapsed() {
        return timeElapsed;
    }
}
