package com.example.zooseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class PlanActivity extends AppCompatActivity {
    ArrayAdapter<String> myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        ListView listPlan = ((ListView) findViewById(R.id.listPlan));
        ZooGraph zGraph = new ZooGraph(this);
        List<String> direction_list = zGraph.getShortestPath(MainActivity.planList);
        myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, direction_list);
        listPlan.setAdapter(myAdapter);
    }
}