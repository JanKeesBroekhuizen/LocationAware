package com.dlvjkb.locationaware;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dlvjkb.locationaware.data.Route;
import com.dlvjkb.locationaware.recyclerview.RouteDirectionsAdapter;

public class ChosenRouteDetailActivity extends AppCompatActivity {

    private TextView tvStartingPoint;
    private TextView tvEndingPoint;
    private ImageView ivTravelType;
    private Route chosenRoute;
    private TextView tvRouteDistance;
    private TextView tvRouteDuration;
    private RecyclerView rvDirections;
    private RouteDirectionsAdapter directionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosenroutedetail);
        chosenRoute = (Route) getIntent().getSerializableExtra("ROUTE");

        tvStartingPoint = findViewById(R.id.tvRouteDetailStartingPoint);
        tvEndingPoint = findViewById(R.id.tvRouteDetailEndingPoint);
        ivTravelType = findViewById(R.id.ivTravelType);
        tvRouteDistance = findViewById(R.id.tvRouteDetailDistance);
        tvRouteDuration = findViewById(R.id.tvRouteDetailDuration);

        tvStartingPoint.setText(chosenRoute.routeStartAddress);
        tvEndingPoint.setText(chosenRoute.routeEndAddress);
        tvRouteDistance.setText(String.format("%.2f",chosenRoute.features.get(0).property.segments.get(0).distance/1000) + " KM");
        tvRouteDuration.setText(setRouteTravelTime());

        setTravelTypeIcon();
        initializeRecyclerView();
    }

    private void setTravelTypeIcon(){
        if (chosenRoute.metadata.query.profile.equals(TravelType.getTravelType(TravelType.DRIVING_CAR))){
            ivTravelType.setImageResource(R.drawable.icon_traveltype_car);
        }
        else if (chosenRoute.metadata.query.profile.equals(TravelType.getTravelType(TravelType.CYCLING_REGULAR))){
            ivTravelType.setImageResource(R.drawable.icon_traveltype_bike);
        }
        else if (chosenRoute.metadata.query.profile.equals(TravelType.getTravelType(TravelType.FOOT_WALKING))){
            ivTravelType.setImageResource(R.drawable.icon_traveltype_walking);
        }
    }

    public void onButtonStopClick(View view){
        RouteInformationPopup.routeStartGeoPoint = null;
        RouteInformationPopup.routeEndGeoPoint = null;
        finish();
    }

    public void onButtonReturnClick(View view){
        finish();
    }

    private void initializeRecyclerView(){
        rvDirections = findViewById(R.id.rvRouteDetailDirections);
        directionsAdapter = new RouteDirectionsAdapter(this,chosenRoute.features.get(0).property.segments.get(0).steps);
        rvDirections.setLayoutManager(new LinearLayoutManager(this));
        rvDirections.setAdapter(directionsAdapter);
    }

    private String setRouteTravelTime(){
        int totalSeconds = (int)chosenRoute.features.get(0).property.segments.get(0).duration;
        String ss = String.format("%02d",totalSeconds%60);
        String mm = String.format("%02d",(totalSeconds % 3600)/60);
        String hh = String.format("%02d",totalSeconds / 3600);

        return hh + ":" + mm + ":" + ss;
    }
}
