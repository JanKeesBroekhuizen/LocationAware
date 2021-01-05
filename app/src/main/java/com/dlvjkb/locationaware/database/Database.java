package com.dlvjkb.locationaware.database;

import androidx.room.RoomDatabase;

import com.dlvjkb.locationaware.database.geocache.DB_Geocache;
import com.dlvjkb.locationaware.database.geocache.GeocacheDao;
import com.dlvjkb.locationaware.database.preset.PresetLocationDoa;
import com.dlvjkb.locationaware.database.preset.PresetLocationRouteDao;
import com.dlvjkb.locationaware.database.preset.PresetRouteDao;
import com.dlvjkb.locationaware.database.preset.Preset_Location;
import com.dlvjkb.locationaware.database.preset.Preset_Location_Route;
import com.dlvjkb.locationaware.database.preset.Preset_Route;
import com.dlvjkb.locationaware.database.saved.SavedLocationDao;
import com.dlvjkb.locationaware.database.saved.SavedLocationRouteDao;
import com.dlvjkb.locationaware.database.saved.SavedRouteDao;
import com.dlvjkb.locationaware.database.saved.Saved_Location;
import com.dlvjkb.locationaware.database.saved.Saved_Location_Route;
import com.dlvjkb.locationaware.database.saved.Saved_Route;

@androidx.room.Database(
        entities = {
            Preset_Location.class,
            Preset_Route.class,
            Preset_Location_Route.class,

            Saved_Location.class,
            Saved_Route.class,
            Saved_Location_Route.class,

            DB_Geocache.class
        },
        version = 1)
public abstract class Database extends RoomDatabase {
    public abstract PresetLocationDoa presetLocationDoa();
    public abstract PresetRouteDao presetRouteDao();
    public abstract PresetLocationRouteDao presetLocationRouteDao();

    public abstract SavedLocationDao savedLocationDao();
    public abstract SavedRouteDao savedRouteDao();
    public abstract SavedLocationRouteDao savedLocationRouteDao();

    public abstract GeocacheDao geocacheDao();
}
