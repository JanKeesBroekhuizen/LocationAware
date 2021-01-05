package com.dlvjkb.locationaware.database.saved;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Saved_Route {
    @PrimaryKey
    public int ID;
    public String Traveltype;
}
