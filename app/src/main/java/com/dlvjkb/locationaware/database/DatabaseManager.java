package com.dlvjkb.locationaware.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.dlvjkb.locationaware.R;
import com.dlvjkb.locationaware.TravelType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static DatabaseManager instance = null;
    synchronized public static DatabaseManager getInstance(Context context){
        if (instance == null){
            instance = new DatabaseManager(context);
        }
        return instance;
    }

    Database database;
    Context context;

    public DatabaseManager(Context context){
        this.context = context;
        this.database = Room.databaseBuilder(context, Database.class, "database-LocationAware1.3").allowMainThreadQueries().build();
    }

    public void initTotalDatabase() {
        initTableLocation();
        initTableRoute();
        initTableLocationRoute();
        initTableGeocache();
    }

    public void initTableLocation(){
        if (database.locationDoa().getAllLocations().size() == 0){
            ArrayList<DB_Location> locations = new ArrayList<>();
            JSONArray jsonArrayLocation = readJson(R.raw.location_file);
            for (int i = 0; i < jsonArrayLocation.length(); i++) {
                JSONObject jsonObject = null;
                DB_Location location = new DB_Location();
                try {
                    jsonObject = jsonArrayLocation.getJSONObject(i);
                    location.ID = jsonObject.getInt("id");
                    location.City = jsonObject.getString("city");
                    location.Street = jsonObject.getString("street");
                    location.Housenumber = jsonObject.getInt("housenumber");
                    locations.add(location);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            database.locationDoa().insertAll(locations);
        }
    }

    public void initTableRoute(){
        if (database.routeDao().getAllRoutes().size() == 0){
            ArrayList<DB_Route> routes = new ArrayList<>();
            JSONArray jsonArrayRoutes = readJson(R.raw.route_file);
            for (int i = 0; i < jsonArrayRoutes.length(); i++) {
                JSONObject jsonObject = null;
                DB_Route route = new DB_Route();
                try {
                    jsonObject = jsonArrayRoutes.getJSONObject(i);
                    route.ID = jsonObject.getInt("id");
                    route.Name = jsonObject.getString("name");
                    route.Traveltype = jsonObject.getString("traveltype");
                    routes.add(route);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            database.routeDao().insertAll(routes);
        }
    }

    public void initTableLocationRoute(){
        if (database.locationRouteDao().getAllLocationRoutes().size() == 0){
            ArrayList<DB_Location_Route> location_routes = new ArrayList<>();
            JSONArray jsonArrayLocationRoutes = readJson(R.raw.location_route_file);
            for (int i = 0; i < jsonArrayLocationRoutes.length(); i++) {
                JSONObject jsonObject = null;
                DB_Location_Route location_route = new DB_Location_Route();
                try {
                    jsonObject = jsonArrayLocationRoutes.getJSONObject(i);
                    location_route.RouteID = jsonObject.getInt("route_id");
                    location_route.LocationID = jsonObject.getInt("location_id");
                    location_routes.add(location_route);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            database.locationRouteDao().insertAll(location_routes);
        }
    }

    public void initTableGeocache(){
        if (database.geocacheDao().getAllGeocaches().size() == 0){
            ArrayList<DB_Geocache> geocaches = new ArrayList<>();
            JSONArray jsonArrayGeocaches = readJson(R.raw.geocache_file);
            for (int i = 0; i < jsonArrayGeocaches.length(); i++) {
                JSONObject jsonObject = null;
                DB_Geocache geocache = new DB_Geocache();
                try {
                    jsonObject = jsonArrayGeocaches.getJSONObject(i);
                    geocache.Name = jsonObject.getString("name");
                    geocache.Longitude = jsonObject.getDouble("longitude");
                    geocache.Latitude = jsonObject.getDouble("latitude");
                    geocache.Size = jsonObject.getString("size");
                    geocache.Difficulty = jsonObject.getString("difficulty");
                    geocache.IsFound = jsonObject.getBoolean("isfinished");
                    geocaches.add(geocache);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            database.geocacheDao().insertAll(geocaches);
        }
        Log.d("GEOCACHES",database.geocacheDao().getAllGeocaches().size() + "");
    }

    public JSONArray readJson(int file) {
        JSONArray array = null;
        try {
            array = new JSONArray(loadJSONFromFile(file));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return array;
    }

    public String loadJSONFromFile(int file) {
        String json = null;
        try {
            InputStream inputStream = context.getResources().openRawResource(file);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public List<DB_Location> getLocations(){
        return database.locationDoa().getAllLocations();
    }

    public List<DB_Route> getRoutes(){
        return database.routeDao().getAllRoutes();
    }

    public List<DB_Location_Route> getLocationRoutes(){
        return database.locationRouteDao().getAllLocationRoutes();
    }

    public List<DB_Location> getLocationsFromRoute(int routeID){
        return database.locationRouteDao().getLocations(routeID);
    }

    public List<DB_Geocache> getGeocaches(){
        return database.geocacheDao().getAllGeocaches();
    }

    public DB_Route getRoute(int routeID){
        System.out.println("ROUTEID: " + routeID);
        System.out.println("Selected Route:   " + database.routeDao().getRoute(routeID).Name);
        return database.routeDao().getRoute(routeID);
    }
}
