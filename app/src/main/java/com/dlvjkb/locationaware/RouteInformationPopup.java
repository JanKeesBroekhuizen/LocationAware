package com.dlvjkb.locationaware;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dlvjkb.locationaware.data.RouteViewModel;
import com.dlvjkb.locationaware.database.preset.Preset_Location;
import com.dlvjkb.locationaware.database.DatabaseManager;
import com.dlvjkb.locationaware.database.saved.Saved_Location;
import com.dlvjkb.locationaware.database.saved.Saved_Location_Route;
import com.dlvjkb.locationaware.database.saved.Saved_Route;
import com.dlvjkb.locationaware.recyclerview.popuppreset.PresetRouteClickListener;
import com.dlvjkb.locationaware.recyclerview.popuppreset.PresetRoutesAdapter;
import com.dlvjkb.locationaware.recyclerview.popupsaved.SavedRouteClickListener;
import com.dlvjkb.locationaware.recyclerview.popupsaved.SavedRoutesAdapter;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RouteInformationPopup extends AppCompatActivity implements PresetRouteClickListener, SavedRouteClickListener {

//    public static ArrayList<GeoPoint> routePoints;
//    public static ArrayList<String> routeAddresses;
//    public static GeoPoint routeStartGeoPoint = null;
//    public static GeoPoint routeEndGeoPoint = null;
//    public static String routeStartAddress;
//    public static String routeEndAddress;
//    public static TravelType travelType;
    private RecyclerView rvPresetRoutes;
    private RecyclerView rvSavedRoutes;
    private EditText etRouteStartCityName;
    private EditText etRouteStartStreetName;
    private EditText etRouteStartStreetNumber;
    private EditText etRouteEndCityName;
    private EditText etRouteEndStreetName;
    private EditText etRouteEndStreetNumber;
    private ImageButton btnCar;
    private ImageButton btnWalk;
    private ImageButton btnBike;
//    private ImageButton btnSelected;
    private GeoPoint geoPoint;
    private Boolean finished;
    private DatabaseManager databaseManager;
    private RouteViewModel viewModel;
    private SavedRoutesAdapter savedRoutesAdapter;
    private SwipeRefreshLayout refreshLayoutSavedRoutes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_routeinformation);

        //Animation:
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;
        getWindow().setAttributes(params);

        //Initialisation views:
        etRouteStartCityName = findViewById(R.id.etStartAddressCity);
        etRouteStartStreetName = findViewById(R.id.etStartAddressStreet);
        etRouteStartStreetNumber = findViewById(R.id.etStartAddressNumber);
        etRouteEndCityName = findViewById(R.id.etEndAddressCity);
        etRouteEndStreetName = findViewById(R.id.etEndAddressStreet);
        etRouteEndStreetNumber = findViewById(R.id.etEndAddressNumber);
        btnCar = findViewById(R.id.ibCarIcon);
        btnWalk = findViewById(R.id.ibWalkIcon);
        btnBike = findViewById(R.id.ibBikeIcon);


        //initialize databasemanager
        databaseManager = DatabaseManager.getInstance(getApplicationContext());
        databaseManager.initTotalDatabase();

        viewModel = RouteViewModel.getInstance();
        onButtonWalkClicked(null);
        //initialize new Arraylists
//        routePoints = new ArrayList<>();
//        routeAddresses = new ArrayList<>();

        initRefreshViews();
        initRecyclerViews();
        testQueries();
        setTestText();
    }

    public void onButtonSearchRouteClicked(View view){
        ArrayList<GeoPoint> routePoints = new ArrayList<>();
        ArrayList<String> routeAddresses = new ArrayList<>();

        Log.d("onButtonSearchRouteClicked Start", "Street: " + etRouteStartStreetName.getText().toString() + " Number: " + etRouteStartStreetNumber.getText().toString() + " City: " + etRouteStartCityName.getText().toString());
        Log.d("onButtonSearchRouteClicked End", "Street: " + etRouteEndStreetName.getText().toString() + " Number: " + etRouteEndStreetNumber.getText().toString() + " City: " + etRouteEndCityName.getText().toString());

        GeoPoint routeStartGeoPoint = AddressToGeoPoint(etRouteStartStreetName.getText().toString() + " " + etRouteStartStreetNumber.getText().toString(), etRouteStartCityName.getText().toString());
        GeoPoint routeEndGeoPoint = AddressToGeoPoint(etRouteEndStreetName.getText().toString() + " " + etRouteEndStreetNumber.getText().toString(), etRouteEndCityName.getText().toString());
        if (routeStartGeoPoint == null || routeEndGeoPoint == null){
            return;
        }
        routePoints.add(routeStartGeoPoint);
        routeAddresses.add(etRouteStartStreetName.getText().toString() + " " + etRouteStartStreetNumber.getText().toString() + "\n" + etRouteStartCityName.getText().toString());
        routePoints.add(routeEndGeoPoint);
        routeAddresses.add(etRouteEndStreetName.getText().toString() + " " + etRouteEndStreetNumber.getText().toString() + "\n" + etRouteEndCityName.getText().toString());

        startRoute(routePoints,routeAddresses,viewModel.getTravelType().getValue());
    }

    public void onButtonSaveRouteClicked(View view){
        Saved_Route route = new Saved_Route();
        route.ID = databaseManager.getSavedRoutes().size() + 1;
        route.Traveltype = TravelType.getTravelType(viewModel.getTravelType().getValue());

        Saved_Location startLocation = new Saved_Location();
        startLocation.ID = databaseManager.getSavedLocations().size() + 1;
        startLocation.City = etRouteStartCityName.getText().toString();
        startLocation.Street = etRouteStartStreetName.getText().toString();
        startLocation.Housenumber = Integer.parseInt(etRouteStartStreetNumber.getText().toString());

        Saved_Location destinationLocation = new Saved_Location();
        destinationLocation.ID = databaseManager.getSavedLocations().size() + 2;
        destinationLocation.City = etRouteEndCityName.getText().toString();
        destinationLocation.Street = etRouteEndStreetName.getText().toString();
        destinationLocation.Housenumber = Integer.parseInt(etRouteEndStreetNumber.getText().toString());

        Saved_Location_Route startLocationRoute = new Saved_Location_Route();
        startLocationRoute.LocationID = startLocation.ID;
        startLocationRoute.RouteID = route.ID;

        Saved_Location_Route destinationLocationRoute = new Saved_Location_Route();
        destinationLocationRoute.LocationID = destinationLocation.ID;
        destinationLocationRoute.RouteID = route.ID;

        //check if route is already in the database!
        boolean created = false;
        if (databaseManager.getSavedRoutes().size() != 0){
            for (Saved_Route saved_route : databaseManager.getSavedRoutes()){
                if (saved_route.Traveltype.equals(route.Traveltype)){
                    List<Saved_Location> routeLocations = databaseManager.getSavedLocationsFromRoute(saved_route.ID);
                    if ((routeLocations.get(0).City.equals(startLocation.City) && routeLocations.get(0).Street.equals(startLocation.Street) && routeLocations.get(0).Housenumber == startLocation.Housenumber) &&
                            (routeLocations.get(1).City.equals(destinationLocation.City) && routeLocations.get(1).Street.equals(destinationLocation.Street) && routeLocations.get(1).Housenumber == destinationLocation.Housenumber)){
                        created = true;
                    }
                }
            }
        }

        if (created){
            Toast.makeText(this, "Route already created!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Route saved and started!", Toast.LENGTH_SHORT).show();
            databaseManager.insertSavedRoute(route, startLocation, destinationLocation, startLocationRoute, destinationLocationRoute);
            savedRoutesAdapter.notifyDataSetChanged();
            onButtonSearchRouteClicked(null);
        }
    }

    public GeoPoint AddressToGeoPoint(String address, String city){
        finished = false;
        OpenRouteServiceCallback openRouteServiceCallback = new OpenRouteServiceCallback();
        String response = openRouteServiceCallback.getLocationResponse(MapScreenActivity.APIKEY, address, city);

        JSONObject responseJson = null;
        try {
            responseJson = new JSONObject(response);
            if (responseJson.getJSONArray("features").length() == 0){
                Toast.makeText(this, "Can't find " + address + ", " + city , Toast.LENGTH_SHORT).show();
                return null;
            }
            double[] coordinates = jsonArrayToArray(responseJson.getJSONArray("features").getJSONObject(0).getJSONObject("geometry").getJSONArray("coordinates"));
            System.out.println(coordinates[0] + " " + coordinates[1]);
            geoPoint = new GeoPoint(coordinates[1],coordinates[0]);
            finished = true;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        while (!finished){/* waiting for location */}
        return geoPoint;
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

    public void setTestText(){
        etRouteStartCityName.setText("Rotterdam");
        etRouteStartStreetName.setText("Pascalweg");
        etRouteStartStreetNumber.setText("147");
        etRouteEndCityName.setText("Bleskensgraaf");
        etRouteEndStreetName.setText("Reigerstraat");
        etRouteEndStreetNumber.setText("26");
    }

    public void testQueries(){
        System.out.println("LOCATIONS: " + databaseManager.getPresetLocations().size());
        System.out.println("ROUTES: " + databaseManager.getPresetRoutes().size());
        System.out.println("LOCATION_ROUTES: " + databaseManager.getPresetLocationRoutes().size());
    }

    public void onButtonCarClicked(View view){
//        this.btnSelected = btnCar;
        this.btnCar.setBackgroundResource(R.drawable.rounded_block_selected);
        this.btnWalk.setBackgroundResource(R.drawable.rounded_block);
        this.btnBike.setBackgroundResource(R.drawable.rounded_block);
        viewModel.setTravelType(TravelType.DRIVING_CAR);
    }
    public void onButtonWalkClicked(View view){
//        this.btnSelected = btnWalk;
        this.btnWalk.setBackgroundResource(R.drawable.rounded_block_selected);
        this.btnCar.setBackgroundResource(R.drawable.rounded_block);
        this.btnBike.setBackgroundResource(R.drawable.rounded_block);
        viewModel.setTravelType(TravelType.FOOT_WALKING);
    }
    public void onButtonBikeClicked(View view){
//        this.btnSelected = btnBike;
        this.btnBike.setBackgroundResource(R.drawable.rounded_block_selected);
        this.btnWalk.setBackgroundResource(R.drawable.rounded_block);
        this.btnCar.setBackgroundResource(R.drawable.rounded_block);
        viewModel.setTravelType(TravelType.CYCLING_REGULAR);
    }

    private void initRecyclerViews(){
        rvPresetRoutes = findViewById(R.id.rvPresetRoutes);
        rvPresetRoutes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));

        PresetRoutesAdapter presetRoutesAdapter = new PresetRoutesAdapter(this, databaseManager.getPresetRoutes(), this);
        rvPresetRoutes.setAdapter(presetRoutesAdapter);
        presetRoutesAdapter.notifyDataSetChanged();


        rvSavedRoutes = findViewById(R.id.rvSavedRoutes);
        rvSavedRoutes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));

        savedRoutesAdapter = new SavedRoutesAdapter(this, databaseManager.getSavedRoutes(), this);
        rvSavedRoutes.setAdapter(savedRoutesAdapter);
        savedRoutesAdapter.notifyDataSetChanged();
    }

    private void initRefreshViews(){
        refreshLayoutSavedRoutes = findViewById(R.id.srvSavedRoute);
        refreshLayoutSavedRoutes.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                savedRoutesAdapter.notifyDataSetChanged();
                refreshLayoutSavedRoutes.setRefreshing(false);
            }
        });
    }

    //Start a route from the presets.
    @Override
    public void onPresetRouteClicked(int position) {
        ArrayList<GeoPoint> routePoints = new ArrayList<>();
        ArrayList<String> routeAddresses = new ArrayList<>();

        Toast.makeText(getApplicationContext(), "PresetRoute clicked " + position, Toast.LENGTH_SHORT).show();
        List<Preset_Location> locations = databaseManager.getPresetLocationsFromRoute(position + 1);
        for (Preset_Location location : locations){
            routePoints.add(AddressToGeoPoint(location.Street + " " + location.Housenumber, location.City));
            routeAddresses.add(location.Street + " " + location.Housenumber + "\n" + location.City);
        }
        routePoints.add(AddressToGeoPoint(locations.get(0).Street + " " + locations.get(0).Housenumber, locations.get(0).City));
        routeAddresses.add(locations.get(0).Street + " " + locations.get(0).Housenumber + "\n" + locations.get(0).City);

        startRoute(routePoints,routeAddresses,TravelType.getTravelTypeEnum(databaseManager.getPresetRoute(position + 1).Traveltype));
    }

    //Save the route in the list and show in the recyclerview and start the route.
    @Override
    public void onSavedRouteClicked(int position) {
        Toast.makeText(getApplicationContext(), "SavedRoute clicked " + position, Toast.LENGTH_SHORT).show();

        ArrayList<GeoPoint> routePoints = new ArrayList<>();
        ArrayList<String> routeAddresses = new ArrayList<>();

        List<Saved_Location> locations = databaseManager.getSavedLocationsFromRoute(position + 1);
        for (Saved_Location location : locations){
            routePoints.add(AddressToGeoPoint(location.Street + " " + location.Housenumber, location.City));
            routeAddresses.add(location.Street + " " + location.Housenumber + "\n" + location.City);
        }
        startRoute(routePoints,routeAddresses,TravelType.getTravelTypeEnum(databaseManager.getSavedRoute(position + 1).Traveltype));
    }

    //Start a route by setting the route in the viewmodel. The mapscreen will on resume show the route.
    private void startRoute(ArrayList<GeoPoint> routePoints, ArrayList<String> routeAddresses,TravelType travelType){
        if (viewModel.getRoute().getValue().size() == 0){
            viewModel.setRoute(routePoints);
            viewModel.setBeginEndPoint(routeAddresses);
            viewModel.setTravelType(travelType);
            viewModel.setIsDrawingRoute(true);

            viewModel.getStartListener().onRouteStartClicked();
            finish();
        }else {
            Toast.makeText(this,"Another route is active!", Toast.LENGTH_SHORT).show();
        }
    }
}
