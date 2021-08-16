package com.teamducky.ecocentric;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
// Home activity file
public class HomeActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    private BroadcastReceiver mBroadcastReceiver;
    private TextView allPointsTxt;
    private int allPoints;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private BroadcastReceiver mMessageReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }
        allPointsTxt = findViewById(R.id.allPointsDisplay);
        if(ParseUser.getCurrentUser().get("points") == null){
            ParseUser.logOutInBackground();
        }
        try {
            ParseUser.getCurrentUser().fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            requestLocationPermission();
        }else{
            if(!checkServiceRunning(StatsService.class)){
                Intent intent = new Intent(HomeActivity.this,StatsService.class);
                startService(intent);
            }
        }
        try {
            ParseUser.getCurrentUser().fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        allPointsTxt.setText(ParseUser.getCurrentUser().get("points")+"");
        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    ParseUser.getCurrentUser().fetch();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                allPointsTxt.setText(ParseUser.getCurrentUser().get("points")+"");
            }
        };

    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    public boolean checkServiceRunning(Class<?> serviceClass){
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if (serviceClass.getName().equals(service.service.getClassName()))
            {
                return true;
            }
        }
        return false;
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_activity:
                            selectedFragment = new ActivityFragment();
                            break;
                        case R.id.nav_rewards:
                            selectedFragment = new RewardsFragment();
                            break;
                        case R.id.nav_mindfulness:
                            selectedFragment = new MindfulFragment();
                            break;
                        case R.id.nav_profile:
                            selectedFragment = new ProfileFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };
    private final int REQUEST_LOCATION_PERMISSION = 1;
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if(EasyPermissions.hasPermissions(this, perms)) {
            if(!checkServiceRunning(StatsService.class)){
                Intent intent = new Intent(HomeActivity.this,StatsService.class);
                startService(intent);
            }
        }
        else {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mMessageReceiver,
                        new IntentFilter("update"));

    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(mMessageReceiver);
    }
}
