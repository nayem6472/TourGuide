package com.nayem.tourguide.model;

public class ExpenseModel {
    private int exID;
    private int eID;
    private String title;
    private int amount;
    private String date;

    public ExpenseModel(int exID, int eID, String title, int amount, String date) {
        this.exID = exID;
        this.eID = eID;
        this.title = title;
        this.amount = amount;
        this.date = date;
    }

    public ExpenseModel(String title, int amount, String date) {
        this.title = title;
        this.amount = amount;
        this.date = date;
    }

    public ExpenseModel(int exID, String title, int amount, String date) {
        this.exID = exID;
        this.title = title;
        this.amount = amount;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public int getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }
}
