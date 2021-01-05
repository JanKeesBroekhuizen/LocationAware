package com.dlvjkb.locationaware.database.preset;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.dlvjkb.locationaware.database.preset.Preset_Location;

import java.util.List;

@Dao
public interface PresetLocationDoa {
    @Query("SELECT * FROM Preset_Location")
    List<Preset_Location> getAllLocations();

    @Insert
    void insert (Preset_Location location);

    @Insert
    void insertAll(List<Preset_Location> locations);
}
