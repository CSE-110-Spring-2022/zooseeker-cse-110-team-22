package com.example.zooseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlanActivity extends AppCompatActivity {
    ArrayAdapter<String> myAdapter;
    public static List<String> route_plan_summary;
    public static List<String> direction_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        //Create ListView
        ListView listPlan = ((ListView) findViewById(R.id.listPlan));
        //Load graph
        ZooGraph zGraph = new ZooGraph(this);

        //Convert list of names to list of ids
        List<String> id_list = MainActivity.planList.stream()
                .map(name -> ExhibitManager.nameToExhibit.get(name))
                .map(exhibit -> exhibit.hasGroup() ? exhibit.groupId : exhibit.id)
                .collect(Collectors.toList());

        // Get list of directions
        direction_list = zGraph.getShortestPath(id_list);
        Log.i("list", direction_list.toString());

        // get order of exhibits
        route_plan_summary = zGraph.getShortestPathOrder(id_list);
        List<String> exhibits = new ArrayList<String>();
        for (int i = 0; i < route_plan_summary.size(); i++) {
            String s = route_plan_summary.get(i);
            if (id_list.contains(s)) {
                exhibits.add(s);
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