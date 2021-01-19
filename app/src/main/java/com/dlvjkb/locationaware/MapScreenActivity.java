package com.dlvjkb.locationaware;

import android.Manifest;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.dlvjkb.locationaware.data.RouteMapper;
import com.dlvjkb.locationaware.data.RouteViewModel;
import com.dlvjkb.locationaware.data.Route;
import com.dlvjkb.locationaware.database.geocache.DB_Geocache;
import com.dlvjkb.locationaware.database.DatabaseManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
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
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MapScreenActivity extends AppCompatActivity implements RouteStartListener {

    public static String APIKEY = "5b3ce3597851110001cf62487e88103431e54b0a846066f367b0b015";
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView mapView = null;
    private boolean finished = false;
    private GeoPoint currentLocationGeoPoint = null;
    private GeoPoint searchLocationGeoPoint = null;
    private EditText etSearchCityName;
    private EditText etSearchStreetName;
    private EditText etSearchStreetNumber;
    private IMapController mapController;
    private Boolean getCoordinatesFinished = false;
    private Boolean geocacheMode = false;
    private Intent locationService;
    private List<Marker> geocacheMarkers;
    private DatabaseManager databaseManager;
    private Route route;
    private Polyline line;
    private ArrayList<Marker> markers;
    private RouteViewModel viewModel;
    private Boolean focussedOnUser = false;
    private ImageButton ibCurrentLocation;
    private ImageButton ibGeoCacheMode;
    private RouteMapper routeMapper;
    private SharedPreferences sharedPreferences;
    private MyLocationNewOverlay locationNewOverlay;
    private HashMap<Integer,Polygon> circleList;
    private OpenRouteServiceCallback openRouteServiceCallback;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocationEvent(LocationService.LocationEvent event) {
        currentLocationGeoPoint = event.getGeoPoint();
//        currentLocationMarker.setPosition(currentLocationGeoPoint);
        Log.d(LocationService.LocationEvent.class.getName(), currentLocationGeoPoint.getLatitude() + "," + currentLocationGeoPoint.getLongitude());
        locationNewOverlay.disableMyLocation();
        locationNewOverlay.enableMyLocation();
        if (focussedOnUser){
            mapController.animateTo(currentLocationGeoPoint,20.0,100L,locationNewOverlay.getLastFix().getBearing());
            if (currentLocationGeoPoint.getLongitude() == event.geoPoint.getLongitude() && currentLocationGeoPoint.getLatitude() == event.geoPoint.getLatitude()){
                mapController.setZoom(20.0);
            }
//            mapView.setMapOrientation(locationNewOverlay.getLastFix().getBearing(), true);
        }
        mapView.invalidate();
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.putLong("LastLatitude",Double.doubleToLongBits(currentLocationGeoPoint.getLatitude()));
        spEditor.putLong("LastLongitude",Double.doubleToLongBits(currentLocationGeoPoint.getLongitude()));
        spEditor.commit();
//        Log.d("PREFSETTER","" + Double.doubleToLongBits(currentLocationGeoPoint.getLongitude()) + " - " + Double.doubleToLongBits(currentLocationGeoPoint.getLongitude()));
//        Log.d("PREFSETTER2","" + Double.longBitsToDouble(Double.doubleToLongBits(currentLocationGeoPoint.getLatitude())) + " - " + Double.longBitsToDouble(Double.doubleToLongBits(currentLocationGeoPoint.getLongitude())));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGeocacheArrivedEvent(LocationService.GeocacheEvent event) {
        if (!event.geocache.IsFound) {
            Dialog geocacheFoundScreen = new GeocacheDetailLocationScreen(MapScreenActivity.this, event.geocache);
            geocacheFoundScreen.show();
            databaseManager.changeGeocacheFoundState(event.geocache, true);
            mapView.getOverlays().removeAll(circleList.values());
            circleList.remove(event.geocache.Id);
            mapView.invalidate();
            displayGeocachePoints();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "notifychannelid")
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Geocache " + event.geocache.Name + " reached!")
                    .setSmallIcon(R.drawable.icon_geocache)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(0, builder.build());
        }
        Log.d(LocationService.GeocacheModeEvent.class.getName(), "Geocache" + event.geocache.Name + "Reached");
    }

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
        sharedPreferences = getSharedPreferences("PREFERENCES",MODE_PRIVATE);
        currentLocationGeoPoint = new GeoPoint(Double.longBitsToDouble(sharedPreferences.getLong("LastLatitude",0)),Double.longBitsToDouble(sharedPreferences.getLong("LastLongitude",0)));
//        Log.d("PREFGETTER2","" + Double.longBitsToDouble(sharedPreferences.getLong("LastLatitude",0))+ " - " + Double.longBitsToDouble(sharedPreferences.getLong("LastLongitude",0)));
//        Log.d("PREFGETTER3","" + sharedPreferences.getLong("LastLatitude",0)+ " - " + sharedPreferences.getLong("LastLongitutde",0));

        Configuration.getInstance().setUserAgentValue("com.dlvjkb.locationaware");
        createNotificationChannel();

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

        line = new Polyline();
        markers = new ArrayList<>();

        viewModel = RouteViewModel.getInstance();
        viewModel.setStartListener(this);
        geocacheMarkers = new ArrayList<>();
        circleList = new HashMap<>();
        databaseManager = DatabaseManager.getInstance(this);
        openRouteServiceCallback = new OpenRouteServiceCallback();

        locationNewOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getApplicationContext()),this.mapView);
        locationNewOverlay.enableMyLocation();
        locationNewOverlay.enableFollowLocation();
        this.mapView.getOverlays().add(locationNewOverlay);
        mapView.setZoomRounding(true);

//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.icon_user_location);
//        locationNewOverlay.setDirectionArrow(bitmap,bitmap);

        routeMapper = new RouteMapper();
        databaseManager.initTotalDatabase();
        //currentLocationGeoPoint = new GeoPoint(51.92458092043162,4.480193483189705);
        mapController.setCenter(currentLocationGeoPoint);
        mapController.setZoom(18.0);

//        currentLocationMarker = new Marker(mapView);
//        currentLocationMarker.setOnMarkerClickListener((marker, mapView) -> false);
//        currentLocationMarker.setIcon(getDrawable(R.drawable.icon_current_location));
//        currentLocationMarker.setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_CENTER);
//        mapView.getOverlays().add(currentLocationMarker);

        ibCurrentLocation = findViewById(R.id.ibGPSLocation);
        ibCurrentLocation.setOnClickListener(v -> onButtonCurrentLocationClicked(v));

        ibGeoCacheMode = findViewById(R.id.ibGeocache);
        ibGeoCacheMode.setOnClickListener(v -> onButtonGeocacheClicked(v));


        createRoute();

        //Start location service for monitoring new geopoints.
        locationService = new Intent(this,LocationService.class);
        startForegroundService(locationService);
    }

    @Override
    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        mapView.onResume(); //needed for compass, my location overlays, v6.0.0 and up
        if (!viewModel.getIsDrawingRoute().getValue()){
            mapView.getOverlayManager().remove(line);
            mapView.getOverlayManager().removeAll(markers);
            mapView.invalidate();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        if (mapView == null){
            return;
        }
        mapView.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        locationNewOverlay.onResume();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
        locationNewOverlay.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDetach();
        stopService(locationService);
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
        focussedOnUser = !focussedOnUser;
        if (!focussedOnUser){
            ibCurrentLocation.setImageResource(R.drawable.icon_user_location);
            mapView.setMapOrientation(0);
//            mapController.setZoom(19.0);
            Toast.makeText(this,"Focussed OFF",Toast.LENGTH_SHORT).show();
        } else if (focussedOnUser){
            mapController.animateTo(currentLocationGeoPoint,20.0,100L,locationNewOverlay.getLastFix().getBearing());
//            mapController.setZoom(20.0);
            ibCurrentLocation.setImageResource(R.drawable.icon_user_location_focussed);
//            mapView.setMapOrientation(locationNewOverlay.getLastFix().getBearing() -180);
            Toast.makeText(this,"Focussed ON",Toast.LENGTH_SHORT).show();
        }
    }

    public void onButtonInformationClicked(View view){
        Toast.makeText(getApplicationContext(),"INFORMATION POP UP",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(MapScreenActivity.this, RouteInformationPopup.class);
        startActivity(intent);
    }

    public void onButtonGeocacheClicked(View view){
        geocacheMode = !geocacheMode;
        if (geocacheMode){
            ibGeoCacheMode.setImageResource(R.drawable.icon_geocache_selected);
        }else {
            ibGeoCacheMode.setImageResource(R.drawable.icon_geocache);
        }
        displayGeocachePoints();
    }

    public void onButtonSearchClicked(View view){
        getCoordinatesFinished = false;

        String response = openRouteServiceCallback.getLocationResponse(
                "5b3ce3597851110001cf62487e88103431e54b0a846066f367b0b015",
                etSearchStreetName.getText().toString() + " " + etSearchStreetNumber.getText().toString(),
                etSearchCityName.getText().toString()
        );
        try {
            JSONObject responseJson = new JSONObject(response);
            double[] coordinates = jsonArrayToArray(responseJson.getJSONArray("features").getJSONObject(0).getJSONObject("geometry").getJSONArray("coordinates"));
            System.out.println(coordinates[0] + " " + coordinates[1]);
            currentLocationGeoPoint.setLatitude(coordinates[1]);
            currentLocationGeoPoint.setLongitude(coordinates[0]);
            getCoordinatesFinished = true;
        }catch (JSONException e) {
            e.printStackTrace();
        }

        while (!getCoordinatesFinished){ /*waiting for location*/ }

        mapController.animateTo(searchLocationGeoPoint);
        mapController.setZoom(18.0);

//        Marker marker = new Marker(mapView);
//        marker.setPosition(currentLocationGeoPoint);
//        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
//        mapView.getOverlays().add(marker);
    }

    public void createRoute(){
        if (viewModel.getRoute().getValue() != null) {
            if (viewModel.getRoute().getValue().size() != 0) {
                finished = false;
                ArrayList<GeoPoint> geoPoints = new ArrayList<>();
                String routeResponse = openRouteServiceCallback.getRouteResponse(
                        APIKEY,
                        (ArrayList<GeoPoint>) viewModel.getRoute().getValue(),
                        "https://api.openrouteservice.org/v2/directions/" + TravelType.getTravelType(viewModel.getTravelType().getValue()) + "/geojson",
                        Locale.getDefault().getLanguage()
                );

                try {
                    JSONObject responseJson = new JSONObject(routeResponse);
                    route = routeMapper.mapRoute(responseJson, viewModel.getBeginEndPoint().getValue());

                    for (double[] coordinate : route.getCoordinates()) {
                        geoPoints.add(new GeoPoint(coordinate[1], coordinate[0]));
                    }
                    finished = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                while (!finished) { /*waiting for route*/ }
                System.out.println("finished: " + finished + "    Geopoints: " + geoPoints.size());

                if (mapView != null){
                mapController.setCenter(geoPoints.get(0));
                mapController.setZoom(18.0f);
                drawLine(geoPoints);
                drawMarkers();
                }
            }
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
                Intent intent = new Intent(MapScreenActivity.this, ChosenRouteDetailActivity.class);
                intent.putExtra("ROUTE", route);
                startActivity(intent);
                return false;
            }
        });
        mapView.getOverlayManager().add(line);
    }

    public void drawMarkers(){
        markers = new ArrayList<>();
        for (int i = 0; i < route.getWayPoints().length; i++) {
            int waypoint = route.getWayPoints()[i];
            double[] coordinates = route.getCoordinates().get(waypoint);
            Marker marker = new Marker(mapView);
            marker.setPosition(new GeoPoint(coordinates[1], coordinates[0]));
            marker.setTitle(viewModel.getBeginEndPoint().getValue().get(i));
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            markers.add(marker);
        }
        mapView.getOverlays().addAll(markers);
    }

    @Override
    public void onRouteStartClicked() {
        Toast.makeText(this, "RouteStarted", Toast.LENGTH_SHORT).show();
        createRoute();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notifychannel";
            String description = "notifydescription";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifychannelid", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void displayGeocachePoints() {
        Log.d("MAP STATE:", mapView + "");
        if (mapView != null) {
            if (geocacheMode) {
                List<DB_Geocache> geocaches = DatabaseManager.getInstance(this).getGeocaches();
                List<GeoPoint> geoPoints = new ArrayList<>();
                for (DB_Geocache geocache : geocaches) {
                    GeoPoint geoPoint = new GeoPoint(geocache.Latitude, geocache.Longitude);
                    geoPoints.add(geoPoint);
                    if (geocache.IsFound) {
                        Marker marker = new Marker(mapView);
                        marker.setPosition(geoPoint);
                        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                        marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker, MapView mapView) {
                                Toast.makeText(MapScreenActivity.this, "CLICK", Toast.LENGTH_SHORT).show();
                                Dialog geocacheDetailLocationScreen = new GeocacheDetailLocationScreen(MapScreenActivity.this, geocache);
                                geocacheDetailLocationScreen.show();
                                return false;
                            }
                        });
                        geocacheMarkers.add(marker);
                    } else {
                            createGeoCacheCircle(geocache);
                            Log.e("HALLO GEOCACHE HIER IN CIRCLE",circleList.get(geocache.Id).hashCode() + "");
                            mapView.getOverlays().add(circleList.get(geocache.Id));
                    }
                }
                mapView.getOverlays().addAll(geocacheMarkers);
                mapView.invalidate();
                EventBus.getDefault().post(new LocationService.GeocacheModeEvent(geocacheMode));
                Log.v("GEOACHE MODE:", "" + geocacheMode);
            }
            if (!geocacheMode){
                mapView.getOverlays().removeAll(geocacheMarkers);
                mapView.getOverlays().removeAll(circleList.values());
                mapView.invalidate();
                Log.v("GEOACHE MODE:", "" + geocacheMode);
                EventBus.getDefault().post(new LocationService.GeocacheModeEvent(geocacheMode));
            }
        }
    }

    public double[] jsonArrayToArray(JSONArray array){
        final double[] coordinatesArray = new double[array.length()];
        for (int jsonArrayIndex = 0; jsonArrayIndex < array.length(); jsonArrayIndex++){
            try {
                coordinatesArray[jsonArrayIndex] = array.getDouble(jsonArrayIndex);
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return coordinatesArray;
    }

    private void createGeoCacheCircle(DB_Geocache geocache){
        Polygon oPolygon = new Polygon(mapView);
        final double radius = calculateGeocacheSize(geocache.Size) + calculateGeocacheDifficulty(geocache.Difficulty);
        ArrayList<GeoPoint> circlePoints = new ArrayList<GeoPoint>();
        for (float f = 0; f < 360; f += 1){
            circlePoints.add(new GeoPoint(geocache.Latitude,geocache.Longitude ).destinationPoint(radius, f));
        }
        oPolygon.setPoints(circlePoints);
        oPolygon.getFillPaint().setColor(ContextCompat.getColor(MapScreenActivity.this,R.color.geocache_radius));
        oPolygon.setOnClickListener(new Polygon.OnClickListener() {
            @Override
            public boolean onClick(Polygon polygon, MapView mapView, GeoPoint eventPos) {
                Toast.makeText(MapScreenActivity.this, "YOU CLICKED ME", Toast.LENGTH_SHORT).show();
                Dialog locationDialog = new GeocacheLocationScreen(MapScreenActivity.this,currentLocationGeoPoint,geocache,MapScreenActivity.this);
                locationDialog.show();
                return false;
            }
        });
        Log.d("GEOCACHEIDCIRCLE",""+geocache.Id);
        circleList.put(geocache.Id,oPolygon);
    }

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

    private int calculateGeocacheDifficulty(String difficulty){
        switch (difficulty){
            default:
                return 0;
            case "easy":
                return 25;
            case "medium":
                return 50;
            case "hard":
                return 100;
        }
    }
}