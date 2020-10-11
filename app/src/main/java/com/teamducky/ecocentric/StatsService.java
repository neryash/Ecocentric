package com.teamducky.ecocentric;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.ActivityTransition;
import com.google.android.gms.location.ActivityTransitionEvent;
import com.google.android.gms.location.ActivityTransitionRequest;
import com.google.android.gms.location.ActivityTransitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import pub.devrel.easypermissions.EasyPermissions;

import static com.teamducky.ecocentric.App.CHANNEL_ID;

public class StatsService extends Service implements SensorEventListener{
    @Override
    public void onCreate() {
        super.onCreate();
    }
    LocationManager locationManager;
    Context mContext;
    ArrayList<Location> locationsArray;
    private int steps;
    SensorManager sensorManager;
    Sensor sSensor;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    BroadcastReceiver broadcastReceiver;
    String currentActivity = "still";
    Location lastLoc = null;
    int timeCycled=0,timeWalked=0,timeRan=0,distanceWalked=0,distanceCycles=0,distanceRan=0;
    ArrayList<String> sessions;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Ecocentric")
                .setContentText("Ecocentric is measuring vital data")
                .setSmallIcon(R.drawable.ic_outline_map_24)
                .setContentIntent(pendingIntent)
                .build();
        SharedPreferences prfs = StatsService.this.getSharedPreferences("sportsData", Context.MODE_PRIVATE);
        int allSteps = prfs.getInt("steps", 0);
        steps = allSteps;
        preferences = getSharedPreferences("sportsData", Context.MODE_PRIVATE);
        editor = preferences.edit();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        sensorManager.registerListener(this, sSensor, SensorManager.SENSOR_DELAY_NORMAL);
        startForeground(1, notification);
        String data = prfs.getString("AllActivities","");
        Gson gson = new Gson();
        sessions = gson.fromJson(data, new TypeToken<List<String>>(){}.getType());
        if(sessions == null || sessions.size() == 0){
            sessions = new ArrayList<>();
        }
        mContext = this;
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                0,
                10, locationListenerGPS);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constants.BROADCAST_DETECTED_ACTIVITY)) {
                    int type = intent.getIntExtra("type", -1);
                    int confidence = intent.getIntExtra("confidence", 0);
                    handleUserActivity(type, confidence);
                }
            }
        };
        startTracking();

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Constants.BROADCAST_DETECTED_ACTIVITY));

        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                if(!currentActivity.equals("still")) {
                    switch (currentActivity) {
                        case "walking":
                            timeWalked += 5;
                            break;
                        case "running":
                            timeRan += 5;
                            break;
                        case "cycling":
                            timeCycled += 5;
                            break;
                    }
                }else{

                }
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, new Date(), 5000);
        return START_NOT_STICKY;
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;

        if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            steps++;
            editor.putInt("steps",(int) steps);
            editor.apply();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    LocationListener locationListenerGPS=new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            if(!currentActivity.equals("still")) {
                if(lastLoc == null){
                    lastLoc = location;
                }
                float dist = location.distanceTo(lastLoc);
                lastLoc = location;
                switch (currentActivity) {
                    case "walking":
                        distanceWalked += dist;
                        break;
                    case "running":
                        distanceRan += dist;
                        break;
                    case "cycling":
                        distanceCycles += dist;
                        break;
                }
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private void startTracking() {
        Intent intent = new Intent(StatsService.this, BackgroundDetectedActivitiesService.class);
        startService(intent);
    }

    private void stopTracking() {
        Intent intent = new Intent(StatsService.this, BackgroundDetectedActivitiesService.class);
        stopService(intent);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void handleUserActivity(int type, int confidence) {
        String label = "UNKNOWN";
        String currentActivityBefore = "still";
        switch (type) {
            case DetectedActivity.IN_VEHICLE: {
                label = ("IN_VEHICLE");
                currentActivityBefore = "still";
                break;
            }
            case DetectedActivity.ON_BICYCLE: {
                label = ("ON_BICYCLE");
                currentActivityBefore = "cycling";
                break;
            }
            case DetectedActivity.ON_FOOT: {
                label = ("ON_FOOT");
                currentActivityBefore = "walking";
                break;
            }
            case DetectedActivity.RUNNING: {
                label = ("RUNNING");
                currentActivityBefore = "running";
                break;
            }
            case DetectedActivity.STILL: {
                label = ("STILL");
                currentActivityBefore = "still";
                break;
            }
            case DetectedActivity.TILTING: {
                //label = ("TILTING");
                currentActivityBefore= "walking";
                break;
            }
            case DetectedActivity.WALKING: {
                label = ("WALKING");
                currentActivityBefore = "walking";
                break;
            }
            case DetectedActivity.UNKNOWN: {
                label = ("UNKNOWN");
                currentActivityBefore = "unknown";
                break;
            }
        }

        if (confidence > Constants.CONFIDENCE) {
            currentActivity = currentActivityBefore;
        }
        if(currentActivity.equals("still")){
            //if((timeWalked > 90 && distanceWalked > 40) || (timeCycled > 90 && distanceCycles > 40) || (timeRan > 90 && distanceRan > 40)){
            //if(timeWalked > 90 && timeCycled > 90 && timeRan > 90 && distanceWalked > 40 && distanceCycles > 40 && distanceRan > 40){
                long unixTime = System.currentTimeMillis() / 1000L;
                Session session = new Session(timeWalked,timeCycled,timeRan,distanceWalked,distanceCycles,distanceRan,unixTime);
                if(session.sumDist() > 40 && session.sumTime() > 90){
                    Gson gson = new Gson();
                    String json = gson.toJson(session);
                    sessions.add(json);
                    String allJsons = gson.toJson(sessions);
                    editor.putString("AllActivities",allJsons);
                    editor.commit();
                    timeCycled=0;
                    timeWalked=0;
                    timeRan=0;
                    distanceWalked=0;
                    distanceCycles=0;
                    distanceRan=0;
                }
            //}
        }
    }
}
