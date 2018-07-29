package com.nayem.tourguide.model;

/**
 * Created by Hasna Amir on 2/8/2017.
 */

public class MomentModel {

    private int mID;
    private int eID;
    private String title;
    private String details;
    private String image;
    private String date;


    public MomentModel(int mID, int eID, String title, String details, String image, String date) {
        this.mID = mID;
        this.eID = eID;
        this.title = title;
        this.details = details;
        this.image = image;
        this.date = date;
    }

    public MomentModel(int mID, String title, String details, String image, String date) {
        this.mID = mID;
        this.title = title;
        this.details = details;
        this.image = image;
        this.date = date;
    }

    public MomentModel(String title, String details, String image, String date) {
        this.title = title;
        this.details = details;
        this.image = image;
        this.date = date;
    }

    public MomentModel() {
    }

    public int getmID() {
        return mID;
    }

    public int geteID() {
        return eID;
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    public String getImage() {
        return image;
    }

    public String getDate() {
        return date;
    }
}
