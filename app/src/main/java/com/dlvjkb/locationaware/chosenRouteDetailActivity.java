package com.dlvjkb.locationaware;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dlvjkb.locationaware.data.Route;

public class chosenRouteDetailActivity extends AppCompatActivity {

    private TextView tvStartingPoint;
    private TextView tvEndingPoint;
    private ImageView ivTravelType;
    private Route chosenRoute;
    private TextView tvRouteDistance;
    private TextView tvRouteDuration;

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
        tvRouteDistance.setText(String.format("%.2f",chosenRoute.features.get(0).property.segments.get(0).distance/1000));
        tvRouteDuration.setText(String.format("%.2f",chosenRoute.features.get(0).property.segments.get(0).duration/60));

        setTravelTypeIcon();
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

    public void onButtonReturnClick(View view){
        finish();
    }
}
