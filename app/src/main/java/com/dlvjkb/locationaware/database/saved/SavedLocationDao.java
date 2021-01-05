package com.dlvjkb.locationaware.database.saved;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.dlvjkb.locationaware.database.preset.Preset_Location;

import java.util.List;

@Dao
public interface SavedLocationDao {
    @Query("SELECT * FROM Saved_Location")
    List<Saved_Location> getAllLocations();

    @Insert
    void insert (Saved_Location location);
}
