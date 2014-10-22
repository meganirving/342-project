package com.example.Project;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class actMain extends Activity implements GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener, LocationListener,
        fragLoad.loadListener, fragMap.mapListener, fragNew.newmsgListener, fragMsg.msgListener {

    // user and message data
    private ArrayList<Message> messages;
    private User user;

    // fragments
    fragMap mapfragment;
    fragNew newmsgfragment;
    fragMsg msgfragment;

    // loading data
    private int loaded = 0;

    // Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;
    // Update frequency in seconds
    public static final int UPDATE_INTERVAL_IN_SECONDS = 60;
    // Update frequency in milliseconds
    private static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    // The fastest update frequency, in seconds
    private static final int FASTEST_INTERVAL_IN_SECONDS = 5;
    // A fast frequency ceiling in milliseconds
    private static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    // Define an object that holds accuracy and frequency parameters
    LocationRequest mLocationRequest;
    LocationClient mLocationClient;
    Location mCurrentLocation;
    boolean mUpdatesRequested;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // load/get new user
        user = new User();

        // check google play services
        /*if (servicesConnected()) {
            Log.v("debug", "connected to Google Play");
        } else {
            // quit?
            Log.v("debug", "not connected to Google Play");
        }*/

        // inflate the loading page & add to container
        FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
        fragLoad loading = new fragLoad();
        fragTransaction.add(R.id.frame, loading);
        fragTransaction.commit();

        // start loading
        loaded = 0;
        new DownloadMessages().execute();

        // Create the LocationRequest object
       /* mLocationRequest = LocationRequest.create();
        // Use high accuracy
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Set the update interval to 5 seconds
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        // Set the fastest update interval to 1 second
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create a new location client, using the enclosing class to handle callbacks.
        mLocationClient = new LocationClient(this, this, this);
        // Start with updates turned off*/

    }

    // get next id
    public int getNextID() {
        int largest = -1;
        for (Message msg : messages) {
            if (msg.getID() > largest) {
                largest = msg.getID();
            }
        }
        return largest++;
    }

    // fragment swapping
    public void doneLoading() {
        // increment load count
        loaded++;
        // if we're done loading, open the map
        if (loaded == 1) {
            openMap();
        }
    }
    public void openMap() {
        // open the map, but DON'T add the loading screen to the backstack
        fragMap frag = new fragMap();
        // add the messages
        frag.setMsgs(messages);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, frag);
        fragmentTransaction.commit();
    }
    @Override
    public void started(){
        // connect
        //mLocationClient.connect();
    }
    @Override
    public void openMsg(Message msg){
        // create new fragment and set its challenge
        fragMsg frag = new fragMsg();
        // get current message
        frag.setData(user, msg);

        // create transaction and swap new fragment in
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, frag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    @Override
    public void openNew(){
        // create new fragment
        fragNew frag = new fragNew();

        // create transaction and swap new fragment in
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, frag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    // voting on a message
    @Override
    public void voteOnMsg(boolean vote, Message msg) {
        // update score in the user and the message
        if (vote == true) {
            // positive vote
            user.changeVote(msg.getID(), 1);
        } else {
            // negative vote
            user.changeVote(msg.getID(), -1);
        }
        msg.updateScore(vote);


        // TODO save on database

        // show feedback
        Toast.makeText(this, "Vote saved!", Toast.LENGTH_SHORT).show();
    }

    // saving a message
    @Override
    public void saveMsg(String msgtext) {
        // get current location
        //mCurrentLocation = mLocationClient.getLastLocation();

        // create message and fill with data
        Message newmsg = new Message();
        newmsg.setMsg(msgtext);
        newmsg.setScore(0);
        newmsg.setLat(1);
        newmsg.setLong(1);
        newmsg.setID(getNextID());

        // save the message
        new saveMessage().execute(newmsg);

        // go back to the map
        fragMap map = new fragMap();
        map.setMsgs(messages);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, map);
        fragmentTransaction.commit();
    }

    // location stuff
    @Override
    public void onLocationChanged(Location location) {
      // DO SOMETHING??
    }
    @Override
    public void onConnected(Bundle dataBundle) {
        doneLoading();
    }
    @Override
    public void onDisconnected() {
        // do something??
    }

    // online database stuff
    private class DownloadMessages extends AsyncTask<Void, Void, Void> {

        InputStream inputStream = null;
        String result = "";

        @Override
        protected Void doInBackground(Void... v) {
            String url_select = "http://messageapp.netau.net/getAllMsg.php";
            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

            try {
                HttpClient httpClient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost(url_select);
                httpPost.setEntity(new UrlEncodedFormEntity(param));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                // Read content & Log
                inputStream = httpEntity.getContent();
            } catch (UnsupportedEncodingException e1) {
                Log.e("UnsupportedEncodingException", e1.toString());
                e1.printStackTrace();
            } catch (ClientProtocolException e2) {
                Log.e("ClientProtocolException", e2.toString());
                e2.printStackTrace();
            } catch (IllegalStateException e3) {
                Log.e("IllegalStateException", e3.toString());
                e3.printStackTrace();
            } catch (IOException e4) {
                Log.e("IOException", e4.toString());
                e4.printStackTrace();
            }

            // Convert response to string using String Builder
            try {
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
                StringBuilder sBuilder = new StringBuilder();

                String line = null;
                while ((line = bReader.readLine()) != null) {
                    sBuilder.append(line + "\n");
                }

                inputStream.close();
                result = sBuilder.toString();

            } catch (Exception e) {
                Log.e("StringBuilding & BufferedReader", "Error converting result " + e.toString());
            }

            try {
                ArrayList<Message> msgList = new ArrayList<Message>();
                JSONArray jArray = new JSONArray(result);
                // loop through all messages
                for(int i = 0; i < jArray.length(); i++) {

                    // get message and turn it into a YoCatch message
                    JSONObject jObject = jArray.getJSONObject(i);
                    Message temp = new Message();
                    temp.setID(jObject.getInt("ID"));
                    temp.setMsg(jObject.getString("msg"));
                    temp.setLat(jObject.getDouble("lat"));
                    temp.setLong(jObject.getDouble("long"));
                    temp.setScore(jObject.getInt("score"));

                    // add it to the list
                    msgList.add(temp);
                }

                // set the list
                messages = msgList;

            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            }
            return null;
        }
        protected void onPostExecute(Void v) {
            doneLoading();
        }
    }
    private class saveMessage extends AsyncTask <Message, Void, String> {
        @Override protected String doInBackground(Message... msg){
            // get the message to be saved
            Message temp = msg[0];

            String response = "";
            DefaultHttpClient client = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://messageapp.netau.net/addMsg.php");
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>(5);
                params.add(new BasicNameValuePair("ID", Integer.toString(temp.getID())));
                params.add(new BasicNameValuePair("msg", temp.getMessage()));
                params.add(new BasicNameValuePair("lat", Double.toString(temp.getLat())));
                params.add(new BasicNameValuePair("long", Double.toString(temp.getLong())));
                params.add(new BasicNameValuePair("score", Integer.toString(temp.getScore())));
                httpPost.setEntity(new UrlEncodedFormEntity(params));
            } catch (Exception e){
                e.printStackTrace();
            }
            try {
                HttpResponse execute = client.execute(httpPost);
                InputStream content = execute.getEntity().getContent();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null){
                    response += s;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            Log.d("debug", result);
            // show feedback
            Toast.makeText(getApplicationContext(), "Message saved!", Toast.LENGTH_SHORT).show();
        }
    }

    // google play stuff
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

         // Google Play services can resolve some errors it detects.
         // If the error has a resolution, try sending an Intent to
         // start a Google Play services activity that can resolve
         // error.

        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);

                // Thrown if Google Play services canceled the original
                // PendingIntent

            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            Log.v("journey", "help");
        }
    }
    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;
        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }
        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }
        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {
            case CONNECTION_FAILURE_RESOLUTION_REQUEST :

             // If the result code is Activity.RESULT_OK, try
             // to connect again

                switch (resultCode) {
                    case Activity.RESULT_OK :

                     // Try the request again

                        break;
                }
        }
    }
    private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates", "Google Play services is available.");
            // Continue
            return true;
            // Google Play services was not available for some reason
        } else {
            // Get the error code
            int errorCode = resultCode;
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode, this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            // If Google Play services can provide an error dialog, show it
            if (errorDialog != null) {
                errorDialog.show();
            }
            return false;
        }
    }
}
