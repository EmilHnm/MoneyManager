package com.example.moneymanager.models;

import java.io.Serializable;

public class Transaction implements Serializable {
    private int id;
    private int categoryId;
    private int money;
    private String date;
    private String note;
    private String with;

    public Transaction(int id, int categoryId, int money, String date, String note, String with) {
        this.id = id;
        this.categoryId = categoryId;
        this.money = money;
        this.date = date;
        this.note = note;
        this.with = with;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getWith() {
        return with;
    }

    public void setWith(String with) {
        this.with = with;
    }
}

