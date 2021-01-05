package com.dlvjkb.locationaware.database.preset;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.dlvjkb.locationaware.database.preset.Preset_Route;

import java.util.List;

@Dao
public interface PresetRouteDao {
    @Query("SELECT * FROM Preset_Route")
    List<Preset_Route> getAllRoutes();

    @Query("SELECT * FROM Preset_Route WHERE ID = :routeID")
    Preset_Route getRoute(int routeID);

    @Insert
    void insert (Preset_Route route);

    @Insert
    void insertAll(List<Preset_Route> routes);
}
