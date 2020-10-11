package com.teamducky.ecocentric;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


public class ActivityFragment extends Fragment {

    private TextView allSessions;
    private ArrayList<String> sessions;
    private LinearLayout linearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_activity, container, false);
        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        linearLayout = getView().findViewById(R.id.activityLayouts);
        SharedPreferences prfs = getActivity().getSharedPreferences("sportsData", Context.MODE_PRIVATE);
        String data = prfs.getString("AllActivities","");
        Gson gson = new Gson();
        if(data.trim().length() > 0){
            getView().findViewById(R.id.noActivities).setVisibility(View.GONE);
            sessions = gson.fromJson(data, new TypeToken<List<String>>(){}.getType());
            final TextView[] tv = new TextView[sessions.size()];
            for(int i = sessions.size()-1; i >= 0; i--){
                Session session = gson.fromJson(sessions.get(i),Session.class);
                tv[i] = new TextView(getContext());
                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams
                        ((int)LinearLayout.LayoutParams.MATCH_PARENT,(int) LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(toDp(32),toDp(16),toDp(32),toDp(16));
                params.topMargin  = 20;
                tv[i].setText("Session time: " + session.sumTime() + "s\nPoints awarded: " + session.calcScore() + "\nSession distance: " + session.sumDist() + "m\nSession activities: " + joinArrayList(session.getActivities()) + "\nSession date: " + new java.util.Date((long)session.getSessionWriteTime()*1000));
                tv[i].setTextSize((float) 14);
                tv[i].setBackground(getActivity().getDrawable(R.drawable.txt_back));

                tv[i].setLayoutParams(params);
                linearLayout.addView(tv[i]);
            }
        }else{
            getView().findViewById(R.id.noActivities).setVisibility(View.VISIBLE);
        }
        getView().findViewById(R.id.refreshSessions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotate_Clockwise(getView().findViewById(R.id.refreshSessions));
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ActivityFragment()).commit();
            }
        });

    }
    private String joinArrayList(ArrayList<String> list){
        String finalString = "";
        for (int i = 0; i < list.size();i++)
        {
            if(i < list.size()-1){
                finalString += list.get(i) + ", ";
            }else{
                finalString += list.get(i);
            }
        }
        return finalString;
    }

    private int toDp(int pxpx){
        Resources r = getContext().getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                pxpx,
                r.getDisplayMetrics()
        );
        return px;
    }
    public void rotate_Clockwise(View view) {
        ObjectAnimator rotate = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
        rotate.setDuration(500);
        rotate.start();
    }
}