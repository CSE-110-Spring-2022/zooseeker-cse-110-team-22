package com.example.zooseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlanActivity extends AppCompatActivity {
    ArrayAdapter<String> myAdapter;
    public static List<String> route_plan_summary;
    public static List<String> direction_list;
    public static ExhibitManager exhibitManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        // Create ListView
        ListView listPlan = ((ListView) findViewById(R.id.listPlan));
        // Load graph
        ZooGraph zGraph = new ZooGraph(this);
        // load exhibit data
        Map<String,ZooData.VertexInfo> exhibit_info = zGraph.vInfo;
        Log.i("exhibits", exhibit_info.toString());

        //Convert list of names to list of ids
        List<String> id_list = MainActivity.planList.stream()
                .map(name -> ExhibitManager.nameToExhibit.get(name))
                .map(exhibit -> exhibit.hasGroup() ? exhibit.groupId : exhibit.id)
                .collect(Collectors.toList());
        Log.i("id_list", id_list.toString());

        // Get list of directions
        direction_list = zGraph.getShortestPath(id_list);
        Log.i("list", direction_list.toString());

        // get current location
        double curr_lat = MainActivity.locationModel.last_known_lat;
        double curr_lng = MainActivity.locationModel.last_known_lng;
        Log.d("curr_lat", String.valueOf(curr_lat));
        Log.d("curr_lng", String.valueOf(curr_lng));

        // get order of exhibits
        route_plan_summary = zGraph.getShortestPathOrder(id_list);
        List<String> exhibits = new ArrayList<String>();
        for (int i = 0; i < route_plan_summary.size(); i++) {
            String s = route_plan_summary.get(i);
            if (id_list.contains(s)) {
                String name = exhibit_info.get(s).name;
                double ex_lat = exhibit_info.get(s).lat;
                double ex_lng = exhibit_info.get(s).lng;
                float[] distance = new float[1];
                Location.distanceBetween(curr_lat, curr_lng, ex_lat, ex_lng, distance);
                String info_string = String.format("%s - %.1f meters", name, distance[0]);

                exhibits.add(info_string);
            }
        }
        route_plan_summary = exhibits;

        myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, route_plan_summary);
        listPlan.setAdapter(myAdapter);
    }

    public void openDirections(View view){
        Intent intent = new Intent(this, DirectionActivity.class);
        startActivity(intent);
    }
}