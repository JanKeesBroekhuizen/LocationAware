package com.dlvjkb.locationaware.database.geocache;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class DB_Geocache implements Serializable {
    @NonNull
    @PrimaryKey
    public int Id;
    public String Name;
    public double Longitude;
    public double Latitude;
    public String Size;
    public String Difficulty;
    public String imageLink;
    public boolean IsFound;
}
