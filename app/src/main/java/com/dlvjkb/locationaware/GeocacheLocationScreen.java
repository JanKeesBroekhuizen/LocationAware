package com.dlvjkb.locationaware;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.dlvjkb.locationaware.data.RouteViewModel;
import com.dlvjkb.locationaware.database.geocache.DB_Geocache;
import org.osmdroid.util.GeoPoint;
import java.util.ArrayList;

public class GeocacheLocationScreen extends Dialog {

    private RouteStartListener listener;
    private GeoPoint geoPoint;
    private GeoPoint middlePointCircle;
    private DB_Geocache geocache;
    private ImageButton btnCar;
    private ImageButton btnWalk;
    private ImageButton btnBike;
    private final TextView tvCurrentGeopoint;
    private final TextView tvDestinationGeopoint;
    private final TextView tvGeocacheName;
    private final Button btnStartGeocache;
    private RouteViewModel viewModel;

    public GeocacheLocationScreen(@NonNull Context context, GeoPoint currentGeopoint, DB_Geocache geocache,GeoPoint middlePoint, RouteStartListener listener) {
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
        this.middlePointCircle = middlePoint;
        viewModel = RouteViewModel.getInstance();

        btnCar.setOnClickListener(v -> onButtonCarClicked(v));
        btnBike.setOnClickListener(v -> onButtonBikeClicked(v));
        btnWalk.setOnClickListener(v -> onButtonWalkClicked(v));
        btnStartGeocache.setOnClickListener(v -> onButtonGeocacheStartClick(v));
        onButtonWalkClicked(null);
        changeDialogText();
    }

    //Change text according to the values of the geocache.
    public void changeDialogText(){
        tvCurrentGeopoint.setText(geoPoint.getLatitude() + ",\n " + geoPoint.getLongitude());
        tvDestinationGeopoint.setText(geocache.Latitude + ",\n " + geocache.Longitude);
        tvGeocacheName.setText(geocache.Name);
    }

    //Start a route to the geocache.
    public void onButtonGeocacheStartClick(View view){
        ArrayList<GeoPoint> routePoints = new ArrayList<>();
        ArrayList<String> routeAddresses = new ArrayList<>();

        routePoints.add(geoPoint);
        //Check for the middlepoint and set route to it if its not null.
        if (middlePointCircle != null){
            routePoints.add(new GeoPoint(middlePointCircle.getLatitude(),middlePointCircle.getLongitude()));
        }else {
            routePoints.add(new GeoPoint(geocache.Latitude, geocache.Longitude));
        }
        routeAddresses.add("Current Location");
        routeAddresses.add(geocache.Name);

        //Check if another route is active. Else print for the user.
        if (viewModel.getRoute().getValue().size() == 0) {

            viewModel.setRoute(routePoints);
            viewModel.setBeginEndPoint(routeAddresses);
            viewModel.setIsDrawingRoute(true);

            listener.onRouteStartClicked();
        }else {
            Toast.makeText(getContext(),"Another Route is active", Toast.LENGTH_SHORT).show();
        }
        this.dismiss();
    }

    //Sets the traveltype.
    public void onButtonCarClicked(View view){
        this.btnCar.setBackgroundResource(R.drawable.rounded_block_selected);
        this.btnWalk.setBackgroundResource(R.drawable.rounded_block);
        this.btnBike.setBackgroundResource(R.drawable.rounded_block);
        viewModel.setTravelType(TravelType.DRIVING_CAR);
    }

    //Sets the traveltype.
    public void onButtonWalkClicked(View view){
        this.btnWalk.setBackgroundResource(R.drawable.rounded_block_selected);
        this.btnCar.setBackgroundResource(R.drawable.rounded_block);
        this.btnBike.setBackgroundResource(R.drawable.rounded_block);
        viewModel.setTravelType(TravelType.FOOT_WALKING);
    }

    //Sets the traveltype.
    public void onButtonBikeClicked(View view){
        this.btnBike.setBackgroundResource(R.drawable.rounded_block_selected);
        this.btnWalk.setBackgroundResource(R.drawable.rounded_block);
        this.btnCar.setBackgroundResource(R.drawable.rounded_block);
        viewModel.setTravelType(TravelType.CYCLING_REGULAR);
    }
}
