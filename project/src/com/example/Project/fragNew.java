package com.example.Project;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Megan on 15/10/2014.
 */
public class fragNew extends Fragment {

    private View root;
    private EditText msg;

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

<<<<<<< HEAD
        // get buttons
     /*   Button save = (Button) root.findViewById(R.id.btnSave);
        Button cancel = (Button) root.findViewById(R.id.btnCancel);
=======
        // get UI
        msg = (EditText) root.findViewById(R.id.edtMsg);
        Button save = (Button) root.findViewById(R.id.btnSave);
>>>>>>> 80cf16ce4c678b52a40d13891a78b62c9b573a02

        // set listener
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // only save it if there's actually a message
                if (!msg.getText().toString().isEmpty()) {
                    listener.saveMsg(msg.getText().toString());
                }
            }
        });

        // hide keyboard when press enter
        msg.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(msg.getWindowToken(), 0);
                return true;
            }
        });

<<<<<<< HEAD
        // set the label
        setLabel();
*/
=======
>>>>>>> 80cf16ce4c678b52a40d13891a78b62c9b573a02
        return root;
    }

    // for interacting with the parent activity
    public interface newmsgListener{
        public void saveMsg(String msgtext);
    }
    private newmsgListener listener;
}