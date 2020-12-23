package com.dlvjkb.locationaware;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dlvjkb.locationaware.data.RouteViewModel;
import com.dlvjkb.locationaware.database.DB_Location;
import com.dlvjkb.locationaware.database.Database;
import com.dlvjkb.locationaware.database.DatabaseManager;
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

        initRecyclerViews();
        testQueries();
        setTestText();
    }

    public void onButtonSearchRouteClicked(View view){
        List<GeoPoint> routePoints = new ArrayList<>();
        List<String> routeAddresses = new ArrayList<>();

        GeoPoint routeStartGeoPoint = AddressToGeoPoint(etRouteStartStreetName.getText().toString() + " " + etRouteStartStreetNumber.getText().toString(), etRouteStartCityName.getText().toString());
        routePoints.add(routeStartGeoPoint);
        routeAddresses.add(etRouteStartStreetName.getText().toString() + " " + etRouteStartStreetNumber.getText().toString() + "\n" + etRouteStartCityName.getText().toString());
        GeoPoint routeEndGeoPoint = AddressToGeoPoint(etRouteEndStreetName.getText().toString() + " " + etRouteEndStreetNumber.getText().toString(), etRouteEndCityName.getText().toString());
        routePoints.add(routeEndGeoPoint);
        routeAddresses.add(etRouteEndStreetName.getText().toString() + " " + etRouteEndStreetNumber.getText().toString() + "\n" + etRouteEndCityName.getText().toString());

        viewModel.setRoute(routePoints);
        viewModel.setBeginEndPoint(routeAddresses);
        viewModel.setIsDrawingRoute(true);

        viewModel.getStartListener().onRouteStartClicked();
        finish();
    }

    public GeoPoint AddressToGeoPoint(String address, String city){
        finished = false;
        OpenRouteServiceConnection.getInstance().getCoordinatesOfAddress(MapScreenActivity.APIKEY, address, city, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(RouteInformationPopup.class.getName(),e.getLocalizedMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                JSONObject responseJson = null;
                try {
                    responseJson = new JSONObject(response.body().string());
                    double[] coordinates = jsonArrayToArray(responseJson.getJSONArray("features").getJSONObject(0).getJSONObject("geometry").getJSONArray("coordinates"));
                    System.out.println(coordinates[0] + " " + coordinates[1]);
                    geoPoint = new GeoPoint(coordinates[1],coordinates[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finished = true;
            }
        });
        while (!finished){}
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
        System.out.println("LOCATIONS: " + databaseManager.getLocations().size());
        System.out.println("ROUTES: " + databaseManager.getRoutes().size());
        System.out.println("LOCATION_ROUTES: " + databaseManager.getLocationRoutes().size());
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

        PresetRoutesAdapter presetRoutesAdapter = new PresetRoutesAdapter(this, databaseManager.getRoutes(), this);
        rvPresetRoutes.setAdapter(presetRoutesAdapter);
        presetRoutesAdapter.notifyDataSetChanged();


//        rvSavedRoutes = findViewById(R.id.rvSavedRoutes);
//        rvSavedRoutes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
//        rvSavedRoutes.setAdapter(new SavedRoutesAdapter(this,null, this));
    }

    @Override
    public void onPresetRouteClicked(int position) {
        List<GeoPoint> routePoints = new ArrayList<>();
        List<String> routeAddresses = new ArrayList<>();

        Toast.makeText(getApplicationContext(), "PresetRoute clicked " + position, Toast.LENGTH_SHORT).show();
        List<DB_Location> locations = databaseManager.getLocationsFromRoute(position + 1);
        for (DB_Location location : locations){
            routePoints.add(AddressToGeoPoint(location.Street + " " + location.Housenumber, location.City));
            routeAddresses.add(location.Street + " " + location.Housenumber + "\n" + location.City);
        }
        routePoints.add(AddressToGeoPoint(locations.get(0).Street + " " + locations.get(0).Housenumber, locations.get(0).City));
        routeAddresses.add(locations.get(0).Street + " " + locations.get(0).Housenumber + "\n" + locations.get(0).City);

        viewModel.setRoute(routePoints);
        viewModel.setBeginEndPoint(routeAddresses);
        viewModel.setTravelType(TravelType.getTravelTypeEnum(databaseManager.getRoute(position + 1).Traveltype));
        viewModel.setIsDrawingRoute(true);

        viewModel.getStartListener().onRouteStartClicked();
        finish();
    }

    @Override
    public void onSavedRouteClicked(int position) {
        Toast.makeText(getApplicationContext(), "SavedRoute clicked " + position, Toast.LENGTH_SHORT).show();
    }
}
