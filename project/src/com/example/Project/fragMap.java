package com.example.Project;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Megan on 15/10/2014.
 */
public class fragMap extends Fragment {
    boolean onOpen = true;

    LocationManager locationManager;
    LocationListener locationListener;

    GoogleMap map;

     // overload onAttach
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
        setHasOptionsMenu(true);

        // create temp list
        /*final ListView list = (ListView) root.findViewById(R.id.list);
        ArrayAdapter<Message> adapter = new ArrayAdapter<Message>(getActivity(), android.R.layout.simple_list_item_1, msgs);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Message temp = (Message) parent.getItemAtPosition(position);
                listener.openMsg(temp);
            }
        });*/
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