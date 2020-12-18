package com.dlvjkb.locationaware;

import android.Manifest;
import android.content.Context;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dlvjkb.locationaware.data.Route;
import com.dlvjkb.locationaware.database.DB_Geocache;
import com.dlvjkb.locationaware.database.DatabaseManager;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MapScreenActivity extends AppCompatActivity implements OnGeoLocationStartListener {

    public static String APIKEY = "5b3ce3597851110001cf62487e88103431e54b0a846066f367b0b015";
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView mapView = null;
    private boolean finished = false;
    private GeoPoint currentGeoPoint = null;
    private EditText etSearchCityName;
    private EditText etSearchStreetName;
    private EditText etSearchStreetNumber;
    private IMapController mapController;
    private Boolean getCoordinatesFinished = false;
    private Route route;
    private Polyline line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapscreen);

        requestPermissionsIfNecessary(new String[] {
                // if you need to show the current location, uncomment the line below
                // Manifest.permission.ACCESS_FINE_LOCATION,
                // WRITE_EXTERNAL_STORAGE is required in order to show the map
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        });

        Configuration.getInstance().setUserAgentValue("com.dlvjkb.locationaware");

        mapView = findViewById(R.id.osmMap);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setDestroyMode(false);
        mapView.setTag("mapView");
        etSearchCityName = findViewById(R.id.etSearchAddressCity);
        etSearchStreetName = findViewById(R.id.etSearchAddressName);
        etSearchStreetNumber = findViewById(R.id.etSearchAddressNumber);
        mapController = mapView.getController();
        mapController.setZoom(9.5);
        mapView.setMultiTouchControls(true);

        DatabaseManager.getInstance(this).initTotalDatabase();
        currentGeoPoint = new GeoPoint(51.92458092043162,4.480193483189705);

        if (RouteInformationPopup.routePoints != null && RouteInformationPopup.routePoints.size() != 0){
            createRoute(RouteInformationPopup.routePoints, RouteInformationPopup.travelType);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        mapView.onResume(); //needed for compass, my location overlays, v6.0.0 and up
        if (RouteInformationPopup.routePoints == null){
            mapView.getOverlayManager().remove(line);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        mapView.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDetach();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    public void onButtonCurrentLocationClicked(View view){
        Toast.makeText(getApplicationContext(),"CURRENT LOCATION",Toast.LENGTH_LONG).show();
        mapController.setCenter(currentGeoPoint);
        mapController.setZoom(18.0);
    }

    public void onButtonInformationClicked(View view){
        Toast.makeText(getApplicationContext(),"INFORMATION POP UP",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(MapScreenActivity.this, RouteInformationPopup.class);
        startActivity(intent);
    }

    public void onButtonGeocacheClicked(View view){
        List<DB_Geocache> geocaches = DatabaseManager.getInstance(this).getGeocaches();
        List<GeoPoint> geoPoints = new ArrayList<>();
        for (DB_Geocache geocache : geocaches ){
            GeoPoint geoPoint = new GeoPoint(geocache.Latitude, geocache.Longitude);
            geoPoints.add(geoPoint);
            Marker marker = new Marker(mapView);
            marker.setPosition(geoPoint);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    Toast.makeText(MapScreenActivity.this, "CLICK", Toast.LENGTH_SHORT).show();
                    Dialog geocacheLocationScreen = new GeocacheLocationScreen(MapScreenActivity.this,currentGeoPoint,geocache, MapScreenActivity.this);
                    geocacheLocationScreen.show();
                    return false;
                }
            });
            mapView.getOverlays().add(marker);
        }
    }

    public void onButtonSearchClicked(View view){
        Toast.makeText(getApplicationContext(),"SEARCH",Toast.LENGTH_LONG).show();
        OpenRouteServiceConnection.getInstance().getCoordinatesOfAddress(
                "5b3ce3597851110001cf62487e88103431e54b0a846066f367b0b015",
                etSearchStreetName.getText().toString() + " " + etSearchStreetNumber.getText().toString(),
                etSearchCityName.getText().toString(),
                new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.d(MapScreenActivity.class.getName(), e.getLocalizedMessage());
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        JSONObject responseJson = null;
                        try {
                            responseJson = new JSONObject(response.body().string());
                            double[] coordinates = jsonArrayToArray(responseJson.getJSONArray("features").getJSONObject(0).getJSONObject("geometry").getJSONArray("coordinates"));
                            System.out.println(coordinates[0] + " " + coordinates[1]);
                            currentGeoPoint.setLatitude(coordinates[1]);
                            currentGeoPoint.setLongitude(coordinates[0]);
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                        getCoordinatesFinished = true;
                    }
                });
        while (!getCoordinatesFinished){}

        mapController.setCenter(currentGeoPoint);
        mapController.setZoom(18.0);

        Marker marker = new Marker(mapView);
        marker.setPosition(currentGeoPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(marker);
    }


    public double[] jsonArrayToArray(JSONArray array){
        final double[] coordinatesArray = new double[2];
        for (int jsonArrayIndex = 0; jsonArrayIndex < array.length(); jsonArrayIndex++){
            try {
                coordinatesArray[jsonArrayIndex] = array.getDouble(jsonArrayIndex);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return coordinatesArray;
    }

    public void createRoute(ArrayList<GeoPoint> routeLocations, TravelType travelType){
        ArrayList<GeoPoint> geoPoints = new ArrayList<>();
        OpenRouteServiceConnection.getInstance().getRouteMultiplePoints(
                APIKEY,
                routeLocations,
                travelType,
                Locale.getDefault().getLanguage(),
                new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.d(MapScreenActivity.class.getName(), e.getLocalizedMessage());
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        finished = false;
                        JSONObject responseJson = null;
                        try {
                            responseJson = new JSONObject(response.body().string());
                            route = new Route(responseJson, routeAddresses.get(0), routeAddresses.get(routeAddresses.size()-1));
                            ArrayList<double[]> coordinates = route.features.get(0).geometry.coordinates;

                            for (double[] coordinate : coordinates){
                                geoPoints.add(new GeoPoint(coordinate[1], coordinate[0]));
                            }
                            System.out.println("GeoPoints: " + geoPoints.size() + " Coordinates: " + coordinates.size());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        finished = true;
                    }
                }
        );

        //Hier gaat de app stuk na 3 routes starten of bij het roteren!!!!!!!!
        //TODO Fix the error!!!

        while(!finished){}
        mapController.setCenter(geoPoints.get(0));
        mapController.setZoom(18.0f);
        drawLine(geoPoints);
        if (line != null){
            mapView.getOverlayManager().add(line);
        }
    }

    public void drawLine(ArrayList<GeoPoint> geoPoints){
        line = new Polyline();
        line.setSubDescription(Polyline.class.getCanonicalName());
        //line.setWidth(20f);
        line.getOutlinePaint().setStrokeWidth(20f);
        line.getOutlinePaint().setColor(Color.RED);
        line.setPoints(geoPoints);
        line.setGeodesic(true);
        line.setInfoWindow(new BasicInfoWindow(R.layout.bonuspack_bubble, mapView));
        line.setOnClickListener(new Polyline.OnClickListener() {
            @Override
            public boolean onClick(Polyline polyline, MapView mapView, GeoPoint eventPos) {
                Intent intent = new Intent(MapScreenActivity.this,ChosenRouteDetailActivity.class);
                intent.putExtra("ROUTE", route);
                startActivity(intent);
                return false;
            }
        });
    }

    @Override
    public void onGeolocationStartClicked(GeoPoint current, DB_Geocache cache, TravelType travelType) {
        ArrayList<GeoPoint> geoPoints = new ArrayList<>();
        geoPoints.add(current);
        geoPoints.add(new GeoPoint(cache.Latitude,cache.Longitude));
        ArrayList<String> locationNames = new ArrayList<>();
        locationNames.add("CurrentLocation");
        locationNames.add(cache.Name);
        createRoutes(geoPoints,travelType,locationNames);
    }
}