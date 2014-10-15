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
public class fragMap extends Fragment {

    // overload onAttach
    public void onAttach(Activity activity)
    {
        // call the regular one
        super.onAttach(activity);
        // set up the listener
        listener = (mapListener)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.map, container, false);

        // get buttons
        Button viewmsg = (Button) root.findViewById(R.id.btnViewMsg);
        Button newmsg = (Button) root.findViewById(R.id.btnNew);

        // set listeners
        // TODO: actually set these up
        viewmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // goes to message
                listener.openMsg();
            }
        });
        newmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // opens "new message" in message format
                listener.openNew(0);
            }
        });


        return root;
    }

    // for interacting with the parent activity
    public interface mapListener{
        public void openMsg();
        public void openNew(int type);
    }
    private mapListener listener;

}