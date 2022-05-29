package com.example.zooseeker;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;

@Entity(tableName = "zoo_exhibits_items")
public class ZooExhibitsItem {

    //to be thrown out, replaced with exhibit and trail
    // 1. Public fields.
    @PrimaryKey(autoGenerate = true)
    public long number_assign;

    @NonNull
    public String id;

    @NonNull
    public String kind;

    @NonNull
    public String name;

    @TypeConverters({Converters.class})
    public List<String> tags;

    @Override
    public String toString() {
        return "ZooExhibitsItem{" +
                "id=" + id +
                ", text='" + kind + '\'' +
                ", completed=" + name +
                ", order=" + String.join(", ", tags) +
                '}';

        //https://stackoverflow.com/questions/599161/best-way-to-convert-an-arraylist-to-a-string
    }

    // 2. Constructor matching fields above.
    ZooExhibitsItem(@NonNull String id, @NonNull String kind, @NonNull String name, List<String> tags) {
        this.id = id;
        this.kind = kind;
        this.name = name;
        this.tags = tags;
    }
    public static List<ZooExhibitsItem> loadJSON(Context context, String path){
        try{
            InputStream input = context.getAssets().open(path);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<ZooExhibitsItem>>(){}.getType();
            return gson.fromJson(reader, type);
        } catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
