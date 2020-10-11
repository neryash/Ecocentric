package com.teamducky.ecocentric;

import java.util.ArrayList;

public class Session {

    private double time_walked,time_cycled,time_ran, distance_walked, distance_cycled, distance_ran;
    private long sessionWriteTime;
    private double score = 0.;

    public Session(double time_walked, double time_cycled, double time_ran, double distance_walked,
                   double distance_cycled, double distance_ran,long sessionWriteTime) {
        this.time_walked = time_walked;
        this.time_cycled = time_cycled;
        this.time_ran = time_ran;
        this.distance_walked = distance_walked;
        this.distance_cycled = distance_cycled;
        this.distance_ran = distance_ran;
        this.sessionWriteTime = sessionWriteTime;

        calcScore();
    }
    public double sumTime(){
        return this.time_cycled + this.time_ran + this.time_walked;
    }
    public double sumDist(){
        return this.distance_cycled + this.distance_ran + this.distance_walked;
    }
    public double getTime_walked() {
        return time_walked;
    }

    public double getTime_cycled() {
        return time_cycled;
    }

    public double getTime_ran() {
        return time_ran;
    }

    public double getDistance_walked() {
        return distance_walked;
    }

    public double getDistance_cycled() {
        return distance_cycled;
    }

    public double getDistance_ran() {
        return distance_ran;
    }

    public long getSessionWriteTime() {
        return sessionWriteTime;
    }

    public double getScore() {
        return score;
    }

    public ArrayList<String> getActivities(){
        ArrayList<String> allActivities = new ArrayList<>();
        if(this.getDistance_cycled() > 10){
            allActivities.add("Cycling");
        }
        if(this.getDistance_ran() > 10){
            allActivities.add("Running");
        }
        if(this.getDistance_walked() > 10){
            allActivities.add("Walking");
        }
        return allActivities;
    }

    public double calcScore(){
        double finalScore = score = Math.floor(Math.min(0.2*time_walked*distance_walked + 0.0555*time_cycled*
                distance_cycled + 0.103*distance_ran*time_ran, 200.));
        return finalScore;
    }
}
