package com.dlvjkb.locationaware.database;

import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {DB_Location.class, DB_Route.class, DB_Location_Route.class}, version = 1)
public abstract class Database extends RoomDatabase {
    public abstract LocationDoa locationDoa();
    public abstract RouteDao routeDao();
    public abstract LocationRouteDao locationRouteDao();
}
