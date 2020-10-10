package com.teamducky.ecocentric;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;

import static com.teamducky.ecocentric.App.CHANNEL_ID;

public class StatsService extends Service implements SensorEventListener {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    LocationManager locationManager;
    Context mContext;
    ArrayList<Location> locationsArray;
    private long steps = 0;
    SensorManager sensorManager;
    Sensor sSensor;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

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
        preferences = getSharedPreferences("sportsData", Context.MODE_PRIVATE);
        editor = preferences.edit();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sSensor= sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        sensorManager.registerListener(this,sSensor,SensorManager.SENSOR_DELAY_NORMAL);
        startForeground(1, notification);
        mContext=this;
        locationManager=(LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,
                0,
                10, locationListenerGPS);

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
            double latitude=location.getLatitude();
            double longitude=location.getLongitude();
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
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
