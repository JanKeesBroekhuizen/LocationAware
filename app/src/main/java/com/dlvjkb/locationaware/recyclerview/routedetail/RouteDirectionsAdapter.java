package com.dlvjkb.locationaware.recyclerview.routedetail;

import android.content.Context;
import android.util.Log;
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
        holder.tvDistanceValue.setText(step.getDistance() + " " + context.getResources().getString(R.string.String_Meters));
        holder.tvDurationValue.setText(getDuration(step.getDuration()));
        holder.ivDirections.setImageResource(getDirectionTypeIcon(step.getType()));
    }

    private String getDuration(double duration){
        int totalSeconds = (int)duration;
        String ss = String.format("%2d",totalSeconds % 60);
        int hh = totalSeconds / 3600;
        String mm = String.format("%2d", ((totalSeconds % 3600) / 60) + hh * 60);

        if ((((totalSeconds % 3600) / 60) + (hh * 60)) == 0){
            return ss + " " + context.getResources().getString(R.string.String_Seconds);
        } else {
            return mm + " " + context.getResources().getString(R.string.String_Minutes) + " " + context.getResources().getString(R.string.String_And) + " " + ss + " " + context.getResources().getString(R.string.String_Seconds);
        }
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
