package com.dlvjkb.locationaware.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DB_Location {
    @PrimaryKey
    public int ID;
    public String City;
    public String Street;
    public int Housenumber;
}
