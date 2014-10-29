package com.example.Project;

import java.util.ArrayList;

/**
 * Created by Megan on 22/10/2014.
 */

public class User {
    private ArrayList<Pair<Integer, Integer>> Votes;
    private ArrayList<Integer> readMsgs;

    public User() {
        Votes = new ArrayList<Pair<Integer, Integer>>();
        readMsgs = new ArrayList<Integer>();
    }

    // adds a message to the read list if it's not already in it
    public void addMsg(Integer ID)
    {
        if (!foundMsg(ID))
            readMsgs.add(ID);
    }
    private boolean foundMsg(Integer ID)
    {
        // loop through
        for (Integer id : readMsgs)
        {
            // return true early if found
            if (id == ID)
                return true;
        }

        // never found
        return false;
    }


    // add a vote to the list
    public void addVote(Integer ID, Integer vote) {
        Votes.add(new Pair<Integer, Integer>(ID, vote));
    }

    // did the user vote on a specific ID and if so: what did they vote?
    // return vals: -1 = negative vote, 0 = no vote, 1 = positive vote
    public int getVote(Integer ID) {
        // loop through the list if it's not empty
        if (!Votes.isEmpty()) {
            for (Pair<Integer, Integer> vote : Votes) {
                // if this is the right vote
                if (vote.getL() == ID) {
                    // return the vote's value (either +1 or -1)
                    return vote.getR();
                }
            }
        }

        // if the user hasn't voted, return 0
        return 0;
    }

    public void changeVote(Integer ID, Integer newVote) {
        // loop through the list
        for (Pair<Integer, Integer> vote : Votes) {
            // if this is the right vote
            if (vote.getL() == ID) {
                // change the vote's value
                vote.setR(newVote);
                return;
            }
        }
    }
}
