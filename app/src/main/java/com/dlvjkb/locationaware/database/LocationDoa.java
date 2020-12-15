package com.dlvjkb.locationaware.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface LocationDoa {
    @Query("SELECT * FROM DB_Location")
    List<DB_Location> getAllLocations();

    @Insert
    void insert (DB_Location location);

    @Insert
    void insertAll(List<DB_Location> locations);
}
