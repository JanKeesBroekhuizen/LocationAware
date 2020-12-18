package com.dlvjkb.locationaware;

import com.dlvjkb.locationaware.database.DB_Geocache;

import org.osmdroid.util.GeoPoint;

public interface OnGeoLocationStartListener {

    void onGeolocationStartClicked(GeoPoint current, DB_Geocache cache, TravelType travelType);
}
