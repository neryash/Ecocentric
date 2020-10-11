package com.teamducky.ecocentric;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MindfulFragment extends Fragment {

    ArrayList<String> allTasks;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mindfull, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewGroup layout = (ViewGroup) getView().findViewById(R.id.TaskView);
        GoalsFragment goalsFragment = new GoalsFragment();

        SharedPreferences prfs = getActivity().getSharedPreferences("sportsData", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String data = prfs.getString("goalss","");
        if(data.trim().length() == 0){
            allTasks = new ArrayList<>();
        }else {
            allTasks = gson.fromJson(data,new TypeToken<List<String>>(){}.getType());
        }
        for (String task : allTasks) {
            Task task1 = gson.fromJson(task,Task.class);
            if(task1.isActive()) {
                LinearLayout taskLayout = new LinearLayout(getContext());
                taskLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                taskLayout.setOrientation(LinearLayout.HORIZONTAL);
                layout.addView(taskLayout);

                RadioButton radioButton = new RadioButton(getContext());
                TextView taskText = new TextView(getContext());
                radioButton.setEnabled(false);
                if (task == null) {
                    taskText.setText("You don't have any tasks!");
                } else {
                    taskText.setText(task1.toTaskString());
                }

                taskLayout.addView(radioButton);
                taskLayout.addView(taskText);
            }
        }

        getView().findViewById(R.id.goToGoals).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new GoalsFragment()).commit();
            }
        });
    }
}