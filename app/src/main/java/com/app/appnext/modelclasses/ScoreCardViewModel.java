package com.app.appnext.modelclasses;

public class ScoreCardViewModel {

    private String name, type;
    private int run, wicket;

    public ScoreCardViewModel(String name, String type, int run, int wicket) {
        this.name = name;
        this.type = type;
        this.run = run;
        this.wicket = wicket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRun() {
        return run;
    }

    public void setRun(int run) {
        this.run = run;
    }

    public int getWicket() {
        return wicket;
    }

    public void setWicket(int wicket) {
        this.wicket = wicket;
    }
}
