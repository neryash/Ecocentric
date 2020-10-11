package com.teamducky.ecocentric;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

public class GoalsFragment extends Fragment {

    Task[] chosenTasks = new Task[3];

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_goal_choice, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText running = (EditText) getView().findViewById(R.id.editTextTextPersonName);
        int numOfKm = Integer.parseInt(running.getText().toString());
        int score = numOfKm*(60/5);
        chosenTasks[0] = new Task(numOfKm, "running", score);

        EditText cycling = getView().findViewById(R.id.editTextTextPersonName1);
        numOfKm = Integer.parseInt(cycling.getText().toString());
        score = numOfKm*(60/5);
        chosenTasks[1] = new Task(numOfKm, "cycling", score);

        EditText walking = getView().findViewById(R.id.editTextTextPersonName1);
        numOfKm = Integer.parseInt(walking.getText().toString());
        score = numOfKm*(60/5);
        chosenTasks[1] = new Task(numOfKm, "walking", score);

    }

    public Task[] getChosenTasks(){
        return chosenTasks;
    }
}