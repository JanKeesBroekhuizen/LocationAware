package com.dlvjkb.locationaware.database.preset;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "PRESET_LOCATION_ROUTE",
        primaryKeys = {"RouteID", "LocationID"},
        foreignKeys = {
            @ForeignKey(entity = Preset_Location.class,
                        parentColumns = "ID",
                        childColumns = "LocationID"),
            @ForeignKey(entity = Preset_Route.class,
                        parentColumns = "ID",
                        childColumns = "RouteID")
        })
public class Preset_Location_Route {
    public int RouteID;
    public int LocationID;
}
