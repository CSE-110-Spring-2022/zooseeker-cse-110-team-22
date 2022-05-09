package com.example.zooseeker;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ZooDatabaseTest {

    //tests that database is properly initialized
    @Test
    public void testSeedDatabase() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

        scenario.onActivity(activity -> {
            ZooDatabase db = Room.inMemoryDatabaseBuilder(activity, ZooDatabase.class)
                    .allowMainThreadQueries()
                    .build();

            ZooExhibitsItemDao dao = db.zooExhibitsItemDao();

            List<ZooExhibitsItem> zooEx = ZooExhibitsItem
                    .loadJSON(activity, "sample_node_info.json");

            dao.insertAll(zooEx);

            //checks that database is populated with proper values
            assert dao.getAll().size() > 0;
            db.close();
        });
    }

    ////tests that values in database can be deleted
    @Test
    public void testDeleteValuesDatabase() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

        scenario.onActivity(activity -> {
            ZooDatabase db = Room.inMemoryDatabaseBuilder(activity, ZooDatabase.class)
                    .allowMainThreadQueries()
                    .build();

            ZooExhibitsItemDao dao = db.zooExhibitsItemDao();

            List<ZooExhibitsItem> zooEx = ZooExhibitsItem
                    .loadJSON(activity, "sample_node_info.json");

            dao.insertAll(zooEx);

            dao.deleteAll();

            //checks that database has no values after deletion
            assert dao.getAll().size() == 0;
            db.close();
        });
    }
}
