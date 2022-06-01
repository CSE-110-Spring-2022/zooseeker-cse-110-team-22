package com.example.zooseeker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Define array adapter for ListView
    ArrayAdapter<String> myAdapter;
    ArrayAdapter<String> myAdapter2;
    // Define array List for List View data
    ArrayList<String> mylist;
    //Array list for selected exhibits
    public static ArrayList<String> planList;


    //PermissionChecker
    private final PermissionChecker permissionChecker = new PermissionChecker(this);

    //LocationModel
    public static LocationModel locationModel;
    //Mock Loacation
    public boolean mockingEnabled = true;
    public double mockLat = 32.72109826903826;
    public double mockLng = -117.15952052282296;

    //ExhibitManager
    public static ExhibitManager exhibitManager;

    //Current Location
    public static Location current;
    public static final double Zoolat = 32.8801;
    public static final double Zoolong = -117.2340;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialise ListView with id
        ListView list1 = ((ListView) findViewById(R.id.listView1));
        ListView list2 = ((ListView) findViewById(R.id.listView2));
        TextView counter = ((TextView) findViewById(R.id.numberCount));
        TextView loc = ((TextView) findViewById(R.id.location));

        // Add items to Array List
        mylist = new ArrayList<>();
        ExhibitManager.nameToExhibit = new HashMap<String, Exhibit>();

        /*
        //database, should load it in to arrayList
        ZooDatabase zooNodes = ZooDatabase.getSingleton(this);
        ZooExhibitsItemDao dao = zooNodes.zooExhibitsItemDao();
        //attains only exhibits to put into UI View
        List<ZooExhibitsItem> exhibits = dao.getAllType("exhibit");
        for(int i = 0; i < exhibits.size(); i++){
            mylist.add(exhibits.get(i).name);
            nameToId.put(exhibits.get(i).name, exhibits.get(i).id);
        }
         */

        //use database for persistence of user chosen exhibits list

        Reader exhibitsReader = null;
        try {
            exhibitsReader = new InputStreamReader(this.getAssets().open("exhibit_info.json"));
        } catch (IOException e) {
            throw new RuntimeException("Unable to load data");
        }
        //loading exhibits
        List<Exhibit> exhibits = Exhibit.fromJson(exhibitsReader);

        ZooDatabase zooNodes = ZooDatabase.getSingleton(this);
        ExhibitDao dao = zooNodes.exhibitsDao();

        //to edit later in persisting saved trail
        //TrailDao tdao = zooNodes.trailsDao();
        List<Exhibit> planListLoader = dao.getAll();

        Reader exhibitsReader2 = null;
        try {
            exhibitsReader2 = new InputStreamReader(this.getAssets().open("exhibit_info.json"));
        } catch (IOException e) {
            throw new RuntimeException("Unable to load data");
        }
        exhibitManager = new ExhibitManager(exhibitsReader2, mylist);
        planList = new ArrayList<>();

        /* Permissions Setup */
        while (true){
            if (! permissionChecker.ensurePermissions()){
                break;
            }
        };

        locationModel = new LocationModel(this);
        if (! mockingEnabled)
        {
            var locationListner = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    current = location;
                    Log.d("LAB7", String.format("Location changed: %s", location));
                    loc.setText(exhibitManager.getClosest(location.getLatitude(), location.getLongitude()).name);
                    locationModel.setLastKnown(location.getLatitude(), location.getLongitude());

                }
            };
            locationModel.requestLocationUpdates(locationListner);
        }
        else{
            Log.d("Mocking Enabled", "Mocking Enabled");
            locationModel.setLastKnown(mockLat, mockLng);
            loc.setText(exhibitManager.getClosest(mockLat, mockLng).name);
        }

        //Get list of exhibits from dao
        for(int i = 0; i < planListLoader.size(); i++){
            planList.add(planListLoader.get(i).name);
        }
        counter.setText("Exhibits to Visit: " + planList.size());
        // Initialize adapters
        myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mylist);
        myAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, planList);
        //set adapters to respective lists
        list1.setAdapter(myAdapter);
        list2.setAdapter(myAdapter2);
        // Handling click for animals
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Displays "added (animal")
                String animal = ( (TextView) view ).getText().toString();
                if(planList.contains(animal)){ // O(n) search
                    Toast.makeText(MainActivity.this, "Already Added " + ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
                }
                else {
                    planList.add(animal);
                    // O(n) add to saved database, can prob use something else
                    for(int i = 0; i < exhibits.size(); i++){
                        if(exhibits.get(i).name.equals(animal)){
                            dao.insertSingle(exhibits.get(i));
                        }
                    }
                    counter.setText("Exhibits to Visit: " + planList.size());
                    Toast.makeText(MainActivity.this, "Added " + ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
                    myAdapter2.notifyDataSetChanged();
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu with items using MenuInflator
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        // Initialise menu item search bar
        // with id and take its object
        MenuItem searchViewItem
                = menu.findItem(R.id.search_bar);
        SearchView searchView
                = (SearchView) MenuItemCompat
                .getActionView(searchViewItem);

        // attach setOnQueryTextListener
        // to search view defined above
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {

                    // Override onQueryTextSubmit method
                    // which is call
                    // when submitquery is searched

                    @Override
                    public boolean onQueryTextSubmit(String searchQuery) {
                        // If the list contains the search query
                        // than filter the adapter
                        // using the filter method
                        // with the query as its argument
                        if (mylist.contains(searchQuery)) {
                            myAdapter.getFilter().filter(searchQuery);
                        } else {
                            // Search query not found in List View
                            Toast
                                    .makeText(MainActivity.this,
                                            "Query not found",
                                            Toast.LENGTH_LONG)
                                    .show();
                        }
                        return false;
                    }

                    // This method is overridden to filter
                    // the adapter according to a search query
                    // when the user is typing search
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        myAdapter.getFilter().filter(newText);
                        return false;
                    }
                });

        return super.onCreateOptionsMenu(menu);
    }

    public void onLaunchSettingsClicked(MenuItem item) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    //checks for planning
    public void openPlan(View view){
        Intent intent = new Intent(this, PlanActivity.class);
        startActivity(intent);
    }

    public void deleteSavedPlan(View view){
        //for deleting plan
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("directionNum", 0);
        editor.commit();
        recreate();
    }

    public void deleteSavedExhibits(View view){
        //for deleting saved exhibits
        ZooDatabase zooNodes = ZooDatabase.getSingleton(this);
        ExhibitDao dao = zooNodes.exhibitsDao();
        dao.deleteAll();
        recreate();
    }
}