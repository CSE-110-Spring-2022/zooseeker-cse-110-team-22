package com.example.zooseeker;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.concurrent.Executors;

import com.example.zooseeker.Trail;
import com.example.zooseeker.Exhibit;

//to be edited w/ trail and exhibit
@Database(entities = {Exhibit.class, Trail.class}, version = 1)
@TypeConverters({ZooDatabase.Converters.class})
public abstract class ZooDatabase extends RoomDatabase {
    private static ZooDatabase singleton = null;

    private static boolean shouldForcePopulate = false;

    public abstract ExhibitDao exhibitsDao();
    public abstract TrailDao trailsDao();

    /**
     * Use method to get singleton from database class
     * @param context Current state of application
     * @return database of zoo exhibits
     */
    public synchronized static ZooDatabase getSingleton(Context context){
        if (singleton == null){
            singleton = ZooDatabase.makeDatabase(context);
        }
        return singleton;
    }

    /**
     * Use method to generate database from given node file
     * @param context Current state of application
     * @return database of zoo exhibits
     */
    private static ZooDatabase makeDatabase(Context context){
        /*
        return Room.databaseBuilder(context, ZooDatabase.class, "zoo_app.db")
                .allowMainThreadQueries().addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(() -> {
                            Reader inputReader = null;
                            try {
                                inputReader = new InputStreamReader(context.getAssets().open("exhibit_info.json"));
                            } catch (IOException e) {
                                throw new RuntimeException("Unable to load data");
                            }
                            List<Exhibit> zooEx = Exhibit
                                    .fromJson(inputReader);
                            getSingleton(context).exhibitsDao().insert(zooEx);
                        });
                    }
                })
                .build();

         */
        return Room
                .databaseBuilder(context, ZooDatabase.class, "zoo_app.db")
                .allowMainThreadQueries()
                /*
                .addCallback(new Callback() {
                    @Override
                    public void onOpen(@NonNull SupportSQLiteDatabase db) {
                        Executors.newSingleThreadExecutor().execute(() -> {
                            // Don't populate on open unless the database is empty.
                            if (isPopulated() && !shouldForcePopulate) return;
                            Log.i(ZooDatabase.class.getCanonicalName(),
                                    "Database is empty or re-popualtion forced, populating...");
                            populate(context, singleton);
                        });
                    }

                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        Executors.newSingleThreadExecutor().execute(() -> {
                            Log.i(ZooDatabase.class.getCanonicalName(),
                                    "Database is new, populating...");
                            populate(context, singleton);
                        });
                    }
                })

                 */
                .build();
    }

    private static boolean isPopulated() {
        return singleton.exhibitsDao().count() > 0
                && singleton.trailsDao().count() > 0;
    }

    public static void setForcePopulate() {
        shouldForcePopulate = true;
    }

    @VisibleForTesting
    public static void injectTestDatabase(ZooDatabase testDatabase) {
        if (singleton != null) {
            singleton.close();
        }
        singleton = testDatabase;
    }

    public static void populate(Context context, ZooDatabase instance) {
        Reader exhibitsReader = null;
        Reader trailsReader = null;
        try {
            exhibitsReader = new InputStreamReader(context.getAssets().open("exhibit_info.json"));
            trailsReader = new InputStreamReader(context.getAssets().open("trail_info.json"));
        } catch (IOException e) {
            throw new RuntimeException("Unable to load data for prepopulation!");
        }
        populate(context, instance, exhibitsReader, trailsReader);
    }

    @VisibleForTesting
    public static void populate(Context context, ZooDatabase instance, Reader exhibitsReader, Reader trailsReader) {
        Log.i(ZooDatabase.class.getCanonicalName(), "Populating database from assets...");

        // Clear all of the tables
        instance.clearAllTables();
        shouldForcePopulate = false;

        var exhibits = Exhibit.fromJson(exhibitsReader);
        instance.exhibitsDao().insert(exhibits);

        var trails = Trail.fromJson(trailsReader);
        instance.trailsDao().insert(trails);
    }

    public static class Converters {
        @TypeConverter
        public static List<String> fromCsv(String csv) {
            return List.of(",".split(csv));
        }

        @TypeConverter
        public static String toCsv(List<String> tags) {
            return String.join(",", tags);
        }
    }
}