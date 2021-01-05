package com.dlvjkb.locationaware.database.preset;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.dlvjkb.locationaware.database.preset.Preset_Location;
import com.dlvjkb.locationaware.database.preset.Preset_Location_Route;

import java.util.List;

@Dao
public interface PresetLocationRouteDao {
    @Query("SELECT * FROM Preset_Location_Route")
    List<Preset_Location_Route> getAllLocationRoutes();

    @Query("SELECT * FROM Preset_Location_Route, Preset_Location WHERE Preset_Location_Route.LocationID = Preset_Location.ID AND Preset_Location_Route.RouteID = :routeID")
    List<Preset_Location> getLocations(int routeID);

    @Insert
    void insert (Preset_Location_Route location_route);

    @Insert
    void insertAll(List<Preset_Location_Route> location_routes);
}
