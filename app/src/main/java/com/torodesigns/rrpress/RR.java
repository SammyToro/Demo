package com.torodesigns.rrpress;

public class RR {
    private String itemName;
    private String timeElapsed;
    private String itemDescription;
    private int imageResourceId;
    private String reportersName;
    private String reportersPhoneNumber;
    public static RR[] rrList;

    //constructor
    public RR(String itemName,String timeElapsed,String itemDescription,int imageResourceId,
              String reportersName,String reportersPhoneNumber){
        this.itemName = itemName;
        this.timeElapsed = timeElapsed;
        this.itemDescription = itemDescription;
        this.imageResourceId = imageResourceId;
        this.reportersName = reportersName;
        this.reportersPhoneNumber = reportersPhoneNumber;
    }

    public RR(String itemName,String timeElapsed, int imageResourceId){
        this.itemName = itemName;
        this.timeElapsed = timeElapsed;
        this.imageResourceId = imageResourceId;
    }

    //Create getters and setters

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setTimeElapsed(String timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    public String getTimeElapsed() {
        return timeElapsed;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setReportersName(String reportersName) {
        this.reportersName = reportersName;
    }

    public String getReportersName() {
        return reportersName;
    }

    public void setReportersPhoneNumber(String reportersPhoneNumber) {
        this.reportersPhoneNumber = reportersPhoneNumber;
    }

    public String getReportersPhoneNumber() {
        return reportersPhoneNumber;
    }
}
