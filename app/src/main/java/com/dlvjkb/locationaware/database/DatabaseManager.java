package com.dlvjkb.locationaware.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.dlvjkb.locationaware.R;
import com.dlvjkb.locationaware.database.geocache.DB_Geocache;
import com.dlvjkb.locationaware.database.preset.Preset_Location;
import com.dlvjkb.locationaware.database.preset.Preset_Location_Route;
import com.dlvjkb.locationaware.database.preset.Preset_Route;
import com.dlvjkb.locationaware.database.saved.SavedRouteDao;
import com.dlvjkb.locationaware.database.saved.Saved_Location;
import com.dlvjkb.locationaware.database.saved.Saved_Location_Route;
import com.dlvjkb.locationaware.database.saved.Saved_Route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
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

    private Database database;
    private Context context;

    public DatabaseManager(Context context){
        this.context = context;
        this.database = Room.databaseBuilder(context, Database.class, "database-LocationAware").allowMainThreadQueries().build();
    }

    public void initTotalDatabase() {
        initTableLocation();
        initTableRoute();
        initTableLocationRoute();
        initTableGeocache();
    }

    public void initTableLocation(){
        if (database.presetLocationDoa().getAllLocations().size() == 0){
            ArrayList<Preset_Location> locations = new ArrayList<>();
            JSONArray jsonArrayLocation = readJson(R.raw.location_file);
            for (int i = 0; i < jsonArrayLocation.length(); i++) {
                JSONObject jsonObject = null;
                Preset_Location location = new Preset_Location();
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
            database.presetLocationDoa().insertAll(locations);
        }
    }

    public void initTableRoute(){
        if (database.presetRouteDao().getAllRoutes().size() == 0){
            ArrayList<Preset_Route> routes = new ArrayList<>();
            JSONArray jsonArrayRoutes = readJson(R.raw.route_file);
            for (int i = 0; i < jsonArrayRoutes.length(); i++) {
                JSONObject jsonObject = null;
                Preset_Route route = new Preset_Route();
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
            database.presetRouteDao().insertAll(routes);
        }
    }

    public void initTableLocationRoute(){
        if (database.presetLocationRouteDao().getAllLocationRoutes().size() == 0){
            ArrayList<Preset_Location_Route> location_routes = new ArrayList<>();
            JSONArray jsonArrayLocationRoutes = readJson(R.raw.location_route_file);
            for (int i = 0; i < jsonArrayLocationRoutes.length(); i++) {
                JSONObject jsonObject = null;
                Preset_Location_Route location_route = new Preset_Location_Route();
                try {
                    jsonObject = jsonArrayLocationRoutes.getJSONObject(i);
                    location_route.RouteID = jsonObject.getInt("route_id");
                    location_route.LocationID = jsonObject.getInt("location_id");
                    location_routes.add(location_route);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            database.presetLocationRouteDao().insertAll(location_routes);
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

    public List<Preset_Location> getPresetLocations(){
        return database.presetLocationDoa().getAllLocations();
    }

    public List<Preset_Route> getPresetRoutes(){
        return database.presetRouteDao().getAllRoutes();
    }

    public List<Preset_Location_Route> getPresetLocationRoutes(){
        return database.presetLocationRouteDao().getAllLocationRoutes();
    }

    public List<Preset_Location> getPresetLocationsFromRoute(int routeID){
        return database.presetLocationRouteDao().getLocations(routeID);
    }

    public List<DB_Geocache> getGeocaches(){
        return database.geocacheDao().getAllGeocaches();
    }

    public Preset_Route getPresetRoute(int routeID){
        return database.presetRouteDao().getRoute(routeID);
    }

    public void insertSavedRoute(Saved_Route route, Saved_Location startLocation, Saved_Location destinationLocation, Saved_Location_Route startLocationRoute, Saved_Location_Route destinationLocationRoute){
        database.savedRouteDao().insert(route);
        database.savedLocationDao().insert(startLocation);
        database.savedLocationDao().insert(destinationLocation);
        database.savedLocationRouteDao().insert(startLocationRoute);
        database.savedLocationRouteDao().insert(destinationLocationRoute);
    }

    public List<Saved_Location> getSavedLocations(){
        return database.savedLocationDao().getAllLocations();
    }

    public List<Saved_Route> getSavedRoutes(){
        return database.savedRouteDao().getAllRoutes();
    }

    public List<Saved_Location> getSavedLocationsFromRoute(int routeID){
        return database.savedLocationRouteDao().getLocations(routeID);
    }

    public Saved_Route getSavedRoute(int routeID){
        return database.savedRouteDao().getRoute(routeID);
    }

    public void changeGeocacheFoundState(DB_Geocache geocache, Boolean state){
        database.geocacheDao().changeGeocacheFoundState(geocache.Name,state);
    }
}
