package com.dlvjkb.locationaware;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dlvjkb.locationaware.data.RouteViewModel;
import com.dlvjkb.locationaware.database.geocache.DB_Geocache;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class GeocacheLocationScreen extends Dialog {

//    public static ArrayList<GeoPoint> geoPoints;
//    public static ArrayList<String> addresses;
//    public static TravelType travelType;
    private RouteStartListener listener;
    private GeoPoint geoPoint;
    private DB_Geocache geocache;
    private ImageButton btnCar;
    private ImageButton btnWalk;
    private ImageButton btnBike;
    private final TextView tvCurrentGeopoint;
    private final TextView tvDestinationGeopoint;
    private final TextView tvGeocacheName;
    private final Button btnStartGeocache;
    private RouteViewModel viewModel;

    public GeocacheLocationScreen(@NonNull Context context, GeoPoint currentGeopoint, DB_Geocache geocache, RouteStartListener listener) {
        super(context);
        setContentView(R.layout.activity_geocachelocation);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnCar = findViewById(R.id.ibGeocacheCarIcon);
        btnWalk = findViewById(R.id.ibGeocacheWalkIcon);
        btnBike = findViewById(R.id.ibGeocacheBikeIcon);
        tvCurrentGeopoint = findViewById(R.id.tvGeoachPopupValueCurrent);
        tvDestinationGeopoint = findViewById(R.id.tvGeoachPopupValueDestGeo);
        tvGeocacheName = findViewById(R.id.tvGeocachePopupName);
        btnStartGeocache = findViewById(R.id.btnGeocacheStart);
        this.geoPoint = currentGeopoint;
        this.geocache = geocache;
        this.listener = listener;
//        geoPoints = new ArrayList<>();
//        addresses = new ArrayList<>();

        viewModel = RouteViewModel.getInstance();

        btnCar.setOnClickListener(v -> onButtonCarClicked(v));
        btnBike.setOnClickListener(v -> onButtonBikeClicked(v));
        btnWalk.setOnClickListener(v -> onButtonWalkClicked(v));
        btnStartGeocache.setOnClickListener(v -> onButtonGeocacheStartClick(v));
        onButtonWalkClicked(null);
        changeDialogText();
    }

    public void changeDialogText(){
        tvCurrentGeopoint.setText(geoPoint.getLatitude() + ",\n " + geoPoint.getLongitude());
        tvDestinationGeopoint.setText(geocache.Latitude + ",\n " + geocache.Longitude);
        tvGeocacheName.setText(geocache.Name);
    }

    public void onButtonGeocacheStartClick(View view){
        List<GeoPoint> routePoints = new ArrayList<>();
        List<String> routeAddresses = new ArrayList<>();

        routePoints.add(geoPoint);
        routePoints.add(new GeoPoint(geocache.Latitude, geocache.Longitude));
        routeAddresses.add("Current Location");
        routeAddresses.add(geocache.Name);

        viewModel.setRoute(routePoints);
        viewModel.setBeginEndPoint(routeAddresses);
        viewModel.setIsDrawingRoute(true);

        listener.onRouteStartClicked();
        this.dismiss();
    }

    public void onButtonCarClicked(View view){
        this.btnCar.setBackgroundResource(R.drawable.rounded_block_selected);
        this.btnWalk.setBackgroundResource(R.drawable.rounded_block);
        this.btnBike.setBackgroundResource(R.drawable.rounded_block);
        viewModel.setTravelType(TravelType.DRIVING_CAR);
    }
    public void onButtonWalkClicked(View view){
        this.btnWalk.setBackgroundResource(R.drawable.rounded_block_selected);
        this.btnCar.setBackgroundResource(R.drawable.rounded_block);
        this.btnBike.setBackgroundResource(R.drawable.rounded_block);
        viewModel.setTravelType(TravelType.FOOT_WALKING);
    }
    public void onButtonBikeClicked(View view){
        this.btnBike.setBackgroundResource(R.drawable.rounded_block_selected);
        this.btnWalk.setBackgroundResource(R.drawable.rounded_block);
        this.btnCar.setBackgroundResource(R.drawable.rounded_block);
        viewModel.setTravelType(TravelType.CYCLING_REGULAR);
    }
}
