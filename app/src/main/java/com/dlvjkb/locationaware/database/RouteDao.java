package com.dlvjkb.locationaware.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface RouteDao {
    @Query("SELECT * FROM DB_Route")
    List<DB_Route> getAllRoutes();

    @Insert
    void insert (DB_Route route);

    @Insert
    void insertAll(List<DB_Route> routes);
}
