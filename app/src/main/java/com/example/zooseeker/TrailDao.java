package com.example.zooseeker;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import com.example.zooseeker.Trail;

@Dao
public interface TrailDao {
    @Insert
    void insert(Trail... trail);

    @Insert
    void insert(List<Trail> trails);

    @Query("SELECT COUNT(*) from trails")
    long count();
}
