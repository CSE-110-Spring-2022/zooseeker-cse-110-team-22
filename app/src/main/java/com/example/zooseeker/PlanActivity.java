package com.example.zooseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
        direction_list = zGraph.getShortestPath(id_list,MainActivity.exhibitManager.getClosest(MainActivity.locationModel.getLat(),MainActivity.locationModel.getLng() ) );
        Log.i("list", direction_list.toString());

        // get current location
        double curr_lat = MainActivity.locationModel.last_known_lat;
        double curr_lng = MainActivity.locationModel.last_known_lng;
        Log.d("curr_lat", String.valueOf(curr_lat));
        Log.d("curr_lng", String.valueOf(curr_lng));

        // get order of exhibits
        List<String> pathOrder = zGraph.getShortestPathOrder(id_list,MainActivity.exhibitManager.getClosest(MainActivity.locationModel.getLat(),MainActivity.locationModel.getLng() ) );
        Log.i("path", pathOrder.toString());
        List<String> exhibits = RoutePlanSummary.getExhibitsDistances(exhibit_info, id_list, pathOrder, curr_lat, curr_lng);
        route_plan_summary = exhibits;

        myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, route_plan_summary);
        listPlan.setAdapter(myAdapter);
    }

    public void openDirections(View view){
        Intent intent = new Intent(this, DirectionActivity.class);
        startActivity(intent);
    }
}