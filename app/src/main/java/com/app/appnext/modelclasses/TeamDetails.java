package com.app.appnext.modelclasses;

public class TeamDetails {

    private String name, toss, choose;
    private int runs;
    private int wickets;
    private int id;
    private int totalOvers;
    private double currentOvers;

    public TeamDetails(int id, String name, String toss, String choose, int runs, int wickets, int totalOvers, double currentOvers) {
        this.name = name;
        this.toss = toss;
        this.choose = choose;
        this.runs = runs;
        this.wickets = wickets;
        this.id = id;
        this.currentOvers = currentOvers;
        this.totalOvers = totalOvers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getToss() {
        return toss;
    }

    public void setToss(String toss) {
        this.toss = toss;
    }

    public String getChoose() {
        return choose;
    }

    public void setChoose(String choose) {
        this.choose = choose;
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public int getWickets() {
        return wickets;
    }

    public void setWickets(int wickets) {
        this.wickets = wickets;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotalOvers() {
        return totalOvers;
    }

    public void setTotalOvers(int totalOvers) {
        this.totalOvers = totalOvers;
    }

    public double getCurrentOvers() {
        return currentOvers;
    }

    public void setCurrentOvers(double currentOvers) {
        this.currentOvers = currentOvers;
    }
}
