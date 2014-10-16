package com.example.Project;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Megan on 15/10/2014.
 */
public class fragLoad extends Fragment {

    // overload onAttach
    public void onAttach(Activity activity)
    {
        // call the regular one
        super.onAttach(activity);
        // set up the listener
        listener = (loadListener)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.load, container, false);

        listener.started();

        return root;
    }

    // for interacting with the parent activity
    public interface loadListener{
        public void started();
    }
    private loadListener listener;
}