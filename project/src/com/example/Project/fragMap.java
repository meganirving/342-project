package com.example.Project;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
<<<<<<< HEAD
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
=======
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
>>>>>>> 80cf16ce4c678b52a40d13891a78b62c9b573a02

/**
 * Created by Megan on 15/10/2014.
 */
public class fragMap extends Fragment {
    boolean onOpen = true;

<<<<<<< HEAD
    LocationManager locationManager;
    LocationListener locationListener;

    GoogleMap map;

     // overload onAttach
=======
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
>>>>>>> 80cf16ce4c678b52a40d13891a78b62c9b573a02
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

<<<<<<< HEAD
        int available = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());

        if(available!= ConnectionResult.SUCCESS){ // Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(available, getActivity(), requestCode);
            dialog.show();

        }else { // Services are available

             map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                    .getMap();

             map.setMyLocationEnabled(true);
             map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                @Override
                public void onMyLocationChange(Location location) {
                    LatLng position = new LatLng(location.getLatitude(), location.getLongitude());

                    //Zoom parameter is set to 14
                    map.clear();
                    if(onOpen == true) {
                        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(position, 16);
                        map.moveCamera(update);
                        onOpen = false;
                    }
                    Circle circle = map.addCircle(new CircleOptions()
                            .center(new LatLng(location.getLatitude(), location.getLongitude()))
                            .radius(125)
                            .strokeColor(Color.BLUE)
                            .strokeWidth(3)
                            .fillColor(0x3399FFFF));
                }
            });

        }
=======
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
>>>>>>> 80cf16ce4c678b52a40d13891a78b62c9b573a02

        return root;
    }
<<<<<<< HEAD
=======

    public void setMsgs(ArrayList<Message> newMsgs) {
        msgs = newMsgs;
    }

>>>>>>> 80cf16ce4c678b52a40d13891a78b62c9b573a02
    // for interacting with the parent activity
    public interface mapListener{
        public void openMsg(Message msg);
        public void openNew();
    }
    private mapListener listener;

}