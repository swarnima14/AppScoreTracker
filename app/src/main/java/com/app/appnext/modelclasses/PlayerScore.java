package com.app.appnext.modelclasses;

public class PlayerScore {

    private String name, status;
    private int runs, wickets, fours, sixes, balls, id;
    private double strikeRate;

    public PlayerScore(int id, String name, String status, int runs, int wickets, int fours, int sixes, int balls, double strikeRate) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.runs = runs;
        this.wickets = wickets;
        this.fours = fours;
        this.sixes = sixes;
        this.balls = balls;
        this.strikeRate = strikeRate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public int getFours() {
        return fours;
    }

    public void setFours(int fours) {
        this.fours = fours;
    }

    public int getSixes() {
        return sixes;
    }

    public void setSixes(int sixes) {
        this.sixes = sixes;
    }

    public int getBalls() {
        return balls;
    }

    public void setBalls(int balls) {
        this.balls = balls;
    }

    public double getStrikeRate() {
        return strikeRate;
    }

    public void setStrikeRate(double strikeRate) {
        this.strikeRate = strikeRate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
