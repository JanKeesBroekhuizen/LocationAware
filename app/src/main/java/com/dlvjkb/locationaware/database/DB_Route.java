package com.dlvjkb.locationaware.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DB_Route {
    @PrimaryKey
    public int ID;
    public String Name;
}
