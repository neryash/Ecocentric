package com.teamducky.ecocentric;

public class Task {

    private int goal;
    private String type;
    private int points;
    private boolean isActive;

    public Task(int goal, String type, int points,boolean isActive) {
        this.goal = goal;
        this.type = type;
        this.points = points;
        this.isActive = isActive;
    }

    public String toTaskString(){
        String msg = "";
        switch (type){
            case "cycling":
                msg =  "Cycle for "+goal+" km.";
                break;
            case "walking":
                msg =  "Walk "+goal+" km.";
                break;
            case "running":
                msg = "Run for "+goal+" km.";
                break;
            default:
                msg = type;
        }
        return msg;
    }

    public int getPoints() {
        return points;
    }

    public boolean isActive() {
        return isActive;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
