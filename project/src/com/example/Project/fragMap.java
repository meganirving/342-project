package com.example.Project;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Megan on 15/10/2014.
 */
public class fragMap extends Fragment {

    ArrayList<Message> msgs;

    // creating a new message via the action bar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.actionbar, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_new:
                listener.openNew();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

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

        setHasOptionsMenu(true);

        // create temp list
        final ListView list = (ListView) root.findViewById(R.id.list);
        ArrayAdapter<Message> adapter = new ArrayAdapter<Message>(getActivity(), android.R.layout.simple_list_item_1, msgs);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Message temp = (Message) parent.getItemAtPosition(position);
                listener.openMsg(temp);
            }
        });

        return root;
    }

    public void setMsgs(ArrayList<Message> newMsgs) {
        msgs = newMsgs;
    }

    // for interacting with the parent activity
    public interface mapListener{
        public void openMsg(Message msg);
        public void openNew();
    }
    private mapListener listener;

}