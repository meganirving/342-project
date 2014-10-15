package com.example.Project;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Megan on 15/10/2014.
 */
public class fragMsg extends Fragment {

    // overload onAttach
    public void onAttach(Activity activity)
    {
        // call the regular one
        super.onAttach(activity);
        // set up the listener
        listener = (msgListener)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.msg, container, false);

        // get buttons
        Button comment = (Button) root.findViewById(R.id.btnComm);
        Button back = (Button) root.findViewById(R.id.btnBack);

        // set listeners
        // TODO: actually set these up
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // opens "new message" in comment format
                listener.openNew(1);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // just goes back to map
                listener.openMap();
            }
        });


        return root;
    }

    // for interacting with the parent activity
    public interface msgListener{
        public void openNew(int type);
        public void openMap();
    }
    private msgListener listener;
}