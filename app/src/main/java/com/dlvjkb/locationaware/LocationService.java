package com.dlvjkb.locationaware;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.dlvjkb.locationaware.database.DatabaseManager;
import com.dlvjkb.locationaware.database.geocache.DB_Geocache;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.osmdroid.util.GeoPoint;

public class LocationService extends Service {

    private static final String TAG = LocationService.class.getName();
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;
    private LocationManager mLocationManager = null;
    private DB_Geocache geocache = null;
    private Boolean geoCacheMode = false;

    //Listen if the user has activied the cache mode.
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGeocacheModeMode(LocationService.GeocacheModeEvent event) {
        geoCacheMode = event.mode;
        Log.d("LOCATIONSERVICE-WAYPOINT:", "" + geoCacheMode);
    }

    //Class for sending the location back to the mapscreen.
    public static class LocationEvent{
        GeoPoint geoPoint;

        public LocationEvent(GeoPoint geoPoint) {
            this.geoPoint = geoPoint;
        }

        public GeoPoint getGeoPoint() {
            return geoPoint;
        }

        public void setGeoPoint(GeoPoint geoPoint) {
            this.geoPoint = geoPoint;
        }
    }

    //Class for sending geocache in vicinity event.
    public static class GeocacheEvent{
        DB_Geocache geocache;

        public GeocacheEvent(DB_Geocache geocache) {
            this.geocache = geocache;
        }

        public DB_Geocache getWaypoint() {
            return geocache;
        }

        public void setWaypoint(DB_Geocache geocache) {
            this.geocache = geocache;
        }
    }

    //Class which is used by the mapscreen to see if the user has started the geocache hunt.
    public static class GeocacheModeEvent{
        Boolean mode;

        public GeocacheModeEvent(Boolean mode) {
            this.mode = mode;
        }

        public Boolean getMode() {
            return mode;
        }

        public void setMode(Boolean mode) {
            this.mode = mode;
        }
    }

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.d(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
            EventBus.getDefault().post(new LocationEvent(new GeoPoint(location.getLatitude(),location.getLongitude())));
            if (geoCacheMode){
                //Check for geocache in the vicinity if so then send event.
                for (DB_Geocache geocache : DatabaseManager.getInstance(getApplicationContext()).getGeocaches()) {
                    double distance = (new GeoPoint(geocache.Latitude, geocache.Longitude).distanceToAsDouble(new GeoPoint(location.getLatitude(), location.getLongitude())));
                    if (distance <= calculateGeocacheSize(geocache.Size)) {
                        Log.e("DISTANCE", "" + distance);
                        //Send the event to the mapscreen which will then start a new screen.
                        EventBus.getDefault().post(new GeocacheEvent(geocache));
                    }
                }
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        EventBus.getDefault().register(this);
        super.onStartCommand(intent, flags, startId);
//        this.route = (Route) intent.getSerializableExtra("geoFenceRoute");
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        //Show a foreground notification which stays while the service is active.
        Notification notification = new NotificationCompat.Builder(getApplicationContext(), "notifychannelid")
                .setSmallIcon(R.mipmap.cachemapsicon)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        R.mipmap.cachemapsicon))
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getResources().getString(R.string.app_name) + " " + getResources().getString(R.string.String_Location_Service))
                .setPriority(NotificationCompat.PRIORITY_MIN).build();
        startForeground(1,notification);
        Log.e(TAG, "onCreate");
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.d(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    //Size calculation is necessary to check if the cache is in the vicinity.
    private int calculateGeocacheSize(String size){
        switch (size){
            default:
                return 0;
            case "small":
                return 25;
            case "medium":
                return 50;
            case "large":
                return 100;
        }
    }
}