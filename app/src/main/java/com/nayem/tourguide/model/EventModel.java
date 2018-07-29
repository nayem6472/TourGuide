package com.nayem.tourguide.model;

public class EventModel {
    private int eID;
    private int uID;
    private String title;
    private String destination;
    private int budget;
    private String startDate;
    private String endDate;


    public EventModel(int eID, String destination, String title, int budget, String startDate, String endDate) {
        this.eID = eID;
        this.destination = destination;
        this.title = title;
        this.budget = budget;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public EventModel(int uID,int eID, String title, String destination, int budget, String startDate, String endDate) {
        this.uID = uID;
        this.title = title;
        this.destination = destination;
        this.budget = budget;
        this.startDate = startDate;
        this.endDate = endDate;
        this.eID = eID;
    }

    public EventModel(String title, String destination, int budget, String startDate, String endDate) {
        this.title = title;
        this.destination = destination;
        this.budget = budget;
        this.startDate = startDate;
        this.endDate = endDate;
    }


    public EventModel() {
    }

    public int geteID() {
        return eID;
    }

    public int getuID() {
        return uID;
    }

    public String getTitle() {
        return title;
    }

    public String getDestination() {
        return destination;
    }

    public int getBudget() {
        return budget;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
