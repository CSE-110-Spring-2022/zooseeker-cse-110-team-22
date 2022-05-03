package com.example.zooseeker;
//import androidx.lifecycle.LiveData;
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

    @Query("SELECT * FROM `zoo_exhibits_items` WHERE `id`=:id")
    ZooExhibitsItem get(long id);

    @Query("SELECT * FROM `zoo_exhibits_items` ORDER BY `name`")
    List<ZooExhibitsItem> getAll();

    //to get nodes which are only exhibits, type should be "exhibits"
    @Query("SELECT * FROM `zoo_exhibits_items` WHERE `kind` =:type")
    List<ZooExhibitsItem> getAllType(String type);

    @Update
    int update(ZooExhibitsItem zooExhibitsItem);

    @Delete
    int delete(ZooExhibitsItem zooExhibitsItem);

    @Insert
    List<Long> insertAll(List<ZooExhibitsItem> zooExhibitsItem);

    //@Query("SELECT * FROM `zoo_exhibits_items` ORDER BY `order`")
    //LiveData<List<ZooExhibitsItem>> getAllLive();

}
