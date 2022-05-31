package com.example.zooseeker;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import com.example.zooseeker.Exhibit;
import com.example.zooseeker.ExhibitWithGroup;

@Dao
public interface ExhibitDao {
    @Insert
    void insert(Exhibit... exhibits);

    @Insert
    void insert(List<Exhibit> exhibits);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertSingle(Exhibit exhibit);

    @Query("SELECT COUNT(*) from exhibits")
    long count();

    //nuke delete
    @Query("DELETE FROM 'exhibits'")
    void deleteAll();

    @Transaction
    @Query("SELECT * FROM exhibits WHERE kind = 'EXHIBIT' ORDER BY name ASC")
    List<ExhibitWithGroup> getExhibitsWithGroups();

    @Query("SELECT * FROM `exhibits` ORDER BY `name`")
    List<Exhibit> getAll();

    @Transaction
    @Query("SELECT * FROM exhibits WHERE kind = 'EXHIBIT' ORDER BY name ASC")
    LiveData<List<ExhibitWithGroup>> getExhibitsWithGroupsLive();

    @Transaction
    @Query("SELECT * FROM exhibits WHERE id=:id")
    ExhibitWithGroup getExhibitWithGroupById(String id);
}