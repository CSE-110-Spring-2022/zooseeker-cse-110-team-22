package com.example.zooseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class DirectionActivity extends AppCompatActivity {
    int direction_no;
    ListView directions;
    private ArrayAdapter<String> myAdapter;

    public static List<String> dlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);
        direction_no = 0;
        ListView directions = (ListView) findViewById(R.id.directions);
        String current = PlanActivity.direction_list.get(direction_no);
        //I HARD CODED THIS I HAVE NO IDEA HOW TO GET CURRENT DIRECTIONS lat n lng are zoo starts
        dlist = ZooGraph.getDirectionsToExhibit(32.8801, -117.2340, current);
        myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dlist);
        directions.setAdapter(myAdapter);
    }

    public void getNext(View view){
        if (direction_no < PlanActivity.direction_list.size() - 1){
            direction_no += 1;
            ListView directions = (ListView) findViewById(R.id.directions);
            String current = PlanActivity.direction_list.get(direction_no);
            //I HARD CODED THIS I HAVE NO IDEA HOW TO GET CURRENT DIRECTIONS lat n lng are zoo starts
            dlist = ZooGraph.getDirectionsToExhibit(32.8801, -117.2340, current);
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
            dlist = ZooGraph.getDirectionsToExhibit(32.8801, -117.2340, current);
            myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dlist);
            directions.setAdapter(myAdapter);
        }
    }
}