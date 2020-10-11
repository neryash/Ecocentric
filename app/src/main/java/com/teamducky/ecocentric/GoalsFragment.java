package com.teamducky.ecocentric;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class GoalsFragment extends Fragment {

    ArrayList<String> chosenTasks;
    private CheckBox run,walk,cycle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_goal_choice, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prfs = getActivity().getSharedPreferences("sportsData", Context.MODE_PRIVATE);
        final Gson gson = new Gson();
        String data = prfs.getString("goalss","");
        run = getView().findViewById(R.id.runCheck);
        walk = getView().findViewById(R.id.walkCheck);
        cycle = getView().findViewById(R.id.cycCheck);
        final EditText running = getView().findViewById(R.id.editTextTextPersonName);
        final EditText cycling = getView().findViewById(R.id.editTextTextPersonName1);
        final EditText walking = getView().findViewById(R.id.editTextTextPersonName2);
        if(data.trim().length() == 0){
            Log.i("sw","n");
            chosenTasks = new ArrayList<>();
        }else {
            Log.i("sw","y");
            chosenTasks = gson.fromJson(data,new TypeToken<List<String>>(){}.getType());
            Log.i("sww",gson.fromJson((String) chosenTasks.get(0),Task.class).getGoal() + "");
            if(chosenTasks.get(0) != null){
                running.setText(gson.fromJson((String) chosenTasks.get(0),Task.class).getGoal() + "");
            }
            if(chosenTasks.get(1) != null){
                cycling.setText(gson.fromJson((String) chosenTasks.get(1),Task.class).getGoal() + "");
            }
            if(chosenTasks.get(2) != null){
                walking.setText(gson.fromJson((String) chosenTasks.get(2),Task.class).getGoal() + "");
            }
        }
        getView().findViewById(R.id.submitTasks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenTasks.clear();
                int numOfKm = run.isChecked() ? Integer.parseInt(running.getText().toString()) : 0;
                int score = numOfKm*(60/5);
                Gson gson1 = new Gson();
                Task task = new Task(numOfKm, "running", score,run.isChecked());
                chosenTasks.add(0,gson1.toJson(task));

                numOfKm = cycle.isChecked() ? Integer.parseInt(cycling.getText().toString()) : 0;
                score = numOfKm*(60/5);
                task =  new Task(numOfKm, "cycling", score,cycle.isChecked());
                chosenTasks.add(1,gson1.toJson(task));

                numOfKm = walk.isChecked() ? Integer.parseInt(walking.getText().toString()) : 0;
                score = numOfKm*(60/5);
                task = new Task(numOfKm, "walking", score,walk.isChecked());
                chosenTasks.add(2,gson1.toJson(task));
                SharedPreferences preferences = getActivity().getSharedPreferences("sportsData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("goalss",gson1.toJson(chosenTasks));
                editor.commit();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MindfulFragment()).commit();
            }
        });
    }
}