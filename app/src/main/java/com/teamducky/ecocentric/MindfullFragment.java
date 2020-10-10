package com.teamducky.ecocentric;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MindfullFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mindfull, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewGroup layout = (ViewGroup) getView().findViewById(R.id.TaskView);
        Task[] usrTasks = {}; //TODO: Change from manual to user based
        for (Task task :
                usrTasks) {
            LinearLayout taskLayout = new LinearLayout(); //do smth
        }
    }
}