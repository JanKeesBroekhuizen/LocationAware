package com.dlvjkb.locationaware.database.preset;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.dlvjkb.locationaware.TravelType;

@Entity
public class Preset_Route {
    @PrimaryKey
    public int ID;
    public String Name;
    public String Traveltype;
}
