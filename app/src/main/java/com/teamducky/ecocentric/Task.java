package com.teamducky.ecocentric;

public class Task {

    private int goal;
    private String type;
    private int points;

    public Task(int goal, String type, int points) {
        this.goal = goal;
        this.type = type;
        this.points = points;
    }

    public String toString(){
        String msg = "";
        switch (type){
            case "cycling":
                msg =  "Cycle for "+goal+"km.";
                break;
            case "walking":
                msg =  "Walk "+goal+"steps.";
                break;
            case "running":
                msg = "Run for "+goal+"km.";
                break;
            default:
                msg = type;
        }
        return msg;
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
