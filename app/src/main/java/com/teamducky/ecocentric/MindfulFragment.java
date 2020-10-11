package com.teamducky.ecocentric;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class MindfulFragment extends Fragment {

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
        Task[] usrTasks = goalsFragment.getChosenTasks();
        for (Task task :
                usrTasks) {
            LinearLayout taskLayout = new LinearLayout(getContext());
            taskLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            taskLayout.setOrientation(LinearLayout.HORIZONTAL);
            layout.addView(taskLayout);

            RadioButton radioButton = new RadioButton(getContext());
            TextView taskText = new TextView(getContext());
//            taskText.setBackgroundColor(Color.parseColor("#d6dcc2")); //TODO: use drawable for text backgroumd, fix marginsת use montserrat font
            taskText.setText(task.toString());

            taskLayout.addView(radioButton);
            taskLayout.addView(taskText);
        }
    }
}