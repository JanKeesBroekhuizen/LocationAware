package com.dlvjkb.locationaware;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dlvjkb.locationaware.data.Route;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MapScreenActivity extends AppCompatActivity {

    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView mapView = null;
    private boolean finished = false;
    private GeoPoint geoPoint = null;

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



        IMapController mapController = mapView.getController();
        mapController.setZoom(9.5);
        ImageButton imageButton = findViewById(R.id.ibGPSLocation);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapController.setCenter(geoPoint);
                mapController.setZoom(18.0);
            }
        });
        ArrayList<GeoPoint> geoPoints = new ArrayList<>();
        OpenRouteServiceConnection.getInstance().getRouteInfo(
                "5b3ce3597851110001cf62487e88103431e54b0a846066f367b0b015",
                "2.681495,44.41461",
                "4.781676,51.874249",
                TravelType.DRIVING_CAR,
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
                    Route route = new Route(responseJson);
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
        });

        while(!finished){}

        mapController.setCenter(geoPoints.get(0));
        Polyline line = new Polyline();
        line.setTitle("Road back home");
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
                Toast.makeText(getApplicationContext(), "JOEJOE", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        mapView.getOverlayManager().add(line);

        OpenRouteServiceConnection.getInstance().getCoordinatesOfAddress(
                "5b3ce3597851110001cf62487e88103431e54b0a846066f367b0b015",
                "Reigerstraat 26",
                "Bleskensgraaf",
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
                    geoPoint = new GeoPoint(coordinates[1], coordinates[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        mapView.onResume(); //needed for compass, my location overlays, v6.0.0 and up
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
    }

    public void onButtonInformationClicked(View view){
        Toast.makeText(getApplicationContext(),"INFORMATION POP UP",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(MapScreenActivity.this, RouteInformationPopup.class);
        startActivity(intent);
    }

    public void onButtonSearchClicked(View view){
        Toast.makeText(getApplicationContext(),"SEARCH",Toast.LENGTH_LONG).show();
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
}