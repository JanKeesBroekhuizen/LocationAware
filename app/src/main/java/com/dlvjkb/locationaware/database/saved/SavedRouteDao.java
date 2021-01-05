package com.dlvjkb.locationaware.database.saved;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.dlvjkb.locationaware.database.preset.Preset_Location;
import com.dlvjkb.locationaware.database.preset.Preset_Route;

import java.util.List;

@Dao
public interface SavedRouteDao {
    @Query("SELECT * FROM Saved_Route")
    List<Saved_Route> getAllRoutes();

    @Query("SELECT * FROM Saved_Route WHERE ID = :routeID")
    Saved_Route getRoute(int routeID);

    @Insert
    void insert (Saved_Route route);
}
