package com.dlvjkb.locationaware.database.preset;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Preset_Location {
    @PrimaryKey
    public int ID;
    public String City;
    public String Street;
    public int Housenumber;
}
