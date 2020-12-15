package com.dlvjkb.locationaware.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface LocationRouteDao {
    @Query("SELECT * FROM LOCATION_ROUTE")
    List<DB_Location_Route> getAllLocationRoutes();

    @Query("SELECT * FROM LOCATION_ROUTE, DB_Location WHERE LOCATION_ROUTE.LocationID = DB_Location.ID AND LOCATION_ROUTE.RouteID = :routeID")
    List<DB_Location> getLocations(int routeID);

    @Insert
    void insert (DB_Location_Route location_route);

    @Insert
    void insertAll(List<DB_Location_Route> location_routes);
}
