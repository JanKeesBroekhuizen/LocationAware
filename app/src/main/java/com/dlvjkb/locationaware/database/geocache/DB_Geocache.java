package com.dlvjkb.locationaware.database.geocache;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DB_Geocache {
    @NonNull
    @PrimaryKey
    public String Name;
    public double Longitude;
    public double Latitude;
    public String Size;
    public String Difficulty;
    public boolean IsFound;
}
