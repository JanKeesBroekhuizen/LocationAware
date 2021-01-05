package com.dlvjkb.locationaware.database.saved;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Saved_Location {
    @PrimaryKey
    public int ID;
    public String City;
    public String Street;
    public int Housenumber;
}
