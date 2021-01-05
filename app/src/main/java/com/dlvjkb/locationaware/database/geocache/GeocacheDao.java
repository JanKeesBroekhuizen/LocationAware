package com.dlvjkb.locationaware.database.geocache;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.dlvjkb.locationaware.database.geocache.DB_Geocache;

import java.util.List;

@Dao
public interface GeocacheDao {
    @Query("SELECT * FROM DB_Geocache")
    List<DB_Geocache> getAllGeocaches();

    @Insert
    void insert (DB_Geocache Geocache);

    @Insert
    void insertAll(List<DB_Geocache> Geocache);
}
