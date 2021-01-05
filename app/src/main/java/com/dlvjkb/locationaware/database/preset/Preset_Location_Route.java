package com.dlvjkb.locationaware.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "LOCATION_ROUTE",
        primaryKeys = {"RouteID", "LocationID"},
        foreignKeys = {
            @ForeignKey(entity = DB_Location.class,
                        parentColumns = "ID",
                        childColumns = "LocationID"),
            @ForeignKey(entity = DB_Route.class,
                        parentColumns = "ID",
                        childColumns = "RouteID")
        })
public class DB_Location_Route {
    public int RouteID;
    public int LocationID;
}
