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

import com.dlvjkb.locationaware.database.DB_Geocache;

import org.osmdroid.util.GeoPoint;

public class GeocacheLocationScreen extends Dialog {

    public static TravelType travelType;
    private OnGeoLocationStartListener listener;
    private GeoPoint geoPoint;
    private DB_Geocache geocache;
    private ImageButton btnCar;
    private ImageButton btnWalk;
    private ImageButton btnBike;
    private ImageButton btnSelected;
    private TextView tvCurrentGeopoint;
    private TextView tvDestinationGeopoint;
    private TextView tvGeocacheName;
    private Button btnStartGeocache;

    public GeocacheLocationScreen(@NonNull Context context, GeoPoint currentGeopoint, DB_Geocache geocache, OnGeoLocationStartListener listener) {
        super(context);
        setContentView(R.layout.activity_geocachelocation);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnCar = findViewById(R.id.ibGeocacheCarIcon);
        btnWalk = findViewById(R.id.ibGeocacheWalkIcon);
        btnBike = findViewById(R.id.ibGeocacheBikeIcon);
        tvCurrentGeopoint = findViewById(R.id.tvGeoachPopupValueCurrent);
        tvDestinationGeopoint = findViewById(R.id.tvGeoachPopupValueDestGeo);
        tvGeocacheName = findViewById(R.id.tvGeoachPopupName);
        btnStartGeocache = findViewById(R.id.btnGeocacheStart);
        this.geoPoint = currentGeopoint;
        this.geocache = geocache;
        this.listener = listener;

        btnCar.setOnClickListener(v -> onButtonCarClicked(v));
        btnBike.setOnClickListener(v -> onButtonBikeClicked(v));
        btnWalk.setOnClickListener(v -> onButtonWalkClicked(v));
        btnStartGeocache.setOnClickListener(v -> onButtonGeocachStartClick(v));
        onButtonWalkClicked(null);
        changeDialogText();
    }

    public void changeDialogText(){
        tvCurrentGeopoint.setText(geoPoint.getLatitude() + ",\n " + geoPoint.getLongitude());
        tvDestinationGeopoint.setText(geocache.Latitude + ",\n " + geocache.Longitude);
        tvGeocacheName.setText(geocache.Name);
    }

    public void onButtonGeocachStartClick(View view){
        this.listener.onGeolocationStartClicked(geoPoint,geocache,travelType);
        this.dismiss();
    }

    public void onButtonCarClicked(View view){
        this.btnSelected = btnCar;
        this.btnCar.setBackgroundResource(R.drawable.rounded_block_selected);
        this.btnWalk.setBackgroundResource(R.drawable.rounded_block);
        this.btnBike.setBackgroundResource(R.drawable.rounded_block);
        travelType = TravelType.DRIVING_CAR;
    }
    public void onButtonWalkClicked(View view){
        this.btnSelected = btnWalk;
        this.btnWalk.setBackgroundResource(R.drawable.rounded_block_selected);
        this.btnCar.setBackgroundResource(R.drawable.rounded_block);
        this.btnBike.setBackgroundResource(R.drawable.rounded_block);
        travelType = TravelType.FOOT_WALKING;
    }
    public void onButtonBikeClicked(View view){
        this.btnSelected = btnBike;
        this.btnBike.setBackgroundResource(R.drawable.rounded_block_selected);
        this.btnWalk.setBackgroundResource(R.drawable.rounded_block);
        this.btnCar.setBackgroundResource(R.drawable.rounded_block);
        travelType = TravelType.CYCLING_REGULAR;
    }
}
