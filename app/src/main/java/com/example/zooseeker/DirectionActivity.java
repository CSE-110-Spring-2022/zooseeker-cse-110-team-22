package com.example.zooseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
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

        //https://developer.android.com/reference/android/content/SharedPreferences
        SharedPreferences settings = getPreferences(0);
        if(settings.getInt("directionNum", -1) == -1) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("directionNum", 0);
            editor.commit();
        } else {
            direction_no = settings.getInt("directionNum", 0);
        }

        directions = (TextView)findViewById(R.id.directions);
        directions.setText(PlanActivity.direction_list.get(direction_no));
    }

    public void getNext(View view){
        direction_no += 1;
        SharedPreferences settings = getPreferences(0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("directionNum", direction_no);
        editor.commit();

        if (direction_no < PlanActivity.direction_list.size()){
            directions.setText(PlanActivity.direction_list.get(direction_no));
        }
    }
}