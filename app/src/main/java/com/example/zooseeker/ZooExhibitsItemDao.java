package com.example.zooseeker;
//import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ZooExhibitsItemDao {
    @Insert
    long insert(ZooExhibitsItem zooExhibitsItem);

    @Query("SELECT * FROM `zoo_exhibits_items` WHERE `number_assign`=:number_assign")
    ZooExhibitsItem get(long number_assign);

    @Query("SELECT * FROM `zoo_exhibits_items` ORDER BY `name`")
    List<ZooExhibitsItem> getAll();

    //to get nodes which are only exhibits, type should be "exhibits"
    @Query("SELECT * FROM `zoo_exhibits_items` WHERE `kind` =:type")
    List<ZooExhibitsItem> getAllType(String type);

    @Update
    int update(ZooExhibitsItem zooExhibitsItem);

    @Delete
    int delete(ZooExhibitsItem zooExhibitsItem);

    //deleting from SQL Database by name
    @Query("DELETE FROM `zoo_exhibits_items` WHERE 'name' =:name")
    void deleteByName(String name);

    @Insert
    List<Long> insertAll(List<ZooExhibitsItem> zooExhibitsItem);

    @Query("SELECT * FROM `zoo_exhibits_items` ORDER BY `kind`")
    LiveData<List<ZooExhibitsItem>> getAllLive();

}
