package com.example.zooseeker;

import static org.junit.Assert.assertEquals;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class StateOfPlanTests {

    //testing if directionNum is properly updated
    @Test
    public void testUpdatingDirectionNum(){
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

        scenario.onActivity(activity -> {
            ZooDatabase db = Room.inMemoryDatabaseBuilder(activity, ZooDatabase.class)
                    .allowMainThreadQueries()
                    .build();
            db.incrementDirectionNum();
            db.incrementDirectionNum();
            assertEquals(2, db.getDirectionNum());
            db.close();
        });
    }

    public void DeletePlan(){
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

        scenario.onActivity(activity -> {
            ZooDatabase db = Room.inMemoryDatabaseBuilder(activity, ZooDatabase.class)
                    .allowMainThreadQueries()
                    .build();
            db.incrementDirectionNum();
            db.resetDirections();
            assertEquals(0, db.getDirectionNum());
            db.close();
        });
    }
}
