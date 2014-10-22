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
public class fragMsg extends Fragment {

    private User user;
    private Message msg;

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
        Button pos = (Button) root.findViewById(R.id.btnPos);
        Button neg = (Button) root.findViewById(R.id.btnNeg);

        // set text
        TextView messageLabel = (TextView) root.findViewById(R.id.txtMsg);
        messageLabel.setText(msg.getMessage());

        // set listeners
        pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the user's current vote on this message
                int vote = user.getVote(msg.getID());
                if (vote == 0) {
                    // add vote
                    user.addVote(msg.getID(), 1);
                    listener.voteOnMsg(true, msg);
                } else if (vote == -1) {
                    // update vote
                    user.changeVote(msg.getID(), 1);
                    listener.voteOnMsg(true, msg);
                }
            }
        });
        neg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the user's current vote on this message
                int vote = user.getVote(msg.getID());
                // if they haven't voted, or only voted positively
                if (vote == 0) {
                    // add vote
                    user.addVote(msg.getID(), -1);
                    listener.voteOnMsg(false, msg);
                } else if (vote == 1) {
                    // update vote
                    user.changeVote(msg.getID(), -1);
                    listener.voteOnMsg(false, msg);
                }
            }
        });

        return root;
    }

    // sets the user and message data
    public void setData(User newUser, Message newMsg) {
        user = newUser;
        msg = newMsg;
    }

    // for interacting with the parent activity
    public interface msgListener{
        public void voteOnMsg(boolean vote, Message msg);
    }
    private msgListener listener;
}