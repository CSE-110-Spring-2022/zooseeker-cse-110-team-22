package com.example.zooseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;
import java.util.stream.Collectors;

public class PlanActivity extends AppCompatActivity {
    ArrayAdapter<String> myAdapter;
    public static List<String> exhibit_order;
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

        //Get list of directions
        direction_list = zGraph.getShortestPath(id_list);
        myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, direction_list);
        listPlan.setAdapter(myAdapter);
    }

    public void openDirections(View view){
        Intent intent = new Intent(this, DirectionActivity.class);
        startActivity(intent);
    }
}