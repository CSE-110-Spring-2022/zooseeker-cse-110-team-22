package com.example.zooseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DirectionActivity extends AppCompatActivity {
    int direction_no;
    TextView directions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);
        direction_no = 0;
        directions = (TextView)findViewById(R.id.directions);
        directions.setText(PlanActivity.direction_list.get(direction_no));
    }

    public void getNext(View view){
        direction_no += 1;
        if (direction_no < PlanActivity.direction_list.size()){
            directions.setText(PlanActivity.direction_list.get(direction_no));
        }
    }
}