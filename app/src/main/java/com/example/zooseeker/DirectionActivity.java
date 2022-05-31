package com.example.zooseeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class DirectionActivity extends AppCompatActivity {
    int direction_no;
    ListView directions;
    private ArrayAdapter<String> myAdapter;

    public static List<String> dlist;

    public boolean mockingEnabled = true;
    //Gorilla Coordinates
    public double mockLat = 32.74711745394194;
    public double mockLng = -117.18047982358976;
//    // Koi Coordinates
//    public double mockLat = 32.72109826903826;
//    public double mockLng = -117.15952052282296;

    public AlertDialog alertDialog;

    TextView inp_lat;
    TextView inp_lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);
        createAlert();
        inp_lat = ((TextView) findViewById(R.id.latitude));
        inp_lng= ((TextView) findViewById(R.id.longitude));
        direction_no = 0;
        ListView directions = (ListView) findViewById(R.id.directions);
        String current = PlanActivity.direction_list.get(direction_no);
        Log.d("DIR_LIST_CURR", current);
        //I HARD CODED THIS I HAVE NO IDEA HOW TO GET CURRENT DIRECTIONS lat n lng are zoo starts
        dlist = ZooGraph.getDirectionsToExhibit(MainActivity.locationModel.getLat(), MainActivity.locationModel.getLng(), current);
        myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dlist);
        directions.setAdapter(myAdapter);

        if (!mockingEnabled) {
            var locationListner2 = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    Log.d("Direction Location Changed", String.format("Location changed: %s", location));
                    if (checkOffTrack(location.getLatitude(), location.getLongitude())){
                        Log.d("Off Track", "User off track");
                    }

                }
            };
            //Register Listner
            MainActivity.locationModel.requestLocationUpdates(locationListner2);
        }
        else {
            Log.d("Mocking Location", String.format("Location mocked: %f %f", mockLat, mockLng));
            Log.d("Off track", String.valueOf(checkOffTrack(mockLat, mockLng)));
        }
    }

    public void getNext(View view){
        if (direction_no < PlanActivity.direction_list.size() - 1){
            direction_no += 1;
            ListView directions = (ListView) findViewById(R.id.directions);
            String current = PlanActivity.direction_list.get(direction_no);
            //I HARD CODED THIS I HAVE NO IDEA HOW TO GET CURRENT DIRECTIONS lat n lng are zoo starts
            dlist = ZooGraph.getDirectionsToExhibit(MainActivity.locationModel.getLat(), MainActivity.locationModel.getLng(), current);
            myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dlist);
            directions.setAdapter(myAdapter);
        }
    }

    public void getPrevious(View view){
        if (direction_no > 0){
            direction_no -= 1;
            ListView directions = (ListView) findViewById(R.id.directions);
            String current = PlanActivity.direction_list.get(direction_no);
            //I HARD CODED THIS I HAVE NO IDEA HOW TO GET CURRENT DIRECTIONS lat n lng are zoo starts
            dlist = ZooGraph.getDirectionsToExhibit(MainActivity.locationModel.getLat(), MainActivity.locationModel.getLng(), current);
            myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dlist);
            directions.setAdapter(myAdapter);
        }
    }

    public boolean checkOffTrack(double lat, double lng){
        ExhibitManager exhibitManager = MainActivity.exhibitManager;
        List<String> subList = PlanActivity.direction_list.subList(direction_no + 1, PlanActivity.direction_list.size());
        if (subList.isEmpty()){
            return false;
        }
        String current_string = PlanActivity.direction_list.get(direction_no);
        Exhibit current = exhibitManager.idToExhibit.get(current_string);
        double curr_distance = exhibitManager.getDistanceBetween(current, lat, lng);

        List<Exhibit> remainingExhibit = subList.stream()
                .map(name -> exhibitManager.idToExhibit.get(name))
                .collect(Collectors.toList());



        for (Exhibit ext : remainingExhibit){
            var distance = exhibitManager.getDistanceBetween(ext, lat, lng);
            if (distance < curr_distance){
                Log.d("Closer Exhibit", ext.name);
                alertDialog.show();
                return true;
            }
        }
        return false;
    }


    public void mockNewLocation(View view){
        double textLat = Double.parseDouble(inp_lat.getText().toString());
        double textLng = Double.parseDouble(inp_lng.getText().toString());
        if (mockingEnabled){
            mockLat = textLat;
            mockLng = textLng;
            Log.d("Mocking Location", String.format("Location mocked: %f %f", mockLat, mockLng));
        }
        checkOffTrack(mockLat, mockLng);
    }

    public void createAlert(){
        AlertDialog.Builder builder = new AlertDialog
                .Builder(this);

        // Set the message show for the Alert time
        builder.setMessage("Do you want to exit ?");

        // Set Alert Title
        builder.setTitle("Alert !");

        // Set Cancelable false
        // for when the user clicks on the outside
        // the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name
        // OnClickListener method is use of
        // DialogInterface interface.

        builder
                .setPositiveButton(
                        "Yes",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                // When the user click yes button
                                // then app will close
                                dialog.cancel();
                            }
                        });

        // Set the Negative button with No name
        // OnClickListener method is use
        // of DialogInterface interface.
        builder
                .setNegativeButton(
                        "No",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                // If user click no
                                // then dialog box is canceled.
                                dialog.cancel();
                            }
                        });

        // Create the Alert dialog
        alertDialog = builder.create();
    }
}