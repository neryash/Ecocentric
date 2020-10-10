package com.teamducky.ecocentric;

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

    private void calcScore(){
        this.score = Math.floor(Math.min(0.2*time_walked*distance_walked + 0.0555*time_cycled*
                distance_cycled + 0.103*distance_ran*time_ran, 200.));
    }
}
