package com.dlvjkb.locationaware.database.saved;

import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.dlvjkb.locationaware.database.preset.Preset_Location;
import com.dlvjkb.locationaware.database.preset.Preset_Route;

@Entity(tableName = "SAVED_LOCATION_ROUTE",
        primaryKeys = {"RouteID", "LocationID"},
        foreignKeys = {
                @ForeignKey(entity = Saved_Location.class,
                        parentColumns = "ID",
                        childColumns = "LocationID"),
                @ForeignKey(entity = Saved_Route.class,
                        parentColumns = "ID",
                        childColumns = "RouteID")
        })
public class Saved_Location_Route {
    public int RouteID;
    public int LocationID;
}