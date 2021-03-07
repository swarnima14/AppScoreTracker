package com.app.appnext.modelclasses;

public class ModelView {

    private String nameA;
    private String nameB;
    private String date;


    public ModelView(String nameA, String nameB, String date) {
        this.nameA = nameA;
        this.nameB = nameB;
        this.date = date;
    }

    public String getNameA() {
        return nameA;
    }

    public void setNameA(String nameA) {
        this.nameA = nameA;
    }

    public String getNameB() {
        return nameB;
    }

    public void setNameB(String nameB) {
        this.nameB = nameB;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
