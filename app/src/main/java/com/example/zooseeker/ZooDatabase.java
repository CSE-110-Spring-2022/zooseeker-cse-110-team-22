package com.example.zooseeker;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {ZooExhibitsItem.class}, version = 1)
public abstract class ZooDatabase extends RoomDatabase {
    private static ZooDatabase singleton = null;

    public abstract ZooExhibitsItemDao zooExhibitsItemDao();

    public synchronized static ZooDatabase getSingleton(Context context){
        if (singleton == null){
            singleton = ZooDatabase.makeDatabase(context);
        }
        return singleton;
    }

    private static ZooDatabase makeDatabase(Context context){
        return Room.databaseBuilder(context, ZooDatabase.class, "zoo_app.db")
                .allowMainThreadQueries().addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(() -> {
                            List<ZooExhibitsItem> zooEx = ZooExhibitsItem
                                    .loadJSON(context, "sample_node_info.json");
                            getSingleton(context).zooExhibitsItemDao().insertAll(zooEx);
                        });
                    }
                })
                .build();
    }

    @VisibleForTesting
    public static void injectTestDatabase(ZooDatabase testDatabase) {
        if (singleton != null) {
            singleton.close();
        }
        singleton = testDatabase;
    }
}
