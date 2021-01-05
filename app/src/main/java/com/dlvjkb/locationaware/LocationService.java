package com.dlvjkb.locationaware;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
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
import org.osmdroid.util.GeoPoint;

public class LocationService extends Service {

    private static final String TAG = LocationService.class.getName();
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;
    private LocationManager mLocationManager = null;
    public DB_Geocache geocache = null;

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

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
            EventBus.getDefault().post(new LocationEvent(new GeoPoint(location.getLatitude(),location.getLongitude())));
            for (DB_Geocache geocache : DatabaseManager.getInstance(getApplicationContext()).getGeocaches()){
                double distance = (new GeoPoint(geocache.Latitude,geocache.Longitude).distanceToAsDouble(new GeoPoint(location.getLatitude(),location.getLongitude())));
                if ( distance <= calculateGeocacheSize(geocache.Size)){
                    Log.e("DISTANCE","" + distance);
//                    Intent intent = new Intent(getApplicationContext(), BuildingDetailScreenActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    intent.putExtra("waypoint",wp);
//                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "notifychannelid")
                            .setContentTitle(getString(R.string.app_name))
                            .setContentText("Waypoint " + geocache.Name + " reached!")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                    // notificationId is a unique int for each notification that you must define
                    notificationManager.notify(0, builder.build());
                    EventBus.getDefault().post(new GeocacheEvent(geocache));
                }
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
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
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
//        this.route = (Route) intent.getSerializableExtra("geoFenceRoute");
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Notification notification = new NotificationCompat.Builder(getApplicationContext(), "notifychannelid")
                .setSmallIcon(R.drawable.icon_geocache)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("LocationAware Service Running ")
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
        Log.e(TAG, "onDestroy");
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
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private int calculateGeocacheSize(String size){
        switch (size){
            default:
                return 100;
            case "small":
                return 10;
            case "medium":
                return 25;
            case "large":
                return 50;
        }
    }
}