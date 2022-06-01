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

import com.example.zooseeker.Exhibit;

//to be edited w/ trail and exhibit
@Database(entities = Exhibit.class, version = 1, exportSchema = false)
@TypeConverters({ZooDatabase.Converters.class})
public abstract class ZooDatabase extends RoomDatabase {
    private static ZooDatabase singleton = null;

    private static boolean shouldForcePopulate = false;

    public abstract ExhibitDao exhibitsDao();

    private static Integer directionNum = 0;

    public static void incrementDirectionNum(){
        directionNum++;
    }

    public static int getDirectionNum(){
        return directionNum;
    }

    public static void resetDirections(){
        directionNum = 0;
    }
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
        return singleton.exhibitsDao().count() > 0;
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