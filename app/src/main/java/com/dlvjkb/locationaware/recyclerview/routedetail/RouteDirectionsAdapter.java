package com.dlvjkb.locationaware.recyclerview.routedetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dlvjkb.locationaware.R;
import com.dlvjkb.locationaware.data.Step;

import java.util.ArrayList;

public class RouteDirectionsAdapter extends RecyclerView.Adapter<RouteDirectionsViewHolder> {


    private ArrayList<Step> steps;
    private Context context;

    public RouteDirectionsAdapter(Context context,ArrayList<Step> stepArrayList) {
        this.context = context;
        this.steps = stepArrayList;
    }

    @NonNull
    @Override
    public RouteDirectionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recylerview_item_routedirections,parent,false);
        RouteDirectionsViewHolder viewHolder = new RouteDirectionsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RouteDirectionsViewHolder holder, int position) {
        Step step = steps.get(position);
        holder.tvDescription.setText(step.getInstruction());
        holder.tvDistanceValue.setText(String.format("%.2f",step.getDistance()/1000) + " " + context.getResources().getString(R.string.recyclerview_distance));
        holder.tvDurationValue.setText(String.format("%.2f",step.getDuration()/60) + " "  + context.getResources().getString(R.string.recyclerview_seconds));
        holder.ivDirections.setImageResource(getDirectionTypeIcon(step.getType()));
    }


    @Override
    public int getItemCount() {
        return steps.size();
    }

    private int getDirectionTypeIcon(int type){
        int drawablevalue=0;
        switch (type){
            case 0:
                drawablevalue = R.drawable.turn_left;
                break;
            case 1:
                drawablevalue = R.drawable.turn_right;
                break;
            case 2:
                drawablevalue = R.drawable.sharp_left;
                break;
            case 3:
                drawablevalue = R.drawable.sharp_right;
                break;
            case 4:
                drawablevalue = R.drawable.slight_left;
                break;
            case 5:
                drawablevalue = R.drawable.slight_right;
                break;
            case 6:
                drawablevalue = R.drawable.straight;
                break;
            case 7:
                drawablevalue = R.drawable.roundabout;
                break;
            case 8:
                drawablevalue = R.drawable.roundabout;
                break;
            case 9:
                drawablevalue = R.drawable.u_turn;
                break;
            case 10:
                drawablevalue = R.drawable.destination_reached;
                break;
            case 11:
                drawablevalue = R.drawable.depart;
                break;
            case 12:
                drawablevalue = R.drawable.keep_left;
                break;
            case 13:
                drawablevalue = R.drawable.keep_right;
                break;
            default:
                drawablevalue = R.drawable.icon_info;
                break;
        }
        return drawablevalue;
    }
}
