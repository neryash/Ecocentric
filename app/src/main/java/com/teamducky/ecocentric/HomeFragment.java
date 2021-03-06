package com.teamducky.ecocentric;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class HomeFragment extends Fragment {

    private TextView welcomeTextView, quotesTxt, stepsData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        InputStream is = this.getResources().openRawResource(R.raw.quotes);
        byte[] buffer = new byte[0];
        try {
            buffer = new byte[is.available()];
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                if (!(is.read(buffer) != -1)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        stepsData = getView().findViewById(R.id.stepsData);
        SharedPreferences prfs = getActivity().getSharedPreferences("sportsData", Context.MODE_PRIVATE);
        int allSteps = prfs.getInt("steps", 0);
        stepsData.setText("Steps: " + allSteps);
        String jsontext = new String(buffer);
        quotesTxt = getView().findViewById(R.id.quoteTxt);
        ArrayList<String> quotes = new ArrayList<>();
        quotes = new ArrayList<String>(Arrays.asList(jsontext.split("\n")));
        boolean foundQuote = false;
        String quote = "";
        while (!foundQuote){
            int rnd = new Random().nextInt(quotes.size());
            quote = quotes.get(rnd);
            quotesTxt.setText(quote);
            if((quote.split(" ").length) < 20){
                foundQuote = true;
            }
        }
        welcomeTextView = getView().findViewById(R.id.welcomeTxt);
        welcomeTextView.setText("Welcome Back, " + ParseUser.getCurrentUser().getUsername());
        welcomeTextView = getView().findViewById(R.id.totalPoints);
        welcomeTextView.setText("Points: " + ParseUser.getCurrentUser().get("points"));
        getView().findViewById(R.id.refreshBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotate_Clockwise(getView().findViewById(R.id.refreshBtn));
                welcomeTextView = getView().findViewById(R.id.totalPoints);
                try {
                    ParseUser.getCurrentUser().fetch();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                welcomeTextView.setText("Points: " + ParseUser.getCurrentUser().get("points"));
                SharedPreferences prfs = getActivity().getSharedPreferences("sportsData", Context.MODE_PRIVATE);
                int allSteps = prfs.getInt("steps", 0);
                stepsData.setText("Steps: " + allSteps);

                sendUpdated();
            }
        });
    }
    public void rotate_Clockwise(View view) {
        ObjectAnimator rotate = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
        rotate.setDuration(500);
        rotate.start();
    }
    private void sendUpdated() {
        Intent intent = new Intent("updated");
        intent.putExtra("message", "updated");
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }
}