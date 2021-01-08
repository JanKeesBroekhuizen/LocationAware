package com.dlvjkb.locationaware;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dlvjkb.locationaware.data.Route;
import com.dlvjkb.locationaware.data.RouteViewModel;
import com.dlvjkb.locationaware.data.Segment;
import com.dlvjkb.locationaware.data.Step;
import com.dlvjkb.locationaware.recyclerview.routedetail.RouteDirectionsAdapter;

import java.util.ArrayList;

public class ChosenRouteDetailActivity extends AppCompatActivity {

    private TextView tvStartingPoint;
    private TextView tvEndingPoint;
    private ImageView ivTravelType;
    private Route chosenRoute;
    private TextView tvRouteDistance;
    private TextView tvRouteDuration;
    private RecyclerView rvDirections;
    private RouteDirectionsAdapter directionsAdapter;
    private RouteViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosenroutedetail);
        viewModel = RouteViewModel.getInstance();
        chosenRoute = (Route) getIntent().getSerializableExtra("ROUTE");

        tvStartingPoint = findViewById(R.id.tvRouteDetailStartingPoint);
        tvEndingPoint = findViewById(R.id.tvRouteDetailEndingPoint);
        ivTravelType = findViewById(R.id.ivTravelType);
        tvRouteDistance = findViewById(R.id.tvRouteDetailDistance);
        tvRouteDuration = findViewById(R.id.tvRouteDetailDuration);

        tvStartingPoint.setText(chosenRoute.getLocations().get(0));
        tvEndingPoint.setText(chosenRoute.getLocations().get(chosenRoute.getLocations().size() - 1));
        tvRouteDistance.setText(String.format("%.2f", chosenRoute.getDistance()/1000) + " KM");
        tvRouteDuration.setText(setRouteTravelTime());

        setTravelTypeIcon();
        initializeRecyclerView();
    }

    private void setTravelTypeIcon(){
        if (chosenRoute.getTravelType().equals(TravelType.DRIVING_CAR)){
            ivTravelType.setImageResource(R.drawable.icon_traveltype_car);
        }
        else if (chosenRoute.getTravelType().equals(TravelType.CYCLING_REGULAR)){
            ivTravelType.setImageResource(R.drawable.icon_traveltype_bike);
        }
        else if (chosenRoute.getTravelType().equals(TravelType.FOOT_WALKING)){
            ivTravelType.setImageResource(R.drawable.icon_traveltype_walking);
        }
    }

    public void onButtonStopClick(View view){
//        RouteInformationPopup.routePoints = null;
//        RouteInformationPopup.routeAddresses = null;
        viewModel.setRoute(new ArrayList<>());
        viewModel.setBeginEndPoint(new ArrayList<>());
        viewModel.setIsDrawingRoute(false);
        finish();
    }

    public void onButtonReturnClick(View view){
        finish();
    }

    private void initializeRecyclerView(){
        rvDirections = findViewById(R.id.rvRouteDetailDirections);

        ArrayList<Step> steps = new ArrayList<>();
        for (Segment segment : chosenRoute.getSegments()){
            steps.addAll(segment.getSteps());
        }

//        int stepAmount = chosenRoute.features.get(0).property.segments.size();
//        for (int i = 0; i < stepAmount; i++) {
//            steps.addAll(chosenRoute.features.get(0).property.segments.get(i).steps);
//        }

        directionsAdapter = new RouteDirectionsAdapter(this, steps);
        rvDirections.setLayoutManager(new LinearLayoutManager(this));
        rvDirections.setAdapter(directionsAdapter);
    }

    private String setRouteTravelTime(){
        int totalSeconds = (int)chosenRoute.getDuration();
        String ss = String.format("%02d",totalSeconds%60);
        String mm = String.format("%02d",(totalSeconds % 3600)/60);
        String hh = String.format("%02d",totalSeconds / 3600);

        return hh + ":" + mm + ":" + ss;
    }
}
