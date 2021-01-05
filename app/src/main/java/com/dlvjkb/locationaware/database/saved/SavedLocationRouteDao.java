package com.dlvjkb.locationaware.database.saved;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.dlvjkb.locationaware.database.preset.Preset_Location;
import com.dlvjkb.locationaware.database.preset.Preset_Location_Route;

import java.util.List;

@Dao
public interface SavedLocationRouteDao {
    @Query("SELECT * FROM SAVED_LOCATION_ROUTE")
    List<Saved_Location_Route> getAllLocationRoutes();

    @Query("SELECT * FROM SAVED_LOCATION_ROUTE, Saved_Location WHERE Saved_Location_Route.LocationID = Saved_Location.ID AND Saved_Location_Route.RouteID = :routeID")
    List<Saved_Location> getLocations(int routeID);

    @Insert
    void insert (Saved_Location_Route locationRoute);
}
