package com.teamducky.ecocentric;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseException;
import com.parse.ParseUser;

public class HomeActivity extends AppCompatActivity {

    private TextView allPointsTxt;
    private int allPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        //I added this if statement to keep the selected fragment when rotating the device
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
        allPointsTxt.setText((float)ParseUser.getCurrentUser().get("points")+"");
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
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };
}