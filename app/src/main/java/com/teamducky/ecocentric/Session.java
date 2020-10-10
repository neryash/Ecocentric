package com.teamducky.ecocentric;

public class Session {
    float time_walked,time_cycled,time_ran,distance,avg_speed_walk,avg_speed_run,avg_speed_cycled,steps_walked;

    public Session(float time_walked, float time_cycled, float time_ran, float distance, float avg_speed_walk, float avg_speed_run, float avg_speed_cycled, float steps_walked) {
        this.time_walked = time_walked;
        this.time_cycled = time_cycled;
        this.time_ran = time_ran;
        this.distance = distance;
        this.avg_speed_walk = avg_speed_walk;
        this.avg_speed_run = avg_speed_run;
        this.avg_speed_cycled = avg_speed_cycled;
        this.steps_walked = steps_walked;
    }
}
