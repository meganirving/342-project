package com.example.Project;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Megan on 15/10/2014.
 */
public class fragNew extends Fragment {

    private View root;
    private int type; // 0 = new message, 1 = new comment

    // overload onAttach
    public void onAttach(Activity activity)
    {
        // call the regular one
        super.onAttach(activity);
        // set up the listener
        listener = (newmsgListener)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.newmsg, container, false);

        // get buttons
        Button save = (Button) root.findViewById(R.id.btnSave);
        Button cancel = (Button) root.findViewById(R.id.btnCancel);

        // set listeners
        // TODO: actually set these up - they currently both just return to prev page
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        // set the label
        setLabel();

        return root;
    }

    // return to the prev page
    public void goBack(){
        if (type == 0){
            listener.openMap();
        } else {
            // somehow keep track of the current message?
            listener.openMsg();
        }
    }
    public void setType(int newType) {
        type = newType;
    }
    public void setLabel() {

        TextView label = (TextView) root.findViewById(R.id.txtLabel);

        if (type == 0) {
            label.setText("NEW MESSAGE:");
        } else {
            label.setText("NEW COMMENT:");
        }
    }

    // for interacting with the parent activity
    public interface newmsgListener{
        public void openMsg();
        public void openMap();
    }
    private newmsgListener listener;
}